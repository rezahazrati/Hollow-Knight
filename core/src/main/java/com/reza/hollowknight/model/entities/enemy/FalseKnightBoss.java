package com.reza.hollowknight.model.entities.enemy;

public class FalseKnightBoss extends BaseEnemy {
    private boolean armorBroken = false;
    private int phase = 1;

    public FalseKnightBoss() {
        super(65);
        setContactDamage(2);
    }

    public boolean isArmorBroken() {
        return armorBroken;
    }

    public void setArmorBroken(boolean armorBroken) {
        this.armorBroken = armorBroken;
    }

    public int getPhase() {
        return phase;
    }

    public void nextPhase() {
        this.phase++;
    }

    @Override
    public void updateStateBehavior(float playerX, float playerY) {
        setFacingLeft(playerX < this.position.x);
    }
}
