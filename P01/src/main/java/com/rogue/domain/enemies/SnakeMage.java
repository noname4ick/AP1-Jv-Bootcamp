package com.rogue.domain.enemies;

import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.EnemyType;
import com.rogue.domain.entities.Position;

import java.util.Random;

public class SnakeMage extends Enemy{

    private Random random;

    public SnakeMage(Position pos,
                     Random random){

        super(
                EnemyType.SNAKE_MAGE,
                25,
                25,
                16,
                9,
                14,
                9,
                pos
        );

        this.random = random;
    }


}