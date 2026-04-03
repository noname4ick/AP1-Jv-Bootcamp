package com.rogue.domain.map;

import com.rogue.domain.items.Item;

public class Tile {

    private TileType type;

    private boolean visible;

    private boolean explored;

    private Item item;

    private KeyColor doorColor;

    public Tile(TileType type){

        this.type = type;
    }

    public TileType getType(){

        return type;
    }

    public void setType(TileType type){

        this.type = type;
    }

    public boolean isVisible(){

        return visible;
    }

    public void setVisible(boolean visible){

        this.visible = visible;
    }

    public boolean isExplored(){

        return explored;
    }

    public void setExplored(boolean explored){

        this.explored = explored;
    }

    public Item getItem(){

        return item;
    }

    public void setItem(Item item){

        this.item = item;
    }

    public KeyColor getDoorColor(){

        return doorColor;
    }

    public void setDoorColor(KeyColor doorColor){

        this.doorColor = doorColor;
    }

    public char symbol(){

        return switch(type){

            case WALL -> '░';

            case FLOOR, CORRIDOR -> '.';

            case EXIT -> '%';

            case DOOR -> '/';

            default -> ' ';
        };
    }

}
