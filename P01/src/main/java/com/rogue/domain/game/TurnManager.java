package com.rogue.domain.game;

public class TurnManager {

    private int turn;

    public void next(){

        turn++;
    }

    public int getTurn(){

        return turn;
    }

}