package com.rogue.domain.entities;


public enum EnemyType {

    ZOMBIE("zombie"),

    VAMPIRE("vampire"),

    GHOST("ghost"),

    OGRE("ogre"),

    SNAKE_MAGE("snake mage"),

    MIMIC("Mimic");

    private final String label;

    EnemyType(
            String label){

        this.label = label;
    }

    public String getLabel(){

        return label;
    }

}
