package com.reza.hollowknight.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.reza.hollowknight.model.GameMapData;
import com.reza.hollowknight.model.GameSession;
import com.reza.hollowknight.model.GameSettings;
import com.reza.hollowknight.model.Vec2;
import com.reza.hollowknight.model.entities.Knight;
import com.reza.hollowknight.model.entities.enemy.BaseEnemy;

public class GamePlayController {
    private final GameSession gameSession;
    private GameMapData mapData;
    private TiledMap loadedMapReference;

    private final float MOVE_SPEED = 300.0f;
    private final float GRAVITY = -600.0f;
    private final float JUMP_FORCE = 450.0f;
    private final float JUMP_CUTOFF = 150.0f;

    private final float PLAYER_WIDTH = 16f;
    private final float PLAYER_HEIGHT = 24f;

    private final float DASH_DURATION = 0.2f;
    private final float DASH_SPEED = 700.0f;
    private final float DASH_COOLDOWN = 0.6f;

    private float dashActiveTimer = 0f;
    private float dashCooldownTimer = 0f;
    private float dashDirX = 0f;

    private boolean canDoubleJump = true;
    private boolean leftWallContact = false;
    private boolean rightWallContact = false;
    private boolean wasOnGround = false;

    private float focusTimer = 0f;
    private final float FOCUS_HEAL_DURATION = 1.5f;
    private boolean isFocusing = false;

    private float runToIdleTimer = 0f;
    private final float RUN_TO_IDLE_DURATION = 0.15f;

    public GamePlayController(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void initializeMapLayers(TiledMap map) {
        this.loadedMapReference = map;

        int width = map.getProperties().get("width", Integer.class);
        int height = map.getProperties().get("height", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);

        this.mapData = new GameMapData(width, height, tileWidth);

        MapLayer objectLayer = map.getLayers().get("logical");
        if (objectLayer != null) {
            for (MapObject obj : objectLayer.getObjects()) {
                float x = obj.getProperties().get("x", Float.class);
                float y = obj.getProperties().get("y", Float.class);
                Vec2 point = new Vec2(x, y);

                if (obj.getName() != null && obj.getName().toLowerCase().contains("respawn")) {
                    mapData.addRespawnPoint(point);
                } else {
                    mapData.addSpawnPoint(point);
                }
            }
        }
    }

    public void update(float delta) {
        Knight knight = gameSession.getKnight();
        if (knight == null || mapData == null) return;

        Vec2 currentPos = knight.getPosition();
        Vec2 velocity = knight.getVelocity();

        if (knight.isNoclipEnabled()) {
            float flySpeed = 400.0f * delta;
            if (Gdx.input.isKeyPressed(GameSettings.keyLeft)) currentPos.x -= flySpeed;
            if (Gdx.input.isKeyPressed(GameSettings.keyRight)) currentPos.x += flySpeed;
            if (Gdx.input.isKeyPressed(GameSettings.keyUp)) currentPos.y += flySpeed;
            if (Gdx.input.isKeyPressed(GameSettings.keyDown)) currentPos.y -= flySpeed;
            if (Gdx.input.isKeyPressed(GameSettings.keyDash)) currentPos.y += flySpeed;
            knight.setPosition(currentPos);
            return;
        }

        boolean onGround = isStandingOnGround(currentPos.x, currentPos.y);
        if (onGround) {
            canDoubleJump = true;
            if (!wasOnGround) {
                knight.setCurrentState(Knight.State.LANDING);
                wasOnGround = true;
            }
        } else {
            wasOnGround = false;
        }

        if (dashCooldownTimer > 0) {
            dashCooldownTimer -= delta;
        }

        if (dashActiveTimer > 0) {
            dashActiveTimer -= delta;
            velocity.y = 0;
            velocity.x = dashDirX * DASH_SPEED;
            knight.setCurrentState(Knight.State.DASH);

            float oldX = currentPos.x;
            currentPos.x += velocity.x * delta;
            if (isCollidingWithSolid(currentPos.x, currentPos.y)) {
                currentPos.x = oldX;
                velocity.x = 0;
                dashActiveTimer = 0;
            }

            if (dashActiveTimer <= 0) {
                velocity.x = 0;
            }

            knight.setPosition(currentPos);
            knight.setVelocity(velocity);
            return;
        }

        if (onGround && Gdx.input.isKeyPressed(GameSettings.keyFocus)) {
            velocity.x = 0;

            if (!isFocusing) {
                isFocusing = true;
                focusTimer = 0f;
                knight.setCurrentState(Knight.State.FOCUS_START);
                Gdx.app.log("FOCUS_SYSTEM", "Focus initiated. Soul: " + knight.getSoulVessel() + "/99");
            } else {
                focusTimer += delta;

                if (focusTimer >= FOCUS_HEAL_DURATION) {
                    if (knight.getSoulVessel() >= 33 && knight.getMask() < knight.getMaxMasks()) {
                        knight.heal();
                        knight.setCurrentState(Knight.State.FOCUS_GET);
                        Gdx.app.log("FOCUS_SYSTEM", "Successful Heal! Masks: " + knight.getMask() + ", Soul: " + knight.getSoulVessel());
                    } else {
                        knight.setCurrentState(Knight.State.FOCUS_END);
                        Gdx.app.log("FOCUS_SYSTEM", "Focus ended. Insufficient Soul or Maximum Health already met.");
                    }
                    focusTimer = 0f;
                } else if (knight.getCurrentState() != Knight.State.FOCUS_GET && focusTimer > 0.2f) {
                    knight.setCurrentState(Knight.State.FOCUS);
                }
            }

            velocity.y += GRAVITY * delta;
            applyMovementPhysics(currentPos, velocity, delta, knight);
            return;
        } else {
            if (isFocusing) {
                isFocusing = false;
                focusTimer = 0f;
                knight.setCurrentState(Knight.State.FOCUS_END);
                Gdx.app.log("FOCUS_SYSTEM", "Focus cancelled prematurely by player releasing key.");
            }
        }

        // Pogo-jumping implementation (Down key + Attack while airborne)
        //todo fix this
        if (Gdx.input.isKeyJustPressed(GameSettings.keyAttack) && Gdx.input.isKeyPressed(GameSettings.keyDown) && !onGround) {
            float checkY = currentPos.y - 4f;
            int tileXLeft = (int) ((currentPos.x - (PLAYER_WIDTH / 2f)) / mapData.getTileSize());
            int tileXRight = (int) ((currentPos.x + (PLAYER_WIDTH / 2f)) / mapData.getTileSize());
            int tileY = (int) (checkY / mapData.getTileSize());

            boolean hitSpike = isCellActiveAt("spikeLayer", tileXLeft, tileY) || isCellActiveAt("spikeLayer", tileXRight, tileY) || isCellActiveAt("water", tileXLeft, tileY) || isCellActiveAt("water", tileXRight, tileY);
            boolean hitEnemy = false;

            if (!hitSpike && gameSession.getEnemies() != null) {
                float halfW = PLAYER_WIDTH / 2f;
                float slashLeft = currentPos.x - halfW;
                float slashRight = currentPos.x + halfW;
                float slashBottom = currentPos.y - 16f;
                float slashTop = currentPos.y;

                for (BaseEnemy enemy : gameSession.getEnemies()) {
//                    if (enemy.getPosition() != null) {
//                        float ex = enemy.getPosition().x;
//                        float ey = enemy.getPosition().y;
//                        float ew = 32f; // Standard enemy hitbox size assumptions
//                        float eh = 32f;
//
//                        if (slashRight >= ex && slashLeft <= (ex + ew) && slashTop >= ey && slashBottom <= (ey + eh)) {
//                            hitEnemy = true;
//                            enemy.takeDamage(1);
//                            break;
//                        }
//                    }
                }
            }

            if (hitSpike || hitEnemy) {
                velocity.y = JUMP_FORCE;
                canDoubleJump = true;
                dashCooldownTimer = 0f;
                knight.setCurrentState(Knight.State.DOWNSLASH);
                knight.setPosition(currentPos);
                knight.setVelocity(velocity);
                return;
            }
        }

        if (Gdx.input.isKeyJustPressed(GameSettings.keyAttack)) {
            knight.setCurrentState(Knight.State.SLASH);
            //todo complete attack
        }
        if (Gdx.input.isKeyJustPressed(GameSettings.keyDash) && dashCooldownTimer <= 0) {
            dashActiveTimer = DASH_DURATION;
            dashCooldownTimer = DASH_COOLDOWN;
            dashDirX = knight.isFacingLeft() ? -1f : 1f;
            velocity.y = 0;
            velocity.x = dashDirX * DASH_SPEED;
            knight.setCurrentState(Knight.State.DASH);
            knight.setVelocity(velocity);
            return;
        }

        if (Gdx.input.isKeyPressed(GameSettings.keyLeft)) {
            velocity.x = -MOVE_SPEED;
            knight.setFacingLeft(true);
            if (onGround) knight.setCurrentState(Knight.State.RUN);
            runToIdleTimer = 0f;
        } else if (Gdx.input.isKeyPressed(GameSettings.keyRight)) {
            velocity.x = MOVE_SPEED;
            knight.setFacingLeft(false);
            if (onGround) knight.setCurrentState(Knight.State.RUN);
            runToIdleTimer = 0f;
        } else {
            if (onGround && (knight.getCurrentState() == Knight.State.RUN)) {
                knight.setCurrentState(Knight.State.RUN_TO_IDLE);
                runToIdleTimer = RUN_TO_IDLE_DURATION;
            }

            velocity.x = 0;

            if (onGround) {
                if (runToIdleTimer > 0) {
                    runToIdleTimer -= delta;
                    if (runToIdleTimer <= 0) {
                        knight.setCurrentState(Knight.State.IDLE);
                    }
                } else if (knight.getCurrentState() != Knight.State.LANDING && knight.getCurrentState() != Knight.State.FOCUS_END) {
                    if (Gdx.input.isKeyPressed(GameSettings.keyUp)) {
                        knight.setCurrentState(Knight.State.LOOKUP);
                    } else if (Gdx.input.isKeyPressed(GameSettings.keyDown)) {
                        knight.setCurrentState(Knight.State.LOOKDOWN);
                    } else {
                        knight.setCurrentState(Knight.State.IDLE);
                    }
                }
            }
        }

        boolean isWallSliding = false;
        if (!onGround && velocity.y < 0) {
            if ((Gdx.input.isKeyPressed(GameSettings.keyLeft) && leftWallContact) ||
                (Gdx.input.isKeyPressed(GameSettings.keyRight) && rightWallContact)) {
                isWallSliding = true;
                velocity.y = -100.0f;
                knight.setCurrentState(Knight.State.WALLJUMP);
            }
        }

        if (!isWallSliding) {
            velocity.y += GRAVITY * delta;
        }

        if (Gdx.input.isKeyJustPressed(GameSettings.keyJump)) {
            if (onGround) {
                velocity.y = JUMP_FORCE;
                knight.setCurrentState(Knight.State.AIRBORNE);
                wasOnGround = false;
            } else if (isWallSliding) {
                velocity.y = JUMP_FORCE;
                velocity.x = leftWallContact ? MOVE_SPEED : -MOVE_SPEED;
                knight.setFacingLeft(!leftWallContact);
                knight.setCurrentState(Knight.State.WALLJUMP);
            } else if (canDoubleJump) {
                velocity.y = JUMP_FORCE;
                knight.setCurrentState(Knight.State.DOUBLE_JUMP);
                canDoubleJump = false;
            }
        }

        if (!Gdx.input.isKeyPressed(GameSettings.keyJump) && velocity.y > JUMP_CUTOFF) {
            velocity.y = JUMP_CUTOFF;
        }

        if (!onGround && !isWallSliding && velocity.y < 0) {
            if (knight.getCurrentState() != Knight.State.DOUBLE_JUMP) {
                knight.setCurrentState(Knight.State.FALL);
            }
        }

        leftWallContact = false;
        rightWallContact = false;

        applyMovementPhysics(currentPos, velocity, delta, knight);
    }

    private void applyMovementPhysics(Vec2 currentPos, Vec2 velocity, float delta, Knight knight) {
        float oldX = currentPos.x;
        currentPos.x += velocity.x * delta;
        if (isCollidingWithSolid(currentPos.x, currentPos.y)) {
            if (velocity.x < 0) leftWallContact = true;
            if (velocity.x > 0) rightWallContact = true;
            currentPos.x = oldX;
            velocity.x = 0;
        }

        float oldY = currentPos.y;
        currentPos.y += velocity.y * delta;
        if (isCollidingWithSolid(currentPos.x, currentPos.y)) {
            currentPos.y = oldY;
            velocity.y = 0;
        }

        if (currentPos.x < 0) currentPos.x = 0;
        if (currentPos.y < 0) currentPos.y = 0;
        if (currentPos.x > mapData.getMapWidthPixels() - PLAYER_WIDTH)
            currentPos.x = mapData.getMapWidthPixels() - PLAYER_WIDTH;
        if (currentPos.y > mapData.getMapHeightPixels() - PLAYER_HEIGHT)
            currentPos.y = mapData.getMapHeightPixels() - PLAYER_HEIGHT;

        knight.setPosition(currentPos);
        knight.setVelocity(velocity);

        checkLayerIntersections(currentPos.x, currentPos.y, knight);
    }

    private boolean isCollidingWithSolid(float x, float y) {
        float halfW = PLAYER_WIDTH / 2f;

        return isCellActiveAt("main", (int) ((x - halfW) / mapData.getTileSize()), (int) (y / mapData.getTileSize())) ||
            isCellActiveAt("main", (int) ((x + halfW) / mapData.getTileSize()), (int) (y / mapData.getTileSize())) ||
            isCellActiveAt("main", (int) ((x - halfW) / mapData.getTileSize()), (int) ((y + PLAYER_HEIGHT) / mapData.getTileSize())) ||
            isCellActiveAt("main", (int) ((x + halfW) / mapData.getTileSize()), (int) ((y + PLAYER_HEIGHT) / mapData.getTileSize()));
    }

    private boolean isStandingOnGround(float x, float y) {
        float halfW = PLAYER_WIDTH / 2f;
        return isCellActiveAt("main", (int) ((x - halfW) / mapData.getTileSize()), (int) ((y - 1f) / mapData.getTileSize())) ||
            isCellActiveAt("main", (int) ((x + halfW) / mapData.getTileSize()), (int) ((y - 1f) / mapData.getTileSize()));
    }

    private void checkLayerIntersections(float x, float y, Knight knight) {
        int tileX = (int) (x / mapData.getTileSize());
        int tileY = (int) (y / mapData.getTileSize());

        if (tileX < 0 || tileY < 0) return;

        if (isCellActiveAt("spikeLayer", tileX, tileY)) {
            damage(knight);
            Gdx.app.log("GAMEPLAY_LOGIC", "masks = " + knight.getMask() + " geo = " + knight.getPosition().x + " geo = " + knight.getGeo());
        }

        if (isCellActiveAt("water", tileX, tileY)) {
            damage(knight);
            Gdx.app.log("GAMEPLAY_LOGIC", "masks = " + knight.getMask() + " geo = " + knight.getPosition().x + " geo = " + knight.getGeo());
        }

        if (isCellActiveAt("bench", tileX, tileY)) {
            Gdx.app.log("GAMEPLAY_LOGIC", "Knight resting on a Bench cell at: [" + tileX + ", " + tileY + "]");
        }
    }

    private void damage(Knight knight) {
        if (!knight.isGodModeEnabled()) {
            knight.setCurrentState(Knight.State.IDLE_HURT);
            knight.takeDamage(1);
            Vec2 newPosition = findNearestRespawnPoint(knight.getPosition().x, knight.getPosition().y);
            knight.setPosition(newPosition);
            if (knight.getMask() == 0) {
                knight.setCurrentState(Knight.State.DEATH);
                knight.setGeo(0);
                knight.setFacingLeft(false);
                knight.addMask(5);
                knight.setPosition(mapData.getSpawnPoints().getFirst());
            }
        }
    }

    private Vec2 findNearestRespawnPoint(float x, float y) {
        Vec2 nearestPoint = new Vec2();
        Vec2 currentPoint = new Vec2(x, y);
        nearestPoint.x = mapData.getRespawnPoints().getFirst().x;
        nearestPoint.y = mapData.getRespawnPoints().getFirst().y;
        mapData.getRespawnPoints().forEach(respawnPoint -> {
            if (currentPoint.dst(respawnPoint) <= currentPoint.dst(nearestPoint)) {
                nearestPoint.x = respawnPoint.x;
                nearestPoint.y = respawnPoint.y;
            }
        });
        return nearestPoint;
    }

    private boolean isCellActiveAt(String layerName, int tileX, int tileY) {
        if (loadedMapReference == null) return false;
        TiledMapTileLayer layer = (TiledMapTileLayer) loadedMapReference.getLayers().get(layerName);
        return layer != null && layer.getCell(tileX, tileY) != null;
    }
}
