package com.reza.hollowknight.model.entities.enemy;

public class CrystalHunter extends BaseEnemy {
    private float timeSinceLastProjectile = 0.0f;

    public CrystalHunter() {
        super(20);
    }

    public boolean canFireProjectile(float delta) {
        timeSinceLastProjectile += delta;
        if (timeSinceLastProjectile >= 2.5f) {
            timeSinceLastProjectile = 0.0f;
            return true;
        }
        return false;
    }

    @Override
    public void updateStateBehavior(float playerX, float playerY) {
        setFacingLeft(playerX < this.position.x);
    }
}
