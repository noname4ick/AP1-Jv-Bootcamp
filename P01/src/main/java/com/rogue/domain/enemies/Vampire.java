package com.rogue.domain.enemies;

import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.EnemyType;
import com.rogue.domain.entities.Position;

public class Vampire extends Enemy {

    private boolean firstHit = true;

    public Vampire(Position pos){

        super(
                EnemyType.VAMPIRE,
                30,
                30,
                14,
                10,
                10,
                8,
                pos
        );
    }

    public boolean dodgeFirst(){

        if(firstHit){

            firstHit = false;

            return true;
        }

        return false;
    }


}