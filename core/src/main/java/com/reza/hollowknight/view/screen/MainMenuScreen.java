package com.reza.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.controller.MenuNavigationController;
import com.reza.hollowknight.model.GameSettings;

public class MainMenuScreen extends ScreenAdapter {
    private final HollowGame game;
    private Stage stage;
    private FitViewport uiViewport;
    private MenuNavigationController navigationController;

    public static final float VIRTUAL_WIDTH = 1280f;
    public static final float VIRTUAL_HEIGHT = 720f;

    public MainMenuScreen(HollowGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        uiViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        stage = new Stage(uiViewport);
        Gdx.input.setInputProcessor(stage);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.assetLoader.menuFont;
        buttonStyle.fontColor = com.badlogic.gdx.graphics.Color.GRAY;
        buttonStyle.overFontColor = com.badlogic.gdx.graphics.Color.WHITE;
        buttonStyle.focusedFontColor = com.badlogic.gdx.graphics.Color.WHITE;

        Image logoImg = new Image(game.assetLoader.gameLogo);

        TextButton startBtn = new TextButton(GameSettings.getString("START GAME"), buttonStyle);
        TextButton settingsBtn = new TextButton(GameSettings.getString("SETTINGS"), buttonStyle);
        TextButton guideBtn = new TextButton(GameSettings.getString("GUIDE_MENU_TITLE"), buttonStyle);
        TextButton achievementsBtn = new TextButton(GameSettings.getString("ACHIEVEMENTS"), buttonStyle);
        TextButton quitBtn = new TextButton(GameSettings.getString("QUIT GAME"), buttonStyle);

        startBtn.setUserObject((Runnable) () -> game.setScreen(new StartMenuScreen(game)));
        settingsBtn.setUserObject((Runnable) () -> game.setScreen(new SettingsMenuScreen(game)));
        guideBtn.setUserObject((Runnable) () -> game.setScreen(new GuideMenuScreen(game)));
        achievementsBtn.setUserObject((Runnable) () -> game.setScreen(new AchievementsScreen(game)));


        quitBtn.setUserObject((Runnable) () -> Gdx.app.exit());

        rootTable.add(logoImg).padBottom(30f).padTop(10).width(900f).height(250f).row();
        rootTable.add(startBtn).width(450f).padBottom(15f).row();
        rootTable.add(settingsBtn).width(450f).padBottom(15f).row();
        rootTable.add(guideBtn).width(450f).padBottom(15f).row();
        rootTable.add(achievementsBtn).width(450f).padBottom(15f).row();
        rootTable.add(quitBtn).width(450f);

        TextButton[] items = new TextButton[]{startBtn, settingsBtn, guideBtn, achievementsBtn, quitBtn};
        navigationController = new MenuNavigationController(game, stage, items);

        if (GameSettings.bgmEnabled && game.assetLoader.titleTheme != null) {
            game.assetLoader.titleTheme.setVolume(GameSettings.bgmVolume);
            if (!game.assetLoader.titleTheme.isPlaying()) {
                game.assetLoader.titleTheme.setLooping(true);
                game.assetLoader.titleTheme.play();
            }
        } else if (game.assetLoader.titleTheme != null && game.assetLoader.titleTheme.isPlaying()) {
            game.assetLoader.titleTheme.stop();
        }
    }

    @Override
    public void render(float delta) {
        float baseColor = 0.05f * GameSettings.brightness;
        Gdx.gl.glClearColor(baseColor, baseColor, baseColor, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        game.batch.setColor(GameSettings.brightness, GameSettings.brightness, GameSettings.brightness, 1f);
        game.batch.draw(game.assetLoader.menuBackground, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        game.batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        game.batch.end();

        stage.act(delta);
        if (navigationController != null) {
            navigationController.updatePointerPositions();
        }
        stage.draw();
    }

    @Override
    public void resize(int width, int height) { uiViewport.update(width, height, true); }
    @Override
    public void hide() { Gdx.input.setInputProcessor(null); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
