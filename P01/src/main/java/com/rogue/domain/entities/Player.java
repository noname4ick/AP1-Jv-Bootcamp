package com.rogue.domain.entities;

import com.rogue.domain.inventory.Backpack;
import com.rogue.domain.items.Weapon;
import com.rogue.domain.map.KeyColor;
import com.rogue.domain.stats.GameStatistics;

import java.util.EnumSet;
import java.util.Set;

public class Player extends Character{

    public static final int DEFAULT_SATIATION =
            100;
    public static int progress = 5;

    private Backpack backpack;

    private int treasure;

    private Weapon weapon;

    private GameStatistics statistics;


    private int satiation =
            DEFAULT_SATIATION;

    private static final int MAX_SATIATION =
            100;

    private int sleepTurns;

    private final Set<KeyColor> heldKeys =
            EnumSet.noneOf(KeyColor.class);

    public Player(){

        super(100, 100, 10, 10, 10, new Position(0, 0));

        backpack = new Backpack();

        statistics = new GameStatistics();
    }

    public Player(Position pos){

        super(100, 100, 10, 10, 10, pos);

        backpack = new Backpack();

        statistics = new GameStatistics();
    }

    public int getSleepTurns(){

        return sleepTurns;
    }

    public void setSleepTurns(int sleepTurns){

        this.sleepTurns = sleepTurns;
    }

    public boolean isAsleep(){

        return sleepTurns > 0;
    }

    private int tempAgilityBonus;

    private int tempStrengthBonus;

    private int tempMaxHpBonus;

    private int buffTurnsRemaining;

    public void setTreasure(int treasure){

        this.treasure = treasure;
    }

    public void tickBuffs(){

        if(buffTurnsRemaining <= 0){

            return;
        }

        buffTurnsRemaining--;

        if(buffTurnsRemaining > 0){

            return;
        }

        agility -= tempAgilityBonus;

        strength -= tempStrengthBonus;

        maxHealth -= tempMaxHpBonus;

        tempAgilityBonus = 0;

        tempStrengthBonus = 0;

        tempMaxHpBonus = 0;

        if(health > maxHealth){

            health = maxHealth;
        }

        if(agility < 1){

            agility = 1;
        }

        if(strength < 1){

            strength = 1;
        }
    }

    public void applyElixir(
            com.rogue.domain.items.Elixir e){

        buffTurnsRemaining = 5;

        if(e.getAgilityBoost() > 0){

            tempAgilityBonus +=
                    e.getAgilityBoost();

            agility +=
                    e.getAgilityBoost();
        }

        if(e.getStrengthBoost() > 0){

            tempStrengthBonus +=
                    e.getStrengthBoost();

            strength +=
                    e.getStrengthBoost();
        }

        if(e.getMaxHealthBoost() > 0){

            tempMaxHpBonus +=
                    e.getMaxHealthBoost();

            maxHealth +=
                    e.getMaxHealthBoost();
        }
    }

    public void applyScroll(
            com.rogue.domain.items.Scroll s){

        strength +=
                s.getStrengthBoost();

        agility +=
                s.getAgilityBoost();

        maxHealth +=
                s.getMaxHealthBoost();

        if(health > maxHealth){

            health = maxHealth;
        }
    }

    public Backpack getBackpack(){

        return backpack;
    }

    public void addTreasure(int value){

        treasure += value;

        statistics.addTreasure(value);
    }

    public int getTreasure(){

        return treasure;
    }

    public Weapon getWeapon(){

        return weapon;
    }

    public void equipWeapon(Weapon w){

        weapon = w;
    }

    public GameStatistics getStatistics(){

        return statistics;
    }

    public int getSatiation(){

        return satiation;
    }

    public void setSatiation(
            int satiation){

        this.satiation =
                Math.max(0,
                        Math.min(MAX_SATIATION, satiation));
    }

    public void tickSatiation(){
        if(satiation > 0){
            if(progress == 0){
                satiation--;
                progress = 5;
            }
            progress--;

        }
    }

    public void addSatiation(
            int amount){

        setSatiation(
                satiation + amount);
    }

    public int getMaxSatiation(){

        return MAX_SATIATION;
    }


    public void addKey(KeyColor color){

        heldKeys.add(color);
    }

    public boolean hasKey(KeyColor color){

        return heldKeys.contains(color);
    }

    public void useKey(KeyColor color){

        heldKeys.remove(color);
    }

    public Set<KeyColor> getHeldKeys(){

        return heldKeys;
    }

    public void clearKeys(){

        heldKeys.clear();
    }

}
