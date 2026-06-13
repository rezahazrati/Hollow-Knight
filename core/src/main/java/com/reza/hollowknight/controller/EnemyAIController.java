package com.reza.hollowknight.controller;

import com.reza.hollowknight.model.GameSession;
import com.reza.hollowknight.model.entities.Knight;
import com.reza.hollowknight.model.entities.enemy.BaseEnemy;
import com.reza.hollowknight.model.entities.enemy.Mosscreep;
import com.reza.hollowknight.model.entities.enemy.WingedSentry;
import com.reza.hollowknight.model.entities.enemy.HuskHornhead;
import com.reza.hollowknight.model.entities.enemy.CrystalGuardian;

public class EnemyAIController {
    private final GameSession gameSession;
    private float mapLeftBound = 0.0f;
    private float mapRightBound = 2000.0f;

    public EnemyAIController(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void update(float delta) {
        Knight knight = gameSession.getKnight();
        if (knight == null || gameSession.getEnemies() == null) {
            return;
        }

        float playerX = knight.getPosition().x;
        float playerY = knight.getPosition().y;

        for (BaseEnemy enemy : gameSession.getEnemies()) {
            if (enemy.getHealth() <= 0) {
                continue;
            }

            enemy.updateStateBehavior(playerX, playerY);

            if (enemy instanceof Mosscreep) {
                handleMosscreepAI((Mosscreep) enemy, delta);
            } else if (enemy instanceof WingedSentry) {
                handleWingedSentryAI((WingedSentry) enemy, playerX, playerY, delta);
            } else if (enemy instanceof HuskHornhead) {
                handleHuskHornheadAI((HuskHornhead) enemy, playerX, playerY, delta);
            } else if (enemy instanceof CrystalGuardian) {
                handleCrystalGuardianAI((CrystalGuardian) enemy, playerX, playerY, delta);
            }
        }
    }

    private void handleMosscreepAI(Mosscreep mosscreep, float delta) {
        float speed = 70.0f;
        if (mosscreep.isFacingLeft()) {
            mosscreep.velocity.x = -speed;
            mosscreep.position.x += mosscreep.velocity.x * delta;
            if (mosscreep.position.x <= mapLeftBound) {
                mosscreep.position.x = mapLeftBound;
                mosscreep.setFacingLeft(false);
            }
        } else {
            mosscreep.velocity.x = speed;
            mosscreep.position.x += mosscreep.velocity.x * delta;
            if (mosscreep.position.x >= mapRightBound) {
                mosscreep.position.x = mapRightBound;
                mosscreep.setFacingLeft(true);
            }
        }
        mosscreep.velocity.y = 0.0f;
    }

    private void handleWingedSentryAI(WingedSentry sentry, float playerX, float playerY, float delta) {
        float attackSpeed = 260.0f;
        float normalSpeed = 75.0f;

        if (sentry.isPerformingChargeAttack()) {
            sentry.velocity.x = sentry.isFacingLeft() ? -attackSpeed : attackSpeed;
            sentry.velocity.y = 0.0f;
            sentry.position.x += sentry.velocity.x * delta;

            float xDiff = Math.abs(playerX - sentry.position.x);
            if (xDiff < 15.0f || sentry.position.x <= mapLeftBound || sentry.position.x >= mapRightBound) {
                sentry.velocity.set(0, 0);
            }
        } else {
            sentry.setFacingLeft(playerX < sentry.position.x);
            float dx = playerX - sentry.position.x;
            float dy = playerY - sentry.position.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance < 280.0f && Math.abs(dy) < 45.0f) {
                sentry.velocity.x = sentry.isFacingLeft() ? -attackSpeed : attackSpeed;
                sentry.velocity.y = 0.0f;
            } else if (distance > 12.0f) {
                sentry.velocity.x = (dx / distance) * normalSpeed;
                sentry.velocity.y = (dy / distance) * normalSpeed;
                sentry.position.x += sentry.velocity.x * delta;
                sentry.position.y += sentry.velocity.y * delta;
            } else {
                sentry.velocity.set(0, 0);
            }
        }
    }

    private void handleHuskHornheadAI(HuskHornhead hornhead, float playerX, float playerY, float delta) {
        if (hornhead.isBlindCharging()) {
            float chargeSpeed = 250.0f;
            hornhead.velocity.x = hornhead.isFacingLeft() ? -chargeSpeed : chargeSpeed;
            hornhead.position.x += hornhead.velocity.x * delta;

            if (hornhead.position.x <= mapLeftBound || hornhead.position.x >= mapRightBound) {
                if (hornhead.position.x <= mapLeftBound) hornhead.position.x = mapLeftBound;
                if (hornhead.position.x >= mapRightBound) hornhead.position.x = mapRightBound;
                hornhead.interruptCharge();
                hornhead.setFacingLeft(!hornhead.isFacingLeft());
            }
        } else {
            float patrolSpeed = 55.0f;
            float xDiff = playerX - hornhead.position.x;
            float yDiff = Math.abs(playerY - hornhead.position.y);

            boolean seesPlayer = false;
            if (hornhead.isFacingLeft() && xDiff < 0 && xDiff > -240.0f && yDiff < 35.0f) {
                seesPlayer = true;
            } else if (!hornhead.isFacingLeft() && xDiff > 0 && xDiff < 240.0f && yDiff < 35.0f) {
                seesPlayer = true;
            }

            if (seesPlayer) {
                hornhead.velocity.x = hornhead.isFacingLeft() ? -250.0f : 250.0f;
            } else {
                if (hornhead.isFacingLeft()) {
                    hornhead.velocity.x = -patrolSpeed;
                    hornhead.position.x += hornhead.velocity.x * delta;
                    if (hornhead.position.x <= mapLeftBound) {
                        hornhead.position.x = mapLeftBound;
                        hornhead.setFacingLeft(false);
                    }
                } else {
                    hornhead.velocity.x = patrolSpeed;
                    hornhead.position.x += hornhead.velocity.x * delta;
                    if (hornhead.position.x >= mapRightBound) {
                        hornhead.position.x = mapRightBound;
                        hornhead.setFacingLeft(true);
                    }
                }
            }
        }
        hornhead.velocity.y = 0.0f;
    }

    private void handleCrystalGuardianAI(CrystalGuardian guardian, float playerX, float playerY, float delta) {
        guardian.tickLaser(delta);
        guardian.velocity.set(0, 0);
        if (!guardian.isFiringLaserBeam()) {
            guardian.setFacingLeft(playerX < guardian.position.x);
        }
    }

    public void setMapBounds(float left, float right) {
        this.mapLeftBound = left;
        this.mapRightBound = right;
    }
}
