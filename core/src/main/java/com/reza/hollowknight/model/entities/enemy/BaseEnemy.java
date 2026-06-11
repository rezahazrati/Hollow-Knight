package com.reza.hollowknight.model.entities.enemy;

import com.reza.hollowknight.model.Vec2;

public class BaseEnemy {
    private int health;
    public Vec2 position;
    public Vec2 velocity;

    public BaseEnemy(int health) {
        this.health = health;
    }

    public BaseEnemy() {

    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int amount) {
        this.health -= amount;
    }

}
