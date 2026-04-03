package com.rogue.domain.map;

import com.rogue.domain.entities.Position;

import java.util.List;

public class Corridor {

    private List<Position> cells;

    public Corridor(List<Position> cells){

        this.cells = cells;
    }

    public List<Position> getCells(){

        return cells;
    }

}