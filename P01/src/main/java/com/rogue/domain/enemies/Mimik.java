package com.rogue.domain.enemies;

import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.EnemyType;
import com.rogue.domain.entities.Position;

import java.util.Random;

public class Mimik extends Enemy {

    private final char disguiseChar;

    private static final char[] ITEM_CHARS =
            { '!', '&', '=', 'p', 'e' };

    public Mimik(Position pos){

        super(
                EnemyType.MIMIC,
                30,
                30,
                8,
                4,
                6,
                4,
                pos
        );

        this.disguiseChar =
                ITEM_CHARS[new Random().nextInt(ITEM_CHARS.length)];
    }

    public char getDisguiseChar(){

        return disguiseChar;
    }

}
