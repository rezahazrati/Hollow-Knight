package com.reza.hollowknight.model.entities.enemy;

public class HuskHornhead extends BaseEnemy {
    private boolean blindCharging = false;

    public HuskHornhead() {
        super(25);
    }

    public boolean isBlindCharging() {
        return blindCharging;
    }

    @Override
    public void updateStateBehavior(float playerX, float playerY) {
        if (!blindCharging) {
            setFacingLeft(playerX < this.position.x);
            float xDiff = Math.abs(playerX - position.x);
            float yDiff = Math.abs(playerY - position.y);
            if (xDiff < 200f && yDiff < 30f) {
                blindCharging = true;
            }
        }
    }

    public void interruptCharge() {
        this.blindCharging = false;
    }
}
