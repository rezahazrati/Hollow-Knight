package com.reza.hollowknight.model.entities;

import com.reza.hollowknight.model.Vec2;
import com.reza.hollowknight.model.enums.CharmType;

public class BreakableWall extends InteractiveEntity {
    private int hitsRemaining = 3;
    private boolean destroyed = false;
    private CharmType hiddenReward = CharmType.VOID_HEART;
    private float width;
    private float height;

    public BreakableWall() {
        super("breakable_wall_01", new Vec2(0, 0));
        this.width = 64.0f;
        this.height = 128.0f;
    }

    public BreakableWall(String entityId, Vec2 position, float width, float height) {
        super(entityId, position);
        this.width = width;
        this.height = height;
    }

    public void hit() {
        if (destroyed) {
            return;
        }
        hitsRemaining--;
        if (hitsRemaining <= 0) {
            destroyed = true;
            setInteracted(true);
        }
    }

    public int getHitsRemaining() {
        return hitsRemaining;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public CharmType getHiddenReward() {
        return hiddenReward;
    }

    public void setHiddenReward(CharmType hiddenReward) {
        this.hiddenReward = hiddenReward;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
