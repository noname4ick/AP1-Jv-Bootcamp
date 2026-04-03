package com.rogue.domain.map;

import com.rogue.domain.entities.Position;

public class Room {

    private int x;

    private int y;

    private int width;

    private int height;

    public Room(){}

    public Room(int x,int y,int w,int h){

        this.x = x;

        this.y = y;

        width = w;

        height = h;
    }

    public boolean contains(Position p){

        return p.getX() >= x &&
                p.getX() < x + width &&
                p.getY() >= y &&
                p.getY() < y + height;
    }

    public Position center(){

        return new Position(
                x + width/2,
                y + height/2
        );
    }

    public int getX(){ return x; }

    public int getY(){ return y; }

    public int getWidth(){ return width; }

    public int getHeight(){ return height; }

}