package com.rogue.domain.items;

public class Treasure extends Item{

    private int value;

    public Treasure(){

        super(ItemType.TREASURE,"Treasure");
    }

    public Treasure(int value){

        super(ItemType.TREASURE,"Treasure");

        this.value = value;
    }

    public int getValue(){

        return value;
    }

}