package com.rogue.domain.items;

import com.rogue.domain.map.KeyColor;

/**
 * A colored key that unlocks the matching colored door on this level.
 * Keys are NOT stored in the backpack; they are auto-collected into the
 * player's key ring and consumed when the matching door is opened.
 */
public class Key extends Item {

    private final KeyColor keyColor;

    public Key(KeyColor color) {

        super(
                ItemType.KEY,
                color.name().toLowerCase()
                        + " key");

        this.keyColor = color;
    }

    public KeyColor getKeyColor() {

        return keyColor;
    }

}
