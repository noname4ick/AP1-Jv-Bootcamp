package com.rogue.domain.services;

import com.rogue.domain.entities.Position;
import com.rogue.domain.map.Level;

public class MovementService {

    public boolean move(Position pos,
                        int dx,
                        int dy,
                        Level level){

        int nx = pos.getX()+dx;

        int ny = pos.getY()+dy;

        if(nx < 0 || ny < 0){

            return false;
        }

        if(ny >= level.getMap().length){

            return false;
        }

        if(nx >= level.getMap()[0].length){

            return false;
        }

        if(!level.isWalkable(nx,ny)){

            return false;
        }

        pos.move(dx,dy);

        return true;
    }

}