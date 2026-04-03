package com.rogue.domain.stats;

public class ScoreEntry {

    private int treasure;

    private int level;

    private int enemiesKilled;

    private int food;

    private int elixirs;

    private int scrolls;

    private int hits;

    private int misses;

    private int moves;

    public ScoreEntry(){}

    public ScoreEntry(
            int treasure,
            int level,
            GameStatistics stats){

        this.treasure = treasure;

        this.level = level;

        enemiesKilled =
                stats.getEnemiesKilled();

        food =
                stats.getFoodConsumed();

        elixirs =
                stats.getElixirsUsed();

        scrolls =
                stats.getScrollsUsed();

        hits =
                stats.getHits();

        misses =
                stats.getMisses();

        moves =
                stats.getCellsTraversed();
    }

    public int getTreasure(){

        return treasure;
    }

    public int getLevel(){

        return level;
    }

    public int getEnemiesKilled(){

        return enemiesKilled;
    }

    public int getFood(){

        return food;
    }

    public int getElixirs(){

        return elixirs;
    }

    public int getScrolls(){

        return scrolls;
    }

    public int getHits(){

        return hits;
    }

    public int getMisses(){

        return misses;
    }

    public int getMoves(){

        return moves;
    }

}