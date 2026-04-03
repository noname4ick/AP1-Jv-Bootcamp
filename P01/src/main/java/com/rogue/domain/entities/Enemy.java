package com.rogue.domain.entities;

public abstract class Enemy extends Character {

    protected EnemyType type;

    protected int hostilityRange;

    public Enemy(){}

    public Enemy(
            EnemyType type,
            int health,
            int maxHealth,
            int agility,
            int strength,
            int speed,
            int hostilityRange,
            Position pos){

        super(health,maxHealth,agility,strength,speed,pos);

        this.type = type;

        this.hostilityRange = hostilityRange;
    }

    public EnemyType getType(){

        return type;
    }

    public int getHostilityRange(){

        return hostilityRange;
    }

}