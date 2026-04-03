package com.rogue.domain.stats;

public class GameStatistics {

    private int treasure;

    private int enemiesKilled;

    private int foodConsumed;

    private int elixirsUsed;

    private int scrollsUsed;

    private int hits;

    private int misses;

    private int cellsTraversed;

    public void addTreasure(int value){

        treasure += value;
    }

    public void addKill(){

        enemiesKilled++;
    }

    public void addFood(){

        foodConsumed++;
    }

    public void addElixir(){

        elixirsUsed++;
    }

    public void addScroll(){

        scrollsUsed++;
    }

    public void addHit(){

        hits++;
    }

    public void addMiss(){

        misses++;
    }

    public void addMove(){

        cellsTraversed++;
    }

    public int getTreasure(){

        return treasure;
    }

    public int getEnemiesKilled(){

        return enemiesKilled;
    }

    public int getFoodConsumed(){

        return foodConsumed;
    }

    public int getElixirsUsed(){

        return elixirsUsed;
    }

    public int getScrollsUsed(){

        return scrollsUsed;
    }

    public int getHits(){

        return hits;
    }

    public int getMisses(){

        return misses;
    }

    public int getCellsTraversed(){

        return cellsTraversed;
    }

}