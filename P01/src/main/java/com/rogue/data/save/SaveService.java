package com.rogue.data.save;

import com.rogue.data.repository.SaveRepository;
import com.rogue.domain.entities.Player;
import com.rogue.domain.game.GameSession;
import com.rogue.domain.items.*;
import com.rogue.domain.stats.GameStatistics;
import com.rogue.domain.stats.ScoreEntry;

public class SaveService {

    private SaveRepository repository;

    public SaveService(){

        repository =
                new SaveRepository();
    }

    public void saveProgress(
            GameSession session){

        SaveData data =
                repository.load();

        data.level =
                session.getLevelIndex();

        data.playerHP =
                session.getPlayer()
                        .getHealth();

        data.maxHP =
                session.getPlayer()
                        .getMaxHealth();

        data.strength =
                session.getPlayer()
                        .getStrength();

        data.agility =
                session.getPlayer()
                        .getAgility();

        data.speed =
                session.getPlayer()
                        .getSpeed();

        data.satiation =
                session.getPlayer()
                        .getSatiation();

        data.treasure =
                session.getPlayer()
                        .getTreasure();

        data.levelSeed =
                session.getCurrentLevelSeed();

        data.difficultyBias =
                session.getDifficultyBias();

        data.backpack =
                new java.util.ArrayList<>();

        for(ItemType type :
                ItemType.values()){

            for(Item item :
                    session.getPlayer()
                            .getBackpack()
                            .getItems(type)){

                data.backpack.add(
                        toSaved(item));
            }
        }

        if(session.getPlayer()
                .getWeapon() != null){

            data.equippedWeapon =
                    toSaved(
                            session.getPlayer()
                                    .getWeapon());
        }
        else{

            data.equippedWeapon = null;
        }

        repository.save(data);
    }

    public void saveScore(
            GameStatistics stats,
            int level,
            int treasure){

        SaveData data =
                repository.load();

        ScoreEntry entry =
                new ScoreEntry(
                        treasure,
                        level,
                        stats);

        data.scores.add(entry);

        data.scores.sort(
                (a,b) ->
                        Integer.compare(
                                b.getTreasure(),
                                a.getTreasure()));

        repository.save(data);
    }

    public SaveData load(){

        return repository.load();
    }

    public void clearRunProgress(){

        SaveData data =
                repository.load();

        data.level = 0;

        data.playerHP = 0;

        data.maxHP = 0;

        data.strength = 0;

        data.agility = 0;

        data.speed = 0;

        data.satiation = 0;

        data.treasure = 0;

        data.levelSeed = 0L;

        data.backpack =
                new java.util.ArrayList<>();

        data.equippedWeapon = null;

        data.difficultyBias = 0;

        repository.save(data);
    }

    public void applyToPlayer(
            Player player,
            SaveData data){

        player.getBackpack()
                .clearAll();

        for(SavedItem s :
                data.backpack){

            Item item =
                    fromSaved(s);

            if(item != null){

                player.getBackpack()
                        .addItem(item);
            }
        }

        if(data.equippedWeapon != null){

            Item w =
                    fromSaved(
                            data.equippedWeapon);

            if(w instanceof Weapon weapon){

                player.equipWeapon(
                        weapon);
            }
        }

        player.setSatiation(
                data.satiation > 0
                        ? data.satiation
                        : Player.DEFAULT_SATIATION);
    }

    private static SavedItem toSaved(
            Item item){

        SavedItem s =
                new SavedItem();

        s.type = item.getType();

        s.name = item.getName();

        if(item instanceof Food f){

            s.foodHeal =
                    f.getHealValue();
        }

        if(item instanceof Weapon w){

            s.weaponStrength =
                    w.getStrengthBonus();
        }

        if(item instanceof Scroll sc){

            s.scrollStr =
                    sc.getStrengthBoost();

            s.scrollAgi =
                    sc.getAgilityBoost();

            s.scrollHp =
                    sc.getMaxHealthBoost();
        }

        if(item instanceof Elixir e){

            s.elixirAgi =
                    e.getAgilityBoost();

            s.elixirStr =
                    e.getStrengthBoost();

            s.elixirHp =
                    e.getMaxHealthBoost();
        }

        if(item instanceof Treasure t){

            s.treasureValue =
                    t.getValue();
        }

        return s;
    }

    private static Item fromSaved(
            SavedItem s){

        if(s == null
                || s.type == null){

            return null;
        }

        return switch(s.type){

            case FOOD ->
                    new Food(
                            s.foodHeal);

            case WEAPON ->
                    new Weapon(
                            s.name != null
                                    ? s.name
                                    : "Weapon",
                            s.weaponStrength);

            case SCROLL ->
                    new Scroll(
                            s.scrollStr,
                            s.scrollAgi,
                            s.scrollHp);

            case ELIXIR ->
                    new Elixir(
                            s.elixirAgi,
                            s.elixirStr,
                            s.elixirHp);

            case TREASURE ->
                    new Treasure(
                            s.treasureValue);

            case KEY -> null;
        };
    }

}
