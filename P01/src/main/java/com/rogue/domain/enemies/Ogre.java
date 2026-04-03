package com.rogue.domain.enemies;

import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.EnemyType;
import com.rogue.domain.entities.Position;

import java.util.Random;

public class Ogre extends Enemy {

    private final Random random = new Random();
    private boolean resting;

    public Ogre(Position pos){

        super(
                EnemyType.OGRE,
                60,
                60,
                4,
                18,
                3,
                6,
                pos
        );
    }

    public boolean isResting(){

        return resting;
    }

    public void rest(){

        resting = true;
    }

    public void wake(){

        resting = false;
    }


}