package com.rogue.domain.game;

import com.rogue.data.save.SaveData;
import com.rogue.data.save.SaveService;
import com.rogue.domain.combat.CombatService;
import com.rogue.domain.enemies.Ghost;
import com.rogue.domain.enemies.Ogre;
import com.rogue.domain.enemies.SnakeMage;
import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.Player;
import com.rogue.domain.entities.Position;
import com.rogue.domain.generation.LevelGenerator;
import com.rogue.domain.items.*;
import com.rogue.domain.map.*;
import com.rogue.domain.services.EnemyFactory;
import com.rogue.domain.services.ItemFactory;
import com.rogue.domain.services.MovementService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameSession {

    private Level currentLevel;

    private Player player;

    private int levelIndex = 1;

    private GameState state =
            GameState.MENU;

    private Random random;

    private MovementService movementService;

    private CombatService combatService;

    private EnemyFactory enemyFactory;

    private ItemFactory itemFactory;

    private LevelGenerator generator;

    private TurnManager turnManager;

    private SaveService saveService;

    private long currentLevelSeed;

    private ItemType pendingSelection;

    private int mapRevision;

    private int playerMovesRemaining;

    private final MessageLog messageLog =
            new MessageLog();

    // -----------------------------------------------------------------------
    // Task 7: Adaptive difficulty
    // -----------------------------------------------------------------------

    /**
     * Bias applied to enemy count and item spawning.
     * Range: -2 (easier) .. +3 (harder).
     * Updated after each level completion based on player performance.
     */
    private int difficultyBias = 0;

    /** Player HP at the start of the current level (for performance tracking). */
    private int hpAtLevelStart;

    /** Kills credited during the current level. */
    private int killsThisLevel;

    public GameSession(
            SaveService saveService){

        this.saveService = saveService;

        random = new Random();

        movementService =
                new MovementService();

        combatService =
                new CombatService(
                        random,
                        messageLog);

        enemyFactory =
                new EnemyFactory(random);

        itemFactory =
                new ItemFactory(random);

        generator =
                new LevelGenerator(random);

        turnManager =
                new TurnManager();

        // Stay in MENU — game is started by menu selection.
    }

    // -----------------------------------------------------------------------
    // Menu / Save helpers
    // -----------------------------------------------------------------------

    /** Returns true when a resumable in-progress run exists in the save file. */
    public boolean hasSave(){

        SaveData data = saveService.load();

        return data.level > 0 && data.playerHP > 0;
    }

    /**
     * Starts a brand new run, discarding any existing save progress.
     * Transitions to RUNNING.
     */
    public void startNewGame(){

        saveService.clearRunProgress();

        messageLog.clear();

        player =
                new Player(
                        new Position(
                                10,
                                10));

        levelIndex = 1;

        difficultyBias = 0;

        currentLevelSeed =
                random.nextLong();

        state =
                GameState.RUNNING;

        pendingSelection = null;

        generateLevel();
    }

    /**
     * Resumes an existing run if one is saved; otherwise starts a new game.
     * Transitions to RUNNING.
     */
    public void continueGame(){

        SaveData data = saveService.load();

        if(data.level > 0 && data.playerHP > 0){

            restoreRun(data);

        } else {

            startNewGame();
        }
    }

    private void restoreRun(
            SaveData data){

        player =
                new Player(
                        new Position(
                                10,
                                10));

        player.restoreStats(
                data.playerHP,
                data.maxHP,
                data.strength,
                data.agility,
                data.speed > 0
                        ? data.speed
                        : 10);

        player.setTreasure(
                data.treasure);

        levelIndex = data.level;

        difficultyBias = data.difficultyBias;

        if(data.levelSeed == 0L){

            currentLevelSeed =
                    random.nextLong();

        } else {

            currentLevelSeed =
                    data.levelSeed;
        }

        saveService.applyToPlayer(
                player,
                data);

        state =
                GameState.RUNNING;

        pendingSelection = null;

        messageLog.clear();

        generateLevel();
    }

    private void generateLevel(){

        currentLevel =
                generator.generate(
                        levelIndex,
                        currentLevelSeed);

        placePlayerInStartRoom();

        currentLevel.setPlayer(
                player);

        spawnEnemies();

        spawnItems();

        mapRevision++;

        playerMovesRemaining =
                computeMovesPerTurn();

        hpAtLevelStart = player.getHealth();

        killsThisLevel = 0;

        // Clear keys when entering a new level
        player.clearKeys();

        if(levelIndex == 1){

            messageLog.add(
                    "You enter the Dungeons of Doom.");

        } else {

            messageLog.add(
                    "You descend to dungeon level "
                            + levelIndex
                            + ".");
        }
    }

    private int computeMovesPerTurn(){

        return 1
                + Math.max(
                0,
                (player.getAgility() - 10)
                        / 5);
    }

    public int getMapRevision(){

        return mapRevision;
    }

    /**
     * Moves left before enemies act (for HUD).
     */
    public int getMovesRemainingPreview(){

        return playerMovesRemaining;
    }

    public MessageLog getMessageLog(){

        return messageLog;
    }

    public String getMessageLine(){

        return messageLog.getLast();
    }

    private void applyHungerAndStarvation(){

        if(state != GameState.RUNNING){

            return;
        }

        if(player.getSatiation() > 0){
            player.tickSatiation();
        }

        if(player.getSatiation() <= 0){

            messageLog.add(
                    "You faint from hunger!");

            player.takeDamage(1);

            if(!player.isAlive()){

                gameOver();
            }
        }
    }

    private void placePlayerInStartRoom(){

        Room start =
                currentLevel
                        .getRooms()
                        .get(0);

        Position p =
                randomFloorInRoom(
                        start);

        player.getPosition()
                .setX(p.getX());

        player.getPosition()
                .setY(p.getY());
    }

    private Position randomFloorInRoom(
            Room room){

        for(int i = 0;
            i < 80;
            i++){

            int x =
                    room.getX()
                            + random.nextInt(
                            room.getWidth());

            int y =
                    room.getY()
                            + random.nextInt(
                            room.getHeight());

            if(currentLevel.isWalkable(
                    x,
                    y)){

                return new Position(
                        x,
                        y);
            }
        }

        return room.center();
    }

    // -----------------------------------------------------------------------
    // Task 7: Adaptive enemy / item spawning
    // -----------------------------------------------------------------------

    private void spawnEnemies(){

        int base =
                2 + levelIndex / 2;

        // difficultyBias adds/removes enemies (+1 per bias point)
        int adjusted =
                Math.max(
                        1,
                        base + difficultyBias);

        int count =
                Math.min(
                        adjusted,
                        currentLevel.getRooms().size() - 1);

        for(int i = 0; i < count; i++){

            Room room =
                    randomRoomExcludingStart();

            Position pos =
                    randomFloorInRoom(
                            room);

            Enemy enemy =
                    enemyFactory.create(
                            pos.copy());

            currentLevel
                    .getEnemies()
                    .add(enemy);
        }
    }

    private void spawnItems(){

        int useful =
                Math.max(
                        1,
                        8 - levelIndex / 3 - difficultyBias);

        int treasureBias =
                3 + levelIndex / 2;

        for(int i = 0;
            i < useful;
            i++){

            placeRandomItem(
                    false);
        }

        for(int i = 0;
            i < treasureBias;
            i++){

            placeRandomItem(
                    true);
        }
    }

    /**
     * Adjusts {@link #difficultyBias} based on how well the player performed
     * on the level they just completed.
     */
    private void updateDifficulty(){

        double hpRatio =
                player.getMaxHealth() > 0
                        ? (double) player.getHealth() / player.getMaxHealth()
                        : 0;

        // Player sailed through: high HP and many kills → increase difficulty
        if(hpRatio > 0.65 && killsThisLevel >= 2){

            difficultyBias =
                    Math.min(3, difficultyBias + 1);

            messageLog.add(
                    "The dungeon grows darker...");

        }
        // Player struggling: low HP or no kills → ease up
        else if(hpRatio < 0.30 || killsThisLevel == 0){

            difficultyBias =
                    Math.max(-2, difficultyBias - 1);

            messageLog.add(
                    "Fortune favors the bold — useful items await.");
        }
    }

    private void placeRandomItem(
            boolean treasurePreferred){

        Item item =
                treasurePreferred
                        ? itemFactory.treasureItem(
                        random,
                        levelIndex)
                        : itemFactory.randomItem(
                        random,
                        levelIndex);

        for(int attempt = 0;
            attempt < 40;
            attempt++){

            Room room =
                    randomRoomExcludingStart();

            Position pos =
                    randomFloorInRoom(
                            room);

            Tile t =
                    currentLevel.getMap()
                            [pos.getY()]
                            [pos.getX()];

            if(t.getItem() != null){

                continue;
            }

            t.setItem(item);

            return;
        }
    }

    private Room randomRoomExcludingStart(){

        List<Room> rooms =
                currentLevel.getRooms();

        int idx;

        do{

            idx =
                    random.nextInt(
                            rooms.size());

        }while(idx == 0);

        return rooms.get(idx);
    }

    // -----------------------------------------------------------------------
    // Player movement
    // -----------------------------------------------------------------------

    public void movePlayer(
            Direction dir){

        if(state != GameState.RUNNING){

            return;
        }

        if(pendingSelection != null){

            return;
        }

        if(player.isAsleep()){

            player.setSleepTurns(
                    player.getSleepTurns()
                            - 1);

            processEnemyTurns();

            if(state != GameState.RUNNING){

                return;
            }

            player.tickBuffs();

            turnManager.next();

            playerMovesRemaining =
                    computeMovesPerTurn();

            applyHungerAndStarvation();

            return;
        }

        // --- Task 6: door interaction ---
        // Check if the target tile is a locked door before attempting to move
        int tx = player.getPosition().getX() + dir.dx();
        int ty = player.getPosition().getY() + dir.dy();

        if(isInBounds(tx, ty)){

            Tile targetTile =
                    currentLevel.getMap()[ty][tx];

            if(targetTile.getType() == TileType.DOOR){

                handleDoorInteraction(targetTile, tx, ty);

                return;
            }
        }

        Room roomBefore =
                currentLevel.findRoomContaining(
                        player.getPosition());

        boolean moved =
                movementService.move(
                        player.getPosition(),
                        dir.dx(),
                        dir.dy(),
                        currentLevel);

        if(!moved){

            return;
        }

        player.getStatistics()
                .addMove();

        Room roomAfter =
                currentLevel.findRoomContaining(
                        player.getPosition());

        if(roomBefore != null
                && roomBefore != roomAfter){

            currentLevel.exploreRoom(
                    roomBefore);
        }

        checkItemPickup();

        checkCombat();

        if(state != GameState.RUNNING){

            return;
        }

        if(checkExit()){

            return;
        }

        playerMovesRemaining--;

        if(playerMovesRemaining > 0){

            return;
        }

        processEnemyTurns();

        if(state != GameState.RUNNING){

            return;
        }

        if(checkExit()){

            return;
        }

        player.tickBuffs();

        turnManager.next();

        playerMovesRemaining =
                computeMovesPerTurn();

        applyHungerAndStarvation();
    }

    /**
     * Handles player bumping into a locked door tile.
     * If the player has the matching key, the door opens (becomes a corridor).
     * Otherwise, a message is shown.
     */
    private void handleDoorInteraction(
            Tile doorTile,
            int tx,
            int ty){

        KeyColor color =
                doorTile.getDoorColor();

        if(color != null && player.hasKey(color)){

            // Unlock the door
            player.useKey(color);

            doorTile.setType(TileType.CORRIDOR);

            doorTile.setDoorColor(null);

            doorTile.setExplored(true);

            messageLog.add(
                    "You use the "
                            + color.name().toLowerCase()
                            + " key to open the door.");

            // Door opens but we still spend the turn
            processEnemyTurns();

            if(state != GameState.RUNNING){

                return;
            }

            player.tickBuffs();

            turnManager.next();

            playerMovesRemaining =
                    computeMovesPerTurn();

            applyHungerAndStarvation();

        } else {

            String colorName =
                    color != null
                            ? color.name().toLowerCase()
                            : "";

            messageLog.add(
                    "The "
                            + colorName
                            + " door is locked. You need the "
                            + colorName
                            + " key.");
        }
    }

    private boolean isInBounds(int x, int y){

        Tile[][] map = currentLevel.getMap();

        return y >= 0 && y < map.length
                && x >= 0 && x < map[0].length;
    }

    private void checkItemPickup(){

        Position p =
                player.getPosition();

        Tile tile =
                currentLevel
                        .getMap()
                        [p.getY()]
                        [p.getX()];

        Item item =
                tile.getItem();

        if(item == null){

            return;
        }

        // Keys are auto-collected into the key ring, not the backpack
        if(item instanceof Key key){

            tile.setItem(null);

            player.addKey(key.getKeyColor());

            messageLog.add(
                    "You pick up the "
                            + key.getKeyColor().name().toLowerCase()
                            + " key.");

            return;
        }

        boolean added =
                player.getBackpack()
                        .addItem(item);

        if(added){

            tile.setItem(null);

            messageLog.add(
                    "Picked up: "
                            + item.getName());
        }
    }

    private void checkCombat(){

        Iterator<Enemy> it =
                currentLevel.getEnemies().iterator();

        while(it.hasNext()){

            Enemy enemy = it.next();

            if(!enemy.isAlive()){
                continue;
            }

            if(enemy.getPosition()
                    .equals(player.getPosition())){

                combatService.attack(player,enemy);

                if(!enemy.isAlive()){

                    player.addTreasure(
                            random.nextInt(30)+10);

                    killsThisLevel++;

                    it.remove();

                    continue;
                }

                combatService.attack(enemy,player);

                if(!player.isAlive()){

                    state =
                            GameState.GAME_OVER;

                    return;
                }
            }
        }
    }

    private void dropTreasureForKill(
            Enemy enemy){

        int base =
                enemy.getHostilityRange()
                        * 2
                        + enemy.getStrength()
                        + enemy.getAgility()
                        + enemy.getHealth()
                        / 10;

        int amount =
                Math.max(
                        5,
                        base
                                + random.nextInt(
                                15));

        player.addTreasure(
                amount);
    }

    private void gameOver(){

        if(state == GameState.GAME_OVER){

            return;
        }

        messageLog.add(
                "You die...");

        state =
                GameState.GAME_OVER;

        saveScore();

        saveService.clearRunProgress();
    }

    private void processEnemyTurns(){

        for(Enemy enemy :
                new ArrayList<>(
                        currentLevel.getEnemies())){

            if(!enemy.isAlive()){
                continue;
            }

            moveEnemy(enemy);

            if(!player.isAlive()){

                return;
            }

            if(enemy.getPosition()
                    .equals(player.getPosition())){

                combatService.attack(
                        enemy,
                        player);

                if(player.isAlive()
                        && enemy.isAlive()){

                    combatService.attack(
                            player,
                            enemy);
                }

                if(!enemy.isAlive()){

                    dropTreasureForKill(
                            enemy);

                    killsThisLevel++;

                    player.getStatistics()
                            .addKill();
                }

                if(!player.isAlive()){

                    gameOver();

                    return;
                }
            }
        }
    }

    private void moveEnemy(
            Enemy enemy){

        if(enemy instanceof Ogre ogre){

            if(ogre.isResting()){

                ogre.wake();

                return;
            }
        }

        if(enemy instanceof Ghost ghost){

            if(random.nextBoolean()){

                ghost.toggle();
            }
        }

        int dist =
                manhattan(
                        player.getPosition(),
                        enemy.getPosition());

        if(dist > enemy.getHostilityRange()){

            idleWander(
                    enemy);

            return;
        }

        if(enemy instanceof Ghost ghost){

            ghostTeleport(
                    ghost);

            return;
        }

        if(enemy instanceof Ogre ogre){

            chaseSteps(
                    enemy,
                    2);

            return;
        }

        if(enemy instanceof SnakeMage){

            snakeMove(
                    (SnakeMage) enemy);

            return;
        }

        chaseSteps(
                enemy,
                1);
    }

    private void idleWander(
            Enemy enemy){

        int dx =
                random.nextInt(3)
                        - 1;

        int dy =
                random.nextInt(3)
                        - 1;

        if(dx == 0
                && dy == 0){

            return;
        }

        movementService.move(
                enemy.getPosition(),
                dx,
                dy,
                currentLevel);
    }

    private void ghostTeleport(
            Ghost ghost){

        Room room =
                currentLevel.findRoomContaining(
                        ghost.getPosition());

        if(room == null){

            idleWander(
                    ghost);

            return;
        }

        for(int i = 0;
            i < 12;
            i++){

            int x =
                    room.getX()
                            + random.nextInt(
                            room.getWidth());

            int y =
                    room.getY()
                            + random.nextInt(
                            room.getHeight());

            if(currentLevel.isWalkable(
                    x,
                    y)
                    && !player.getPosition()
                    .equals(
                            new Position(
                                    x,
                                    y))){

                ghost.getPosition()
                        .setX(x);

                ghost.getPosition()
                        .setY(y);

                return;
            }
        }
    }

    private void snakeMove(
            SnakeMage mage){

        int dx =
                random.nextBoolean()
                        ? 1
                        : -1;

        int dy =
                random.nextBoolean()
                        ? 1
                        : -1;

        movementService.move(
                mage.getPosition(),
                dx,
                dy,
                currentLevel);
    }

    private void chaseSteps(
            Enemy enemy,
            int steps){

        for(int s = 0;
            s < steps;
            s++){

            int dx =
                    Integer.compare(
                            player.getPosition()
                                    .getX(),
                            enemy.getPosition()
                                    .getX());

            int dy =
                    Integer.compare(
                            player.getPosition()
                                    .getY(),
                            enemy.getPosition()
                                    .getY());

            if(dx != 0
                    && dy != 0){

                if(random.nextBoolean()){

                    dx = 0;

                } else {

                    dy = 0;
                }
            }

            boolean moved =
                    movementService.move(
                            enemy.getPosition(),
                            dx,
                            dy,
                            currentLevel);

            if(!moved){

                break;
            }

            if(enemy.getPosition()
                    .equals(
                            player.getPosition())){

                break;
            }
        }
    }

    private static int manhattan(
            Position a,
            Position b){

        return Math.abs(
                a.getX()
                        - b.getX())
                + Math.abs(
                a.getY()
                        - b.getY());
    }

    /**
     * @return true if the level changed or the game ended (caller should stop processing this move).
     */
    private boolean checkExit(){

        if(!player.getPosition()
                .equals(
                        currentLevel.getExit())){

            return false;
        }

        nextLevel();

        return true;
    }

    private void nextLevel(){

        levelIndex++;

        if(levelIndex > 21){

            messageLog.add(
                    "You escaped the Dungeons of Doom! A glorious victory!");

            state =
                    GameState.VICTORY;

            saveScore();

            saveService.clearRunProgress();

            return;
        }

        currentLevelSeed =
                random.nextLong();

        saveService.saveProgress(
                this);

        generateLevel();

        updateDifficulty();
    }

    private void saveScore(){

        saveService.saveScore(
                player.getStatistics(),
                levelIndex,
                player.getTreasure());
    }

    // -----------------------------------------------------------------------
    // Item use
    // -----------------------------------------------------------------------

    public Level getCurrentLevel(){

        return currentLevel;
    }

    public Player getPlayer(){

        return player;
    }

    public GameState getState(){

        return state;
    }

    public void setState(GameState state){

        this.state = state;
    }

    public int getLevelIndex(){

        return levelIndex;
    }

    public long getCurrentLevelSeed(){

        return currentLevelSeed;
    }

    public SaveService getSaveService(){

        return saveService;
    }

    public ItemType getPendingSelection(){

        return pendingSelection;
    }

    public int getDifficultyBias(){

        return difficultyBias;
    }

    public void beginSelection(
            ItemType type){

        if(state != GameState.RUNNING){

            return;
        }

        if(player.getBackpack()
                .getItems(type)
                .isEmpty()){

            pendingSelection = null;

            return;
        }

        pendingSelection = type;
    }

    public void cancelSelection(){

        pendingSelection = null;
    }

    public boolean selectItemSlot(
            int oneBased){

        if(pendingSelection == null){

            return false;
        }

        int idx =
                oneBased - 1;

        if(idx < 0
                || idx > 8){

            cancelSelection();

            return false;
        }

        boolean ok =
                useItemAt(
                        pendingSelection,
                        idx);

        pendingSelection = null;

        return ok;
    }

    private boolean useItemAt(
            ItemType type,
            int index){

        Item item =
                player.getBackpack()
                        .remove(
                                type,
                                index);

        if(item == null){

            return false;
        }

        if(type == ItemType.FOOD
                && item instanceof Food food){

            player.heal(
                    food.getHealValue());

            player.addSatiation(
                    Math.min(
                            player.getMaxSatiation(),
                            food.getHealValue()
                                    + 35));

            player.getStatistics()
                    .addFood();

            messageLog.add(
                    "You eat the food—you feel much better.");

            endEnemyPhaseAfterItemUse();

            return true;
        }

        if(type == ItemType.WEAPON
                && item instanceof Weapon weapon){

            Weapon previous =
                    player.getWeapon();

            player.equipWeapon(
                    weapon);

            if(previous != null){

                dropItemAtAdjacent(
                        previous);
            }

            messageLog.add(
                    "You wield the "
                            + weapon.getName()
                            + ".");

            endEnemyPhaseAfterItemUse();

            return true;
        }

        if(type == ItemType.ELIXIR
                && item instanceof Elixir elixir){

            player.applyElixir(
                    elixir);

            player.getStatistics()
                    .addElixir();

            messageLog.add(
                    "You quaff the elixir.");

            endEnemyPhaseAfterItemUse();

            return true;
        }

        if(type == ItemType.SCROLL
                && item instanceof Scroll scroll){

            player.applyScroll(
                    scroll);

            player.getStatistics()
                    .addScroll();

            messageLog.add(
                    "The scroll vanishes as you read it.");

            endEnemyPhaseAfterItemUse();

            return true;
        }

        if(type == ItemType.TREASURE){

            player.getBackpack()
                    .addItem(item);

            return false;
        }

        return false;
    }

    private void endEnemyPhaseAfterItemUse(){

        if(state != GameState.RUNNING){

            return;
        }

        processEnemyTurns();

        if(state != GameState.RUNNING){

            return;
        }

        player.tickBuffs();

        turnManager.next();

        playerMovesRemaining =
                computeMovesPerTurn();

        applyHungerAndStarvation();
    }

    private void dropItemAtAdjacent(
            Item item){

        int px =
                player.getPosition()
                        .getX();

        int py =
                player.getPosition()
                        .getY();

        int[][] dirs =
                {
                        {0, 1},
                        {0, -1},
                        {1, 0},
                        {-1, 0}
                };

        for(int[] d :
                dirs){

            int nx =
                    px + d[0];

            int ny =
                    py + d[1];

            if(!currentLevel.isWalkable(
                    nx,
                    ny)){

                continue;
            }

            Tile t =
                    currentLevel.getMap()
                            [ny][nx];

            if(t.getItem() != null){

                continue;
            }

            t.setItem(item);

            return;
        }
    }

}
