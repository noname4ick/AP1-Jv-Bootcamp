package com.rogue.presentation.view;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.rogue.domain.enemies.Ghost;
import com.rogue.domain.enemies.Mimik;
import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.Player;
import com.rogue.domain.entities.Position;
import com.rogue.domain.game.GameSession;
import com.rogue.domain.items.Item;
import com.rogue.domain.items.ItemType;
import com.rogue.domain.map.Level;
import com.rogue.domain.map.Room;
import com.rogue.domain.map.Tile;
import com.rogue.domain.map.TileType;
import com.rogue.presentation.ui.Menu;
import com.rogue.presentation.ui.MenuRenderer;

public class Renderer {

    private Screen screen;

    private FogOfWar fog;

    private HudRenderer hud;

    private int lastMapRevision =
            -1;

    public Renderer(Screen screen){

        this.screen = screen;

        fog =
                new FogOfWar();

        hud =
                new HudRenderer(screen);
    }

    public void draw(
            GameSession session){

        draw(
                session,
                false);
    }

    public void draw(
            GameSession session,
            boolean showStats){

        int rev =
                session.getMapRevision();

        if(rev != lastMapRevision){

            screen.clear();

            lastMapRevision = rev;
        }

        if(showStats){

            screen.clear();

            hud.draw(
                    session,
                    true);

            return;
        }

        Level level =
                session.getCurrentLevel();

        Player player =
                session.getPlayer();

        Tile[][] map =
                level.getMap();

        boolean[][] vis =
                fog.computeVisible(
                        level,
                        player);

        fog.reveal(
                level,
                vis);

        for(int y = 0;
            y < map.length;
            y++){

            for(int x = 0;
                x < map[0].length;
                x++){

                Tile tile =
                        map[y][x];

                if(!tile.isExplored()){

                    write(
                            x,
                            y,
                            ' ',
                            TextColor.ANSI.BLACK,
                            TextColor.ANSI.BLACK);

                    continue;
                }

                Position here =
                        new Position(
                                x,
                                y);

                Room hereRoom =
                        level.findRoomContaining(
                                here);

                if(hereRoom != null
                        && !hereRoom.contains(
                        player.getPosition())){

                    write(
                            x,
                            y,
                            '#',
                            TextColor.ANSI.BLACK_BRIGHT,
                            TextColor.ANSI.BLACK);

                    continue;
                }

                if(hereRoom == null
                        && !vis[y][x]){

                    // Doors in explored corridors remain visible even out of LOS
                    if(tile.getType() == TileType.DOOR){

                        TextColor doorColor =
                                tile.getDoorColor() != null
                                        ? switch(tile.getDoorColor()){
                                            case RED -> TextColor.ANSI.RED;
                                            case BLUE -> TextColor.ANSI.BLUE_BRIGHT;
                                            case GREEN -> TextColor.ANSI.GREEN;
                                        }
                                        : TextColor.ANSI.WHITE;

                        write(x, y, '/', doorColor, TextColor.ANSI.BLACK);

                    } else {

                        write(x, y, ' ',
                                TextColor.ANSI.BLACK,
                                TextColor.ANSI.BLACK);
                    }

                    continue;
                }

                char c =
                        tile.symbol();

                TextColor tileColor;

                if(tile.getType() == TileType.DOOR){

                    // Door color: red/blue/green based on key color
                    tileColor =
                            tile.getDoorColor() != null
                                    ? switch(tile.getDoorColor()){
                                        case RED -> TextColor.ANSI.RED_BRIGHT;
                                        case BLUE -> TextColor.ANSI.BLUE_BRIGHT;
                                        case GREEN -> TextColor.ANSI.GREEN;
                                    }
                                    : TextColor.ANSI.WHITE;

                } else {

                    tileColor = TextColor.ANSI.MAGENTA;
                }

                write(
                        x,
                        y,
                        c,
                        tileColor,
                        TextColor.ANSI.BLACK);

                Item ground =
                        tile.getItem();

                if(ground != null){
                    TextColor color =
                            switch(ground.getType()){

                                case ItemType.FOOD ->
                                        TextColor.ANSI.GREEN;

                                case ItemType.WEAPON ->
                                        TextColor.ANSI.RED;

                                case ItemType.ELIXIR,ItemType.SCROLL ->
                                        TextColor.ANSI.CYAN_BRIGHT;

                                case ItemType.TREASURE ->
                                        TextColor.ANSI.YELLOW_BRIGHT;

                                case ItemType.KEY ->
                                        switch(((com.rogue.domain.items.Key) ground).getKeyColor()){
                                            case RED -> TextColor.ANSI.RED_BRIGHT;
                                            case BLUE -> TextColor.ANSI.BLUE_BRIGHT;
                                            case GREEN -> TextColor.ANSI.GREEN_BRIGHT;
                                        };

                            };

                    char sym =
                            switch(ground.getType()){

                                case ItemType.FOOD -> 'p';
                                case ItemType.WEAPON -> '!';
                                case ItemType.ELIXIR -> 'e';
                                case ItemType.SCROLL -> '=';
                                case ItemType.TREASURE -> '&';
                                case ItemType.KEY -> 'K';
                            };


                    write(
                            x,
                            y,
                            sym,
                            color,
                            TextColor.ANSI.BLACK);
                }
            }
        }

        drawEnemies(
                level,
                player,
                vis);

        drawPlayer(player);

        clearHudRows();

        hud.draw(
                session,
                false);
    }

    /** Clear HUD band so stale Rogue-style lines do not linger. */
    private void clearHudRows(){

        String blank =
                " "
                        .repeat(
                                ScreenLayout.TERMINAL_COLS);

        for(int y = ScreenLayout.HUD_ROW_MESSAGE;
            y <= ScreenLayout.HUD_ROW_STATUS;
            y++){

            drawTextRaw(
                    0,
                    y,
                    blank);
        }
    }

    private void drawTextRaw(
            int x,
            int y,
            String text){

        for(int i = 0;
            i < text.length()
                    && x + i < ScreenLayout.TERMINAL_COLS;
            i++){

            screen.setCharacter(
                    x + i,
                    y,
                    new TextCharacter(
                            text.charAt(i),
                            TextColor.ANSI.BLUE,
                            TextColor.ANSI.BLACK));
        }
    }

    private void write(
            int x,
            int y,
            char c,
            TextColor fg,
            TextColor bg){

        screen.setCharacter(x,
                y,
                new TextCharacter(
                        c,
                        fg,
                        bg));
    }

    private void drawPlayer(
            Player player){

        write(
                player.getPosition().getX(),
                player.getPosition().getY(),
                '@',
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.BLACK);
    }

    private void drawEnemies(
            Level level,
            Player player,
            boolean[][] vis){

        for(Enemy enemy :
                level.getEnemies()){

            if(!enemy.isAlive()){

                continue;
            }

            if(enemy instanceof Ghost g
                    && !g.isVisible()){

                continue;
            }

            int ex =
                    enemy.getPosition()
                            .getX();

            int ey =
                    enemy.getPosition()
                            .getY();

            Tile t =
                    level.getMap()[ey][ex];

            if(!t.isExplored()){

                continue;
            }

            Room er =
                    level.findRoomContaining(
                            enemy.getPosition());

            if(er != null
                    && !er.contains(
                    player.getPosition())){

                continue;
            }

            if(er == null
                    && !vis[ey][ex]){

                continue;
            }

            TextColor color =
                    switch(enemy.getType()){

                        case ZOMBIE ->
                                TextColor.ANSI.GREEN;

                        case VAMPIRE ->
                                TextColor.ANSI.RED;

                        case GHOST, SNAKE_MAGE, MIMIC ->
                                TextColor.ANSI.WHITE;

                        case OGRE ->
                                TextColor.ANSI.YELLOW;

                    };


            char sym =
                    switch(enemy.getType()){

                        case ZOMBIE -> 'z';

                        case VAMPIRE -> 'v';

                        case GHOST -> 'g';

                        case OGRE -> 'O';

                        case SNAKE_MAGE -> 's';

                        case MIMIC ->
                                (enemy instanceof Mimik m)
                                        ? m.getDisguiseChar()
                                        : '?';
                    };

            write(
                    ex,
                    ey,
                    sym,
                    color,
                    TextColor.ANSI.BLACK);
        }
    }
    public void drawMenu(Menu menu){

        TextGraphics g =
                screen.newTextGraphics();

        MenuRenderer menuRenderer =
                new MenuRenderer();

        menuRenderer.draw(g, menu);
    }

}
