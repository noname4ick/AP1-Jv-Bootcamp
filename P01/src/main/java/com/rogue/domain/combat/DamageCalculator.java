package com.rogue.domain.combat;

import com.rogue.domain.entities.Character;
import com.rogue.domain.entities.Player;
import com.rogue.domain.items.Weapon;

public class DamageCalculator {

    public int calculate(Character attacker){

        int base = attacker.getStrength();

        if(attacker instanceof Player player){

            Weapon weapon = player.getWeapon();

            if(weapon != null){

                base += weapon.getStrengthBonus();
            }
        }

        if(base < 1){

            base = 1;
        }

        return base;
    }

}