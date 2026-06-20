package com.reza.hollowknight.view.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.model.GameSession;
import com.reza.hollowknight.model.entities.Knight;
import com.reza.hollowknight.view.audio.AudioManager;

public class GameRenderer implements Disposable {
    private final HollowGame game;
    private final GameSession session;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final KnightView knightView;

    private float currentZoom = 2.5f;
    private boolean firstFrame = true;

    public GameRenderer(HollowGame game, GameSession session, TiledMap map, Viewport viewport) {
        this.game = game;
        this.session = session;
        this.viewport = viewport;
        this.camera = (OrthographicCamera) viewport.getCamera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, 1.0f);
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.knightView = new KnightView();
    }

    public void render(float delta) {
        Knight knight = session.getKnight();
        if (knight == null) return;

        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            currentZoom = Math.min(5.0f, currentZoom + delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
            currentZoom = Math.max(0.1f, currentZoom - delta);
        }
        camera.zoom = currentZoom;

        float cameraOffsetY = 120.0f;

        if (firstFrame) {
            camera.position.x = knight.getPosition().x;
            camera.position.y = knight.getPosition().y + cameraOffsetY;
            firstFrame = false;
        } else {
            float lerpFactor = 0.1f;
            camera.position.x += (knight.getPosition().x - camera.position.x) * lerpFactor;

            float targetCameraY = knight.getPosition().y + cameraOffsetY;
            camera.position.y += (targetCameraY - camera.position.y) * lerpFactor;
        }

        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();
        AudioManager.getInstance().updateStateAudio(knight.getCurrentState());
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        knightView.draw(batch, knight, delta);
        batch.end();
    }

    @Override
    public void dispose() {
        if (mapRenderer != null) mapRenderer.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (knightView != null) knightView.dispose();
        AudioManager.getInstance().dispose();
        batch.dispose();
    }
}
