package com.reza.hollowknight.model;

import java.util.ArrayList;
import java.util.List;

public class GameMapData {
    private final int mapWidthTiles;
    private final int mapHeightTiles;
    private final int tileSize;

    private final List<Vec2> spawnPoints = new ArrayList<>();
    private final List<Vec2> respawnPoints = new ArrayList<>();

    public GameMapData(int width, int height, int tileSize) {
        this.mapWidthTiles = width;
        this.mapHeightTiles = height;
        this.tileSize = tileSize;
    }

    public int getMapWidthPixels() { return mapWidthTiles * tileSize; }
    public int getMapHeightPixels() { return mapHeightTiles * tileSize; }
    public int getTileSize() { return tileSize; }

    public void addSpawnPoint(Vec2 point) { spawnPoints.add(point); }
    public List<Vec2> getSpawnPoints() { return spawnPoints; }

    public void addRespawnPoint(Vec2 point) { respawnPoints.add(point); }
    public List<Vec2> getRespawnPoints() { return respawnPoints; }
}
