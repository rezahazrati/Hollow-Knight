package com.reza.hollowknight.model.entities.enemy;

import com.reza.hollowknight.model.Vec2;

public abstract class BaseEnemy {
    private int health;
    private int maxHealth;
    private int contactDamage = 1;
    private boolean facingLeft = true;
    public Vec2 position = new Vec2(0, 0);
    public Vec2 velocity = new Vec2(0, 0);

    public BaseEnemy() {
    }

    public BaseEnemy(int health) {
        this.health = health;
        this.maxHealth = health;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int amount) {
        this.health = Math.max(0, this.health - amount);
    }

    public int getContactDamage() {
        return contactDamage;
    }

    public void setContactDamage(int damage) {
        this.contactDamage = damage;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public abstract void updateStateBehavior(float playerX, float playerY);
}
