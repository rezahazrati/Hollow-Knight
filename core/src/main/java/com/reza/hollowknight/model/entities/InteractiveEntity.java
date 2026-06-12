package com.reza.hollowknight.model.entities;

import com.reza.hollowknight.model.Vec2;

public class InteractiveEntity {
    private String entityId;
    private Vec2 position = new Vec2(0, 0);
    private boolean interacted = false;

    public InteractiveEntity() {
    }

    public InteractiveEntity(String entityId, Vec2 position) {
        this.entityId = entityId;
        this.position = position;
    }

    public String getEntityId() {
        return entityId;
    }

    public Vec2 getPosition() {
        return position;
    }

    public boolean isInteracted() {
        return interacted;
    }

    public void setInteracted(boolean interacted) {
        this.interacted = interacted;
    }
}
