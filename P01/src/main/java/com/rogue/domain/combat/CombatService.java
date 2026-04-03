package com.rogue.domain.combat;

import com.rogue.domain.enemies.Ogre;
import com.rogue.domain.enemies.Vampire;
import com.rogue.domain.entities.Character;
import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.EnemyType;
import com.rogue.domain.entities.Player;
import com.rogue.domain.game.MessageLog;

import java.util.Random;

public class CombatService {

    private HitCalculator hitCalc;

    private DamageCalculator damageCalc;

    private Random random;

    private MessageLog messages;

    public CombatService(
            Random random,
            MessageLog messages){

        this.random = random;

        this.messages = messages;

        hitCalc =
                new HitCalculator(random);

        damageCalc =
                new DamageCalculator();
    }

    public void attack(
            Character attacker,
            Character defender){

        if(attacker instanceof Player player
                && defender instanceof Vampire vampire){

            if(vampire.dodgeFirst()){

                player.getStatistics()
                        .addMiss();

                messages.add(
                        "You miss the vampire — it shimmers away!");

                return;
            }
        }

        boolean hit =
                hitCalc.hit(
                        attacker,
                        defender);

        if(attacker instanceof Player player){

            if(hit){

                player.getStatistics()
                        .addHit();
            }
            else{

                player.getStatistics()
                        .addMiss();
            }
        }

        if(!hit){

            if(attacker instanceof Player
                    && defender instanceof Enemy e){

                messages.add(
                        "You miss the "
                                + e.getType()
                                        .getLabel());
            }

            if(attacker instanceof Enemy e
                    && defender instanceof Player){

                messages.add(
                        "The "
                                + e.getType()
                                        .getLabel()
                                + " misses");
            }

            return;
        }

        int damage =
                damageCalc.calculate(
                        attacker);

        defender.takeDamage(
                damage);

        if(attacker instanceof Player
                && defender instanceof Enemy e){

            if(!e.isAlive()){

                messages.add(
                        "You defeated the "
                                + e.getType()
                                        .getLabel()
                                + "!");
            }
            else{

                messages.add(
                        "You hit the "
                                + e.getType()
                                        .getLabel());
            }
        }

        if(attacker instanceof Enemy e
                && defender instanceof Player p){

            if(e instanceof Vampire){

                p.reduceMaxHealth(
                        3 + random.nextInt(4));

                messages.add(
                        "The vampire hit you — you feel weaker!");
            }
            else if(e.getType()
                    == EnemyType.SNAKE_MAGE){

                boolean sleep =
                        random.nextDouble()
                                < 0.35;

                if(sleep){

                    p.setSleepTurns(1);
                }

                messages.add(
                        sleep
                                ? "The snake mage hit you—you fall asleep!"
                                : "The snake mage hit you");
            }
            else{

                messages.add(
                        "The "
                                + e.getType()
                                        .getLabel()
                                + " hit you");
            }

            if(e instanceof Ogre ogre){

                ogre.rest();
            }
        }
    }

}
