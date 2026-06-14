package com.reza.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.controller.MenuNavigationController;
import com.reza.hollowknight.core.event.AchievementManager;
import com.reza.hollowknight.model.GameSettings;

public class AchievementsScreen extends ScreenAdapter {
    private final HollowGame game;
    private Stage stage;
    private FitViewport viewport;
    private MenuNavigationController navigationController;

    private TextButton.TextButtonStyle buttonStyle;
    private Label.LabelStyle goldHeaderStyle;
    private Label.LabelStyle bodyStyle;

    public AchievementsScreen(HollowGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(MainMenuScreen.VIRTUAL_WIDTH, MainMenuScreen.VIRTUAL_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        game.achievementPopupOverlay.updateActiveStage(stage);

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.assetLoader.menuFont;
        buttonStyle.fontColor = Color.GRAY;
        buttonStyle.overFontColor = Color.WHITE;
        buttonStyle.focusedFontColor = Color.WHITE;

        goldHeaderStyle = new Label.LabelStyle(game.assetLoader.menuFont, Color.GOLDENROD);
        bodyStyle = new Label.LabelStyle(game.assetLoader.menuFont, Color.LIGHT_GRAY);

        buildScreenLayout();
    }

    private void buildScreenLayout() {
        Table root = new Table();
        root.setFillParent(true);
        root.center().pad(40f);
        stage.addActor(root);

        Label screenTitle = new Label("=== " + GameSettings.getString("ACHIEVEMENTS") + " ===", goldHeaderStyle);
        root.add(screenTitle).padBottom(20f).row();

        Table scrollContentTable = new Table();
        scrollContentTable.top().left();

        appendAchievementDisplayRow("ACH_COMPLETION", scrollContentTable);
        appendAchievementDisplayRow("ACH_SPEEDRUN", scrollContentTable);
        appendAchievementDisplayRow("ACH_TRUE_HUNTER", scrollContentTable);
        appendAchievementDisplayRow("ACH_DEFEAT_FALSE_KNIGHT", scrollContentTable);
        appendAchievementDisplayRow("ACH_GENOCIDE", scrollContentTable);
        appendAchievementDisplayRow("ACH_GEO_HOARDER", scrollContentTable);

        ScrollPane pane = new ScrollPane(scrollContentTable);
        pane.setScrollingDisabled(true, false);
        root.add(pane).width(900f).height(400f).row();
        TextButton backBtn = new TextButton("[ " + GameSettings.getString("BACK") + " ]", buttonStyle);
        backBtn.setUserObject((Runnable) () -> game.setScreen(new MainMenuScreen(game)));
        root.add(backBtn).padTop(20f);

        navigationController = new MenuNavigationController(game, stage, new TextButton[]{backBtn});
        navigationController.setSelectedIndex(0);
    }

    private void appendAchievementDisplayRow(String id, Table scrollContentTable) {
        boolean unlocked = AchievementManager.getInstance().isUnlocked(id);

        Table rowItemTable = new Table();
        rowItemTable.left().pad(10f);

        Color iconColor;
        Color titleColor;
        Color descColor;

        if (unlocked) {
            iconColor = Color.GOLD;
            titleColor = Color.GOLDENROD;
            descColor = Color.WHITE;
        } else {
            iconColor = new Color(0.4f, 0.4f, 0.4f, 1f);
            titleColor = Color.LIGHT_GRAY;
            descColor = new Color(0.55f, 0.55f, 0.58f, 1f);
        }

        Label statusIcon = new Label(unlocked ? "🏆" : "🔒", goldHeaderStyle);
        statusIcon.setColor(iconColor);

        Label nameLabel = new Label(GameSettings.getString(id + "_TITLE"), goldHeaderStyle);
        nameLabel.setColor(titleColor);

        Label descriptionLabel = new Label(GameSettings.getString(id + "_DESC"), bodyStyle);
        descriptionLabel.setColor(descColor);
        descriptionLabel.setFontScale(0.85f);

        rowItemTable.add(statusIcon).padRight(20f);
        Table textStack = new Table();
        textStack.left();
        textStack.add(nameLabel).left().row();
        textStack.add(descriptionLabel).left();

        rowItemTable.add(textStack).left();
        scrollContentTable.add(rowItemTable).width(850f).padBottom(15f).left().row();
    }

    @Override
    public void render(float delta) {
        float baseColor = 0.05f * GameSettings.brightness;
        Gdx.gl.glClearColor(baseColor, baseColor, baseColor, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        game.batch.setColor(GameSettings.brightness, GameSettings.brightness, GameSettings.brightness, 1f);
        game.batch.draw(game.assetLoader.menuBackground, 0, 0, MainMenuScreen.VIRTUAL_WIDTH, MainMenuScreen.VIRTUAL_HEIGHT);
        game.batch.setColor(Color.WHITE);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
    }
}
