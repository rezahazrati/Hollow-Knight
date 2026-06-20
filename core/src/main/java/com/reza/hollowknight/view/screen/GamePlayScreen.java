package com.reza.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.controller.GamePlayController;
import com.reza.hollowknight.model.App;
import com.reza.hollowknight.model.GameSession;
import com.reza.hollowknight.model.GameSettings;
import com.reza.hollowknight.model.Vec2;
import com.reza.hollowknight.model.entities.Knight;
import com.reza.hollowknight.view.rendering.GameRenderer;

public class GamePlayScreen extends ScreenAdapter {
    private final HollowGame game;
    private TiledMap map;
    private GameSession session;
    private GamePlayController controller;
    private GameRenderer renderer;
    private FitViewport gameViewport;

    public GamePlayScreen(HollowGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        session = new GameSession();
        App.setGameSession(session);
        if (GameSettings.bgmEnabled && game.assetLoader.greenpath != null) {
            game.assetLoader.greenpath.setVolume(GameSettings.bgmVolume);
            if (!game.assetLoader.greenpath.isPlaying()) {
                game.assetLoader.greenpath.setLooping(true);
                game.assetLoader.greenpath.play();
            }
        } else if (game.assetLoader.greenpath != null && game.assetLoader.greenpath.isPlaying()) {
            game.assetLoader.greenpath.stop();
        }
        map = new TmxMapLoader().load("maps/greenpath.tmx");
        gameViewport = new FitViewport(426, 240);


        float spawnX = 710f;
        float spawnY = 468f;

        MapLayer logicalLayer = map.getLayers().get("logical");
        if (logicalLayer != null) {
            MapObject benchObj = logicalLayer.getObjects().get("bench");
            if (benchObj != null) {
                spawnX = benchObj.getProperties().get("x", Float.class);
                spawnY = benchObj.getProperties().get("y", Float.class);
            }
        }

        Knight knight = new Knight(spawnX, spawnY);
        controller = new GamePlayController(session);
        controller.initializeMapLayers(map);
        session.setKnight(knight);
        renderer = new GameRenderer(game, session, map, gameViewport);
        gameViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    @Override
    public void render(float delta) {
        float bgc = 0.02f * GameSettings.brightness;
        Gdx.gl.glClearColor(bgc, bgc, bgc, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.update(delta);
        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, false);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (map != null) map.dispose();
        if (renderer != null) renderer.dispose();
    }
}
