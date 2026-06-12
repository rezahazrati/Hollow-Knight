package com.reza.hollowknight.model.entities.enemy;

public class WingedSentry extends BaseEnemy {
    private boolean performingChargeAttack = false;

    public WingedSentry() {
        super(20);
    }

    public boolean isPerformingChargeAttack() {
        return performingChargeAttack;
    }

    @Override
    public void updateStateBehavior(float playerX, float playerY) {
        setFacingLeft(playerX < this.position.x);
        float xDiff = Math.abs(playerX - position.x);
        float yDiff = Math.abs(playerY - position.y);
        performingChargeAttack = xDiff < 300f && yDiff < 40f;
    }
}
