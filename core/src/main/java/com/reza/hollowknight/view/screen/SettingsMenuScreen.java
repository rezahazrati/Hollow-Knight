package com.reza.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.controller.MenuNavigationController;

public class SettingsMenuScreen extends ScreenAdapter {
    private final HollowGame game;
    private Stage stage;
    private FitViewport viewport;
    private MenuNavigationController navigationController;

    public SettingsMenuScreen(HollowGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(MainMenuScreen.VIRTUAL_WIDTH, MainMenuScreen.VIRTUAL_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.assetLoader.menuFont;

        TextButton audioBtn = new TextButton("AUDIO OPTIONS", style);
        audioBtn.setUserObject((Runnable) () -> System.out.println("Open Audio Settings Panel"));

        TextButton videoBtn = new TextButton("VIDEO OPTIONS", style);
        videoBtn.setUserObject((Runnable) () -> System.out.println("Open Video Settings Panel"));

        TextButton backBtn = new TextButton("BACK TO MAIN MENU", style);
        backBtn.setUserObject((Runnable) () -> game.setScreen(new MainMenuScreen(game)));

        table.add(audioBtn).padBottom(20f).row();
        table.add(videoBtn).padBottom(20f).row();
        table.add(backBtn);

        TextButton[] items = new TextButton[]{ audioBtn, videoBtn, backBtn };
        navigationController = new MenuNavigationController(game, stage, items);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        game.batch.draw(game.assetLoader.menuBackground, 0, 0, MainMenuScreen.VIRTUAL_WIDTH, MainMenuScreen.VIRTUAL_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override
    public void hide() { Gdx.input.setInputProcessor(null); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
