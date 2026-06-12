package com.reza.hollowknight.model;

import com.reza.hollowknight.model.atmosphere.Atmosphere;
import com.reza.hollowknight.model.entities.InteractiveEntity;
import com.reza.hollowknight.model.entities.Knight;
import com.reza.hollowknight.model.entities.enemy.BaseEnemy;
import com.reza.hollowknight.model.entities.enemy.FalseKnightBoss;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
    private Atmosphere atmosphere;
    private Knight knight;
    private List<BaseEnemy> enemies = new ArrayList<>();
    private FalseKnightBoss falseKnight;
    private List<InteractiveEntity> interactiveEntities = new ArrayList<>();

    public GameSession() {
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }

    public Knight getKnight() {
        return knight;
    }

    public void setKnight(Knight knight) {
        this.knight = knight;
    }

    public List<BaseEnemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<BaseEnemy> enemies) {
        this.enemies = enemies;
    }

    public FalseKnightBoss getFalseKnight() {
        return falseKnight;
    }

    public void setFalseKnight(FalseKnightBoss falseKnight) {
        this.falseKnight = falseKnight;
    }

    public List<InteractiveEntity> getInteractiveEntities() {
        return interactiveEntities;
    }

    public void setInteractiveEntities(List<InteractiveEntity> interactiveEntities) {
        this.interactiveEntities = interactiveEntities;
    }
}
