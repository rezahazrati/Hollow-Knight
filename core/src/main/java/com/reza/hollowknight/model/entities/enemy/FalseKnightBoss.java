package com.reza.hollowknight.model.entities.enemy;

public class FalseKnightBoss extends BaseEnemy{
    private static final int health = 50;
    private boolean isStunned = false;

    public FalseKnightBoss() {
        super(health);
    }


}
