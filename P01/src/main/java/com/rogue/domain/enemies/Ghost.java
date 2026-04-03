package com.rogue.domain.enemies;

import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.EnemyType;
import com.rogue.domain.entities.Position;

import java.util.Random;

public class Ghost extends Enemy {

    private final Random random;

    private boolean visible = true;

    public Ghost(Position pos,
                 Random random){

        super(
                EnemyType.GHOST,
                20,
                20,
                15,
                5,
                12,
                4,
                pos
        );

        this.random = random;
    }

    public void teleport(){

        position.setX(
                position.getX()
                        + random.nextInt(3)-1);

        position.setY(
                position.getY()
                        + random.nextInt(3)-1);
    }

    public void toggle(){

        visible = !visible;
    }

    public boolean isVisible(){

        return visible;
    }


}