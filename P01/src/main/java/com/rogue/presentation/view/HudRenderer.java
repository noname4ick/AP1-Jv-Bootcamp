package com.rogue.presentation.view;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.Screen;
import com.rogue.data.save.SaveData;
import com.rogue.data.save.SaveService;
import com.rogue.domain.entities.Player;
import com.rogue.domain.game.GameSession;
import com.rogue.domain.game.GameState;
import com.rogue.domain.items.Item;
import com.rogue.domain.items.ItemType;
import com.rogue.domain.map.KeyColor;
import com.rogue.domain.stats.ScoreEntry;

import java.util.List;
import java.util.Set;

public class HudRenderer {

    private Screen screen;

    public HudRenderer(Screen screen){

        this.screen =
                screen;
    }

    public void draw(
            GameSession session,
            boolean showStats){

        if(showStats){

            drawStats(
                    session.getSaveService());

            return;
        }

        Player p =
                session.getPlayer();

        int moves =
                session.getMovesRemainingPreview();

        drawLine(
                ScreenLayout.HUD_ROW_MESSAGE,
                clip(
                        session.getMessageLine()));

        ItemType pending =
                session.getPendingSelection();

        if(pending != null){

            drawBackpackList(
                    p,
                    pending);
        }

        drawLine(
                ScreenLayout.HUD_ROW_HINT,
                clip(
                        "WASD/arrows move | h wpn j food k elixir e scr | 1-9 use | t scores | ESC menu"));

        String status =
                rogueStatusLine(
                        session,
                        p,
                        moves);

        drawLine(
                ScreenLayout.HUD_ROW_STATUS,
                clip(
                        status));
    }

    private static String rogueStatusLine(
            GameSession session,
            Player p,
            int movesLeft){

        StringBuilder sb =
                new StringBuilder();

        sb.append(
                "Level:")
                .append(
                        session.getLevelIndex())
                .append(
                        "/21  ");

        sb.append(
                "Gold:")
                .append(
                        p.getTreasure())
                .append(
                        "  ");

        sb.append(
                "Hp:")
                .append(
                        p.getHealth())
                .append(
                        "(")
                .append(
                        p.getMaxHealth())
                .append(
                        ")  ");

        sb.append(
                "Str:")
                .append(
                        p.getStrength())
                .append(
                        "  ");

        sb.append(
                "Hung:")
                .append(
                        p.getSatiation())
                .append(
                        "%");

        if(movesLeft > 1){

            sb.append(
                            "  Mv:")
                    .append(
                            movesLeft);
        }

        // Show held keys (Task 6)
        Set<KeyColor> keys =
                p.getHeldKeys();

        if(!keys.isEmpty()){

            sb.append("  Keys:");

            for(KeyColor kc : keys){

                sb.append(kc.name().charAt(0));
            }
        }

        if(session.getState()
                == GameState.GAME_OVER){

            sb.append(
                    "  --GAME OVER-- (Enter/ESC to menu)");
        }

        if(session.getState()
                == GameState.VICTORY){

            sb.append(
                    "  **VICTORY** (Enter/ESC to menu)");
        }

        return sb.toString();
    }

    private void drawBackpackList(
            Player p,
            ItemType type){

        List<Item> list =
                p.getBackpack()
                        .getItems(type);

        StringBuilder sb =
                new StringBuilder();

        sb.append(
                "Pick ")
                .append(
                        type)
                .append(
                        ": ");

        for(int i = 0;
            i < list.size();
            i++){

            sb.append(
                    (i + 1))
                    .append(
                            ")")
                    .append(
                            list.get(i)
                                    .getName())
                    .append(
                            " ");
        }

        drawLine(
                ScreenLayout.HUD_ROW_PICK,
                clip(
                        sb.toString()));
    }

    private void drawStats(
            SaveService saveService){

        SaveData data =
                saveService.load();

        screen.clear();

        int row = 0;

        drawLine(
                row++,
                clip(
                        "=== TOP SCORES (by gold) ==="));

        drawLine(
                row++,
                clip(
                        "Gold Lvl K F E S Hit Miss Moves"));

        int max =
                Math.min(
                        14,
                        data.scores.size());

        for(int i = 0;
            i < max;
            i++){

            ScoreEntry e =
                    data.scores.get(i);

            String line =
                    e.getTreasure()
                            + " "
                            + e.getLevel()
                            + " "
                            + e.getEnemiesKilled()
                            + " "
                            + e.getFood()
                            + " "
                            + e.getElixirs()
                            + " "
                            + e.getScrolls()
                            + " "
                            + e.getHits()
                            + " "
                            + e.getMisses()
                            + " "
                            + e.getMoves();

            drawLine(
                    row++,
                    clip(line));
        }

        drawLine(
                ScreenLayout.HUD_ROW_HINT,
                clip(
                        "t close"));
    }

    private static String clip(
            String s){

        int max =
                ScreenLayout.TERMINAL_COLS;

        if(s.length() <= max){

            return s;
        }

        return s.substring(
                0,
                max);
    }

    private void drawLine(
            int y,
            String text){

        for(int i = 0;
            i < text.length();
            i++){

            screen.setCharacter(
                    i,
                    y,
                    new TextCharacter(
                            text.charAt(i),
                            TextColor.ANSI.WHITE,
                            TextColor.ANSI.BLACK));
        }

        for(int i = text.length();
            i < ScreenLayout.TERMINAL_COLS;
            i++){

            screen.setCharacter(
                    i,
                    y,
                    new TextCharacter(
                            ' ',
                            TextColor.ANSI.WHITE,
                            TextColor.ANSI.BLACK));
        }
    }

}
