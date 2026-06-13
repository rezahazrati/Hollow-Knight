package com.reza.hollowknight.model.entities;

import com.reza.hollowknight.model.Vec2;
import java.util.ArrayList;
import java.util.HashMap;

public class Knight {
    private int mask = 5;
    private int maxMasks = 5;
    private int soulVessel = 0;
    private final int maxSoul = 99;
    private int geo = 0;
    private Vec2 position = new Vec2(0, 0);
    private Vec2 velocity = new Vec2(0, 0);
    private final ArrayList<String> inventory = new ArrayList<>();
    private final HashMap<String, Boolean> boughtSpells = new HashMap<>();
    private final ArrayList<String> unlockedAbilities = new ArrayList<>();

    public void addMask() {
        this.maxMasks += 1;
        this.mask = this.maxMasks;
    }

    public void heal() {
        if (this.soulVessel >= 33 && this.mask < this.maxMasks) {
            this.soulVessel -= 33;
            this.mask += 1;
        }
    }

    public void takeDamage(int amount) {
        this.mask = Math.max(0, this.mask - amount);
    }

    public void addSoulVessel() {
        this.soulVessel = Math.min(maxSoul, this.soulVessel + 11);
    }

    public int getMask() {
        return mask;
    }

    public int getMaxMasks() {
        return maxMasks;
    }

    public int getSoulVessel() {
        return soulVessel;
    }

    public int getGeo() {
        return geo;
    }

    public void setGeo(int geo) {
        this.geo = Math.max(0, this.geo + geo);
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    public ArrayList<String> getInventory() {
        return inventory;
    }

    public HashMap<String, Boolean> getBoughtSpells() {
        return boughtSpells;
    }

    public ArrayList<String> getUnlockedAbilities() {
        return unlockedAbilities;
    }
}
