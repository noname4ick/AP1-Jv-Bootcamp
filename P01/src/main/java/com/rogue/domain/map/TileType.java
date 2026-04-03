package com.rogue.domain.map;

public enum TileType {

    WALL,
    FLOOR,
    CORRIDOR,
    EXIT,

    /** Colored locked door (Task 6). Becomes CORRIDOR once unlocked. */
    DOOR

}
