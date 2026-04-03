package com.rogue.domain.items;

public abstract class Item {

    protected ItemType type;

    protected String name;

    public Item(){}

    public Item(ItemType type,String name){

        this.type = type;
        this.name = name;
    }

    public ItemType getType(){

        return type;
    }

    public String getName(){

        return name;
    }

}