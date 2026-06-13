package com.reza.hollowknight.model.entities.enemy;

public class CrystalGuardian extends BaseEnemy {
    public enum LaserState {
        COOLDOWN,
        CHARGING,
        FIRING
    }

    private LaserState currentState = LaserState.COOLDOWN;
    private float stateTimer = 0.0f;

    private static final float COOLDOWN_DURATION = 3.0f;
    private static final float CHARGE_DURATION = 1.2f;
    private static final float FIRING_DURATION = 0.6f;

    public CrystalGuardian() {
        super(150);
        this.velocity.set(0, 0);
    }

    public void tickLaser(float delta) {
        stateTimer += delta;

        switch (currentState) {
            case COOLDOWN:
                if (stateTimer >= COOLDOWN_DURATION) {
                    currentState = LaserState.CHARGING;
                    stateTimer = 0.0f;
                }
                break;

            case CHARGING:
                if (stateTimer >= CHARGE_DURATION) {
                    currentState = LaserState.FIRING;
                    stateTimer = 0.0f;
                }
                break;

            case FIRING:
                if (stateTimer >= FIRING_DURATION) {
                    currentState = LaserState.COOLDOWN;
                    stateTimer = 0.0f;
                }
                break;
        }
    }

    @Override
    public void updateStateBehavior(float playerX, float playerY) {
        if (currentState == LaserState.COOLDOWN) {
            setFacingLeft(playerX < this.position.x);
        }
    }

    public boolean isCharging() {
        return currentState == LaserState.CHARGING;
    }

    public boolean isFiringLaserBeam() {
        return currentState == LaserState.FIRING;
    }

    public LaserState getCurrentState() {
        return currentState;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public float getChargePercentage() {
        if (currentState == LaserState.CHARGING) {
            return Math.min(1.0f, stateTimer / CHARGE_DURATION);
        }
        return 0.0f;
    }
}
