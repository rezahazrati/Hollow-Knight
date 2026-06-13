package com.reza.hollowknight.controller;

import com.reza.hollowknight.model.GameSession;
import com.reza.hollowknight.model.entities.Knight;
import com.reza.hollowknight.model.entities.enemy.FalseKnightBoss;
import java.util.Random;

public class BossAIController {
    public enum BossMove {
        IDLE,
        MACE_SLAM,
        CHARGE_RUN,
        OFFENSIVE_LEAP,
        DEFENSIVE_LEAP,
        SHOCKWAVE_SLAM
    }

    private final GameSession gameSession;
    private final Random random = new Random();
    private BossMove currentMove = BossMove.IDLE;
    private BossMove lastMove = BossMove.IDLE;

    private float actionTimer = 0.0f;
    private float moveDuration = 0.0f;
    private float speedMultiplier = 1.0f;

    private boolean isStunned = false;
    private float stunTimer = 0.0f;
    private final float maxStunDuration = 4.0f;
    private boolean halfHealthThresholdTriggered = false;

    public BossAIController(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void update(float delta) {
        FalseKnightBoss boss = gameSession.getFalseKnight();
        Knight knight = gameSession.getKnight();
        if (boss == null || knight == null || boss.getHealth() <= 0) {
            return;
        }

        float playerX = knight.getPosition().x;
        float playerY = knight.getPosition().y;

        if (!halfHealthThresholdTriggered && boss.getHealth() <= 32) {
            triggerStun(boss);
        }

        if (isStunned) {
            handleStunLifecycle(boss, delta);
            return;
        }

        boss.updateStateBehavior(playerX, playerY);
        actionTimer += delta;

        if (currentMove == BossMove.IDLE) {
            if (actionTimer >= 1.2f) {
                selectNextAction(boss, playerX, playerY);
            }
        } else {
            executeCurrentMoveBehavior(boss, playerX, playerY, delta);
        }
    }

    private void triggerStun(FalseKnightBoss boss) {
        isStunned = true;
        halfHealthThresholdTriggered = true;
        boss.setArmorBroken(true);
        currentMove = BossMove.IDLE;
        boss.velocity.set(0, 0);
        stunTimer = 0.0f;
    }

    private void handleStunLifecycle(FalseKnightBoss boss, float delta) {
        stunTimer += delta;
        if (stunTimer >= maxStunDuration) {
            isStunned = false;
            boss.setArmorBroken(false);
            boss.nextPhase();
            speedMultiplier = 1.5f;
            actionTimer = 0.0f;
        }
    }

    private void selectNextAction(FalseKnightBoss boss, float playerX, float playerY) {
        actionTimer = 0.0f;
        float distance = Math.abs(playerX - boss.position.x);

        BossMove chosenMove;
        int rolls = 0;

        do {
            int roll = random.nextInt(100);
            if (distance < 150.0f) {
                if (roll < 50) chosenMove = BossMove.MACE_SLAM;
                else if (roll < 75) chosenMove = BossMove.DEFENSIVE_LEAP;
                else chosenMove = BossMove.CHARGE_RUN;
            } else if (distance < 400.0f) {
                if (roll < 40) chosenMove = BossMove.CHARGE_RUN;
                else if (roll < 70) chosenMove = BossMove.OFFENSIVE_LEAP;
                else chosenMove = BossMove.MACE_SLAM;
            } else {
                if (roll < 60) chosenMove = BossMove.OFFENSIVE_LEAP;
                else chosenMove = BossMove.CHARGE_RUN;
            }

            if (chosenMove == BossMove.SHOCKWAVE_SLAM && boss.getPhase() < 2) {
                chosenMove = BossMove.MACE_SLAM;
            }
            if (boss.getPhase() >= 2 && random.nextInt(100) < 30) {
                chosenMove = BossMove.SHOCKWAVE_SLAM;
            }
            rolls++;
        } while (chosenMove == lastMove && rolls < 5);

        currentMove = chosenMove;
        lastMove = chosenMove;

        switch (currentMove) {
            case MACE_SLAM:
            case SHOCKWAVE_SLAM:
                moveDuration = 1.0f;
                boss.velocity.set(0, 0);
                break;
            case CHARGE_RUN:
                moveDuration = 1.5f;
                break;
            case OFFENSIVE_LEAP:
            case DEFENSIVE_LEAP:
                moveDuration = 0.8f;
                boss.velocity.y = 350.0f;
                break;
            default:
                moveDuration = 0.0f;
                break;
        }
    }

    private void executeCurrentMoveBehavior(FalseKnightBoss boss, float playerX, float playerY, float delta) {
        if (actionTimer >= moveDuration) {
            currentMove = BossMove.IDLE;
            actionTimer = 0.0f;
            boss.velocity.set(0, 0);
            return;
        }

        float directionSign = boss.isFacingLeft() ? -1.0f : 1.0f;

        switch (currentMove) {
            case CHARGE_RUN:
                boss.velocity.x = directionSign * 160.0f * speedMultiplier;
                boss.position.x += boss.velocity.x * delta;
                break;

            case OFFENSIVE_LEAP:
                boss.velocity.x = directionSign * 200.0f * speedMultiplier;
                boss.position.x += boss.velocity.x * delta;
                boss.position.y += boss.velocity.y * delta;
                boss.velocity.y -= 600.0f * delta;
                break;

            case DEFENSIVE_LEAP:
                boss.velocity.x = -directionSign * 180.0f * speedMultiplier;
                boss.position.x += boss.velocity.x * delta;
                boss.position.y += boss.velocity.y * delta;
                boss.velocity.y -= 600.0f * delta;
                break;

            case MACE_SLAM:
            case SHOCKWAVE_SLAM:
                boss.velocity.set(0, 0);
                break;

            default:
                break;
        }
    }

    public BossMove getCurrentMove() {
        return currentMove;
    }

    public boolean isBossStunned() {
        return isStunned;
    }
}
