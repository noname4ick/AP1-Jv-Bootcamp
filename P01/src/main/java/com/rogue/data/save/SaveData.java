package com.rogue.data.save;

import com.rogue.domain.stats.ScoreEntry;

import java.util.ArrayList;
import java.util.List;

public class SaveData {

    public int level;

    public int playerHP;

    public int maxHP;

    public int strength;

    public int agility;

    public int speed;

    public int satiation = 100;

    public int treasure;

    public long levelSeed;

    public List<SavedItem> backpack =
            new ArrayList<>();

    public SavedItem equippedWeapon;

    public int difficultyBias;

    public List<ScoreEntry> scores =
            new ArrayList<>();

}