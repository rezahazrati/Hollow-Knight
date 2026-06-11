package com.reza.hollowknight.model;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveProfile {
    public int slotId;
    public float playTime;
    public String lastSaved;
    public String lastBenchId;
    public String currentScene;
    public int currentHealth;
    public int maxMasks;
    public int geo;
    public int soul;
    public ArrayList<String> inventory = new ArrayList<>();
    public HashMap<String, Boolean> boughtSpells = new HashMap<>();
    public ArrayList<String> unlockedAbilities = new ArrayList<>();
}
