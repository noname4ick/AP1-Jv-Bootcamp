package com.rogue.domain.items;

public class Scroll extends Item{

    private int strengthBoost;

    private int agilityBoost;

    private int maxHealthBoost;

    public Scroll(){

        super(ItemType.SCROLL,"Scroll");
    }

    public Scroll(int str,int agi,int hp){

        super(ItemType.SCROLL,"Scroll");

        strengthBoost = str;

        agilityBoost = agi;

        maxHealthBoost = hp;
    }

    public int getStrengthBoost(){

        return strengthBoost;
    }

    public int getAgilityBoost(){

        return agilityBoost;
    }

    public int getMaxHealthBoost(){

        return maxHealthBoost;
    }

}