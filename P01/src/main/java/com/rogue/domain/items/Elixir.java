package com.rogue.domain.items;

public class Elixir extends Item{

    private int agilityBoost;

    private int strengthBoost;

    private int maxHealthBoost;

    public Elixir(){

        super(ItemType.ELIXIR,"Elixir");
    }

    public Elixir(int agi, int str, int hp){

        super(ItemType.ELIXIR,"Elixir");

        agilityBoost = agi;

        strengthBoost = str;

        maxHealthBoost = hp;
    }

    public int getAgilityBoost(){

        return agilityBoost;
    }

    public int getStrengthBoost(){

        return strengthBoost;
    }

    public int getMaxHealthBoost(){

        return maxHealthBoost;
    }

}