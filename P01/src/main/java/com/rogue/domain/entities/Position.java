package com.rogue.domain.entities;

import java.util.Objects;

public class Position {

    private int x;
    private int y;

    public Position(){}

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public Position copy(){
        return new Position(x,y);
    }

    public void move(int dx,int dy){
        x += dx;
        y += dy;
    }

    @Override
    public boolean equals(Object o){

        if(this == o) return true;

        if(!(o instanceof Position)) return false;

        Position p = (Position)o;

        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }
}