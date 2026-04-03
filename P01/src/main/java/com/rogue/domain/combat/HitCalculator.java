package com.rogue.domain.combat;

import com.rogue.domain.entities.Character;

import java.util.Random;

public class HitCalculator {

    private final Random random;

    public HitCalculator(Random random){

        this.random = random;
    }

    public boolean hit(Character attacker,
                       Character defender){

        double att =
                attacker.getAgility()
                        + attacker.getSpeed();

        double def =
                defender.getAgility()
                        + defender.getSpeed();

        if(att <= 0){

            att = 1;
        }

        if(def <= 0){

            def = 1;
        }

        double chance =
                att / (att + def);

        return random.nextDouble() < chance;
    }

}