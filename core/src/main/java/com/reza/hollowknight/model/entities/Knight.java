package com.reza.hollowknight.model.entities;

import com.badlogic.gdx.math.Vector2;
import com.reza.hollowknight.model.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Knight {
    // --- NEW/UPGRADED: Animation & State tracking machine matching image_aea32b.png ---
    public enum State {
        IDLE,
        RUN,
        RUN_TO_IDLE,
        AIRBORNE,
        FALL,
        LANDING,
        WALLJUMP,
        WALL_SLIDING,
        DASH,
        DOUBLE_JUMP,
        SLASH,
        SLASHALT,
        DOWNSLASH,
        UPSLASH,
        LOOKUP,
        LOOKDOWN,
        FOCUS_START,
        FOCUS,
        FOCUS_GET,
        FOCUS_END,
        FIREBALL,
        IDLE_HURT,
        SCREAM,
        DEATH,
        WALK
    }

    public Knight(float startX, float startY) {
        this.position = new Vec2(startX, startY);
        this.velocity = new Vec2(0, 0);
        this.bounds = new Rectangle((int) startX, (int) startY, (int) WIDTH, (int) HEIGHT);
    }
    private State currentState = State.IDLE;
    private boolean facingLeft = false;

    // --- Your Existing Structs (Untouched) ---
    private int mask = 5;
    private int maxMasks = 5;
    private int soulVessel =50;
    private final int maxSoul = 99;
    private int geo = 0;
    private Vec2 position = new Vec2(0, 0);
    private Vec2 velocity = new Vec2(0, 0);
    private final ArrayList<String> inventory = new ArrayList<>();
    private final HashMap<String, Boolean> boughtSpells = new HashMap<>();
    private final ArrayList<String> unlockedAbilities = new ArrayList<>();
    public Rectangle bounds;

    // Define dimensions structurally (e.g., width: 32 units, height: 48 units)
    public static final float WIDTH = 32f;
    public static final float HEIGHT = 48f;
    private boolean godModeEnabled = false;
    private boolean noclipEnabled = false;

    // --- NEW: Getters and Setters for state machine ---
    public State getCurrentState() { return currentState; }
    public void setCurrentState(State currentState) { this.currentState = currentState; }
    public boolean isFacingLeft() { return facingLeft; }
    public void setFacingLeft(boolean facingLeft) { this.facingLeft = facingLeft; }

    // --- Your Existing Logic (Untouched) ---
    public void addMask() {
        this.mask += 1;
        this.mask = Math.max(mask, maxMasks);
    }
    public void addMask(int amount) {
        this.mask += amount;
        this.mask = Math.max(mask, maxMasks);
    }

    public void heal() {
        if (this.soulVessel >= 33 && this.mask < this.maxMasks) {
            this.soulVessel -= 33;
            this.mask += 1;
        }
    }

    public void takeDamage(int amount) {
        if (godModeEnabled) return;
        this.mask = Math.max(0, this.mask - amount);
    }

    public void addSoulVessel() {
        this.soulVessel = Math.min(maxSoul, this.soulVessel + 11);
    }

    public void cheatEmergencyHeal() {
        this.mask = this.maxMasks;
    }

    public void cheatRefillSoul() {
        this.soulVessel = this.maxSoul;
    }

    public void toggleGodMode() {
        this.godModeEnabled = !this.godModeEnabled;
    }

    public void toggleNoclip() {
        this.noclipEnabled = !this.noclipEnabled;
        if (noclipEnabled) {
            this.velocity.set(0, 0);
        }
    }

    public int getMask() { return mask; }
    public int getMaxMasks() { return maxMasks; }
    public int getSoulVessel() { return soulVessel; }
    public int getGeo() { return geo; }
    public void setGeo(int geo) { this.geo = Math.max(0, this.geo + geo); }
    public Vec2 getPosition() { return position; }
    public void setPosition(Vec2 position) { this.position = position; }
    public Vec2 getVelocity() { return velocity; }
    public void setVelocity(Vec2 velocity) { this.velocity = velocity; }
    public ArrayList<String> getInventory() { return inventory; }
    public HashMap<String, Boolean> getBoughtSpells() { return boughtSpells; }
    public ArrayList<String> getUnlockedAbilities() { return unlockedAbilities; }

    public boolean isGodModeEnabled() { return godModeEnabled; }
    public boolean isNoclipEnabled() { return noclipEnabled; }
    public void updateInput(boolean left, boolean right, boolean jump, float moveSpeed, float jumpVelocity, float gravity, float delta) {
        if (left) {
            velocity.x = -moveSpeed;
        } else if (right) {
            velocity.x = moveSpeed;
        } else {
            velocity.x = 0;
        }

        velocity.y -= gravity * delta;

        if (jump && velocity.y == 0) {
            velocity.y = jumpVelocity;
        }
    }
}
