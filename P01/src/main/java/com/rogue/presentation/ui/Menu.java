package com.rogue.presentation.ui;

public class Menu {

    public static final int IDX_NEW_GAME = 0;

    public static final int IDX_CONTINUE = 1;

    public static final int IDX_EXIT = 2;

    private final String[] options = {

            "New Game",
            "Continue",
            "Exit"
    };

    private boolean hasSave;

    private int selected = 0;

    public Menu(boolean hasSave){

        this.hasSave = hasSave;
    }

    public void setHasSave(boolean hasSave){

        this.hasSave = hasSave;
    }

    public boolean isHasSave(){

        return hasSave;
    }

    public void moveUp(){

        do {

            selected--;

            if(selected < 0){

                selected = options.length - 1;
            }

        } while(!isSelectable(selected));
    }

    public void moveDown(){

        do {

            selected++;

            if(selected >= options.length){

                selected = 0;
            }

        } while(!isSelectable(selected));
    }

    private boolean isSelectable(int idx){

        if(idx == IDX_CONTINUE){

            return hasSave;
        }

        return true;
    }

    public int getSelected(){

        return selected;
    }

    public String[] getOptions(){

        return options;
    }

}
