package com.rogue.domain.enemies;

import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.EnemyType;
import com.rogue.domain.entities.Position;

public class Zombie extends Enemy {

    private int turnCounter = 0;

    public Zombie(Position pos){

        super(
                EnemyType.ZOMBIE,
                40,
                40,
                5,
                8,
                4,
                6,
                pos
        );
    }

}