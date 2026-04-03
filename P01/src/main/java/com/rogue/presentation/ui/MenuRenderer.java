package com.rogue.presentation.ui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class MenuRenderer {

    private static final String TITLE =
            "D U N G E O N S   O F   D O O M";

    private static final String SUBTITLE =
            "A Roguelike Adventure";

    public void draw(
            TextGraphics graphics,
            Menu menu){

        graphics.setForegroundColor(
                TextColor.ANSI.YELLOW_BRIGHT);

        graphics.putString(
                24, 6, TITLE);

        graphics.setForegroundColor(
                TextColor.ANSI.WHITE);

        graphics.putString(
                29, 7, SUBTITLE);

        // Options
        String[] options =
                menu.getOptions();

        int startY = 10;

        for(int i = 0; i < options.length; i++){

            boolean isContinue =
                    i == Menu.IDX_CONTINUE;

            boolean disabled =
                    isContinue && !menu.isHasSave();

            boolean isSelected =
                    i == menu.getSelected();

            if(isSelected){

                graphics.setForegroundColor(
                        TextColor.ANSI.YELLOW);

                graphics.putString(
                        30,
                        startY + i,
                        "> " + options[i]);

            } else if(disabled){

                graphics.setForegroundColor(
                        TextColor.ANSI.BLACK_BRIGHT);

                graphics.putString(
                        30,
                        startY + i,
                        "  " + options[i] + "  [no save]");

            } else {

                graphics.setForegroundColor(
                        TextColor.ANSI.WHITE);

                graphics.putString(
                        30,
                        startY + i,
                        "  " + options[i]);
            }
        }

        // Controls hint
        graphics.setForegroundColor(
                TextColor.ANSI.BLACK_BRIGHT);

        graphics.putString(
                25, 15,
                "Arrow keys / Enter to select");
    }

}
