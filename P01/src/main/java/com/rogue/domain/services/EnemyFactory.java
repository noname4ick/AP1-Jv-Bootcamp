package com.rogue.domain.services;

import com.rogue.domain.enemies.*;
import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.Position;

import java.util.Random;

public class EnemyFactory {

    private final Random random;

    public EnemyFactory(Random random){

        this.random = random;
    }

    public Enemy create(Position pos){

        int r = random.nextInt(6);

        return switch(r){

            case 0 -> new Zombie(pos);

            case 1 -> new Vampire(pos);

            case 2 -> new Ghost(pos,random);

            case 3 -> new Ogre(pos);

            case 4 -> new Mimik(pos);

            default -> new SnakeMage(pos,random);

        };
    }

}