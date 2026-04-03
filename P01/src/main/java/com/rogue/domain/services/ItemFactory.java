package com.rogue.domain.services;

import com.rogue.domain.items.*;

import java.util.Random;

public class ItemFactory {

    private Random random;


    public ItemFactory(Random random){

        this.random = random;
    }

    public Item randomItem(
            Random rng,
            int levelIndex){

        int bias =
                Math.min(
                        8,
                        levelIndex);

        int r =
                rng.nextInt(
                        12 + bias);

        if(r < 4){

            return new Food(
                    8 + rng.nextInt(
                            12 + levelIndex));
        }

        if(r < 6){

            return new Weapon(
                    "Sword",
                    2 + rng.nextInt(
                            4 + levelIndex / 3));
        }

        if(r < 8){

            return new Scroll(
                    1 + rng.nextInt(2),
                    1 + rng.nextInt(2),
                    2 + rng.nextInt(4));
        }

        if(r < 10){

            return new Elixir(
                    2 + rng.nextInt(4),
                    rng.nextInt(3),
                    rng.nextInt(3));
        }

        return new Treasure(
                5 + rng.nextInt(
                        30 + levelIndex * 3));
    }

    public Item treasureItem(
            Random rng,
            int levelIndex){

        return new Treasure(
                15 + rng.nextInt(
                        40 + levelIndex * 5));
    }

}
