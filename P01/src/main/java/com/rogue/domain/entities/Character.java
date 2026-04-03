package com.rogue.domain.entities;

public abstract class Character {

    protected int health;

    protected int maxHealth;

    protected int agility;

    protected int strength;

    protected int speed;

    protected Position position;

    protected boolean alive = true;

    public Character(){}

    public Character(
            int health,
            int maxHealth,
            int agility,
            int strength,
            int speed,
            Position position){

        this.health = health;
        this.maxHealth = maxHealth;
        this.agility = agility;
        this.strength = strength;
        this.speed = speed;
        this.position = position;
    }

    public void takeDamage(int damage){

        health -= damage;

        if(health <= 0){

            health = 0;

            alive = false;
        }
    }

    public void heal(int value){

        health += value;

        if(health > maxHealth){

            health = maxHealth;
        }
    }

    public boolean isAlive(){

        return alive;
    }

    public Position getPosition(){

        return position;
    }

    public int getHealth(){

        return health;
    }

    public int getMaxHealth(){

        return maxHealth;
    }

    public int getAgility(){

        return agility;
    }

    public int getStrength(){

        return strength;
    }

    public int getSpeed(){

        return speed;
    }

    public void setAgility(int agility){

        this.agility = agility;
    }

    public void setStrength(int strength){

        this.strength = strength;
    }

    public void setSpeed(int speed){

        this.speed = speed;
    }

    public void reduceMaxHealth(int amount){

        if(amount <= 0){

            return;
        }

        maxHealth -= amount;

        if(maxHealth < 1){

            maxHealth = 1;
        }

        if(health > maxHealth){

            health = maxHealth;
        }
    }

    public void increaseMaxHealth(int amount){

        maxHealth += amount;
    }

    public void setHealth(int health){

        this.health = health;
    }

    public void restoreStats(
            int hp,
            int maxHp,
            int str,
            int agi,
            int spd){

        health = hp;

        maxHealth = maxHp;

        strength = str;

        agility = agi;

        speed = spd;

        alive = hp > 0;
    }

}