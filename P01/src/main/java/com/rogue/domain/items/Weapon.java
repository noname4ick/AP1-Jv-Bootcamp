package com.rogue.domain.items;

public class Weapon extends Item{

    private int strengthBonus;

    public Weapon(){

        super(ItemType.WEAPON,"Weapon");
    }

    public Weapon(String name,int strength){

        super(ItemType.WEAPON,name);

        this.strengthBonus = strength;
    }

    public int getStrengthBonus(){

        return strengthBonus;
    }

}