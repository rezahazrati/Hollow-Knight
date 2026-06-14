package com.reza.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.controller.MenuNavigationController;
import com.reza.hollowknight.model.GameSettings;

import java.util.ArrayList;

public class GuideMenuScreen extends ScreenAdapter {
    private final HollowGame game;
    private Stage stage;
    private FitViewport viewport;
    private MenuNavigationController navigationController;

    private TextButton.TextButtonStyle styleNormal;
    private Label.LabelStyle titleStyle;
    private Label.LabelStyle descBoxStyle;

    private ScrollPane leftScroll;
    private Label rightInfoLabel;

    private final ArrayList<String> indexDescriptionKeys = new ArrayList<>();
    private int currentTrackingIndex = -1;

    public GuideMenuScreen(HollowGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(MainMenuScreen.VIRTUAL_WIDTH, MainMenuScreen.VIRTUAL_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        styleNormal = new TextButton.TextButtonStyle();
        styleNormal.font = game.assetLoader.menuFont;
        styleNormal.fontColor = com.badlogic.gdx.graphics.Color.GRAY;
        styleNormal.overFontColor = com.badlogic.gdx.graphics.Color.WHITE;
        styleNormal.focusedFontColor = com.badlogic.gdx.graphics.Color.WHITE;

        titleStyle = new Label.LabelStyle(game.assetLoader.menuFont, com.badlogic.gdx.graphics.Color.GOLDENROD);

        descBoxStyle = new Label.LabelStyle(game.assetLoader.menuFont, com.badlogic.gdx.graphics.Color.LIGHT_GRAY);

        buildScreenLayout();
    }

    private void buildScreenLayout() {
        stage.clear();
        indexDescriptionKeys.clear();

        Table rootGrid = new Table();
        rootGrid.setFillParent(true);
        rootGrid.center().pad(30f);
        stage.addActor(rootGrid);

        Label mainTitle = new Label("=== " + GameSettings.getString("GUIDE_MENU_TITLE") + " ===", titleStyle);
        rootGrid.add(mainTitle).colspan(2).padBottom(30f).row();

        Table leftScrollContainer = new Table();
        leftScrollContainer.top().left();
        ArrayList<TextButton> listRegistry = new ArrayList<>();

        appendGuideItem("LBL_MOVE_UP", "DESC_MOVE_UP", Input.Keys.toString(GameSettings.keyUp), leftScrollContainer, listRegistry);
        appendGuideItem("LBL_MOVE_DOWN", "DESC_MOVE_DOWN", Input.Keys.toString(GameSettings.keyDown), leftScrollContainer, listRegistry);
        appendGuideItem("LBL_MOVE_LEFT", "DESC_MOVE_LEFT", Input.Keys.toString(GameSettings.keyLeft), leftScrollContainer, listRegistry);
        appendGuideItem("LBL_MOVE_RIGHT", "DESC_MOVE_RIGHT", Input.Keys.toString(GameSettings.keyRight), leftScrollContainer, listRegistry);
        appendGuideItem("LBL_ATTACK", "DESC_ATTACK", Input.Keys.toString(GameSettings.keyAttack), leftScrollContainer, listRegistry);
        appendGuideItem("LBL_DASH", "DESC_DASH", Input.Keys.toString(GameSettings.keyDash), leftScrollContainer, listRegistry);
        appendGuideItem("LBL_JUMP", "DESC_JUMP", Input.Keys.toString(GameSettings.keyJump), leftScrollContainer, listRegistry);


        appendGuideItem("LBL_HEALTH", "DESC_HEALTH", "", leftScrollContainer, listRegistry);
        appendGuideItem("LBL_SOUL", "DESC_SOUL", "", leftScrollContainer, listRegistry);

        appendGuideItem("LBL_CHEAT_TELEPORT", "DESC_CHEAT_TELEPORT", "", leftScrollContainer, listRegistry);
        appendGuideItem("LBL_CHEAT_NOCLIP", "DESC_CHEAT_NOCLIP", "", leftScrollContainer, listRegistry);
        appendGuideItem("LBL_CHEAT_HEAL", "DESC_CHEAT_HEAL", "", leftScrollContainer, listRegistry);
        appendGuideItem("LBL_CHEAT_SOUL", "DESC_CHEAT_SOUL", "", leftScrollContainer, listRegistry);
        appendGuideItem("LBL_CHEAT_GOD", "DESC_CHEAT_GOD", "", leftScrollContainer, listRegistry);


        TextButton backMenuBtn = new TextButton("[ " + GameSettings.getString("GUIDE_BACK") + " ]", styleNormal);
        backMenuBtn.setUserObject((Runnable) () -> game.setScreen(new MainMenuScreen(game)));
        leftScrollContainer.add(backMenuBtn).padTop(25f).left().row();
        listRegistry.add(backMenuBtn);
        indexDescriptionKeys.add("GUIDE_BACK");

        leftScroll = new ScrollPane(leftScrollContainer);
        leftScroll.setScrollingDisabled(true, false);
        leftScroll.setFadeScrollBars(false);

        Table rightColumnBox = new Table();
        rightColumnBox.top().left().pad(20f);

        Label infoHeader = new Label("--- " + GameSettings.getString("DESCRIPTION_HEADER") + " ---", titleStyle);
        rightColumnBox.add(infoHeader).padBottom(15f).left().row();

        rightInfoLabel = new Label("", descBoxStyle);
        rightInfoLabel.setWrap(true);
        rightColumnBox.add(rightInfoLabel).width(450f).top().left();

        rootGrid.add(leftScroll).width(650f).height(450f).top();
        rootGrid.add(rightColumnBox).width(500f).height(450f).top();

        TextButton[] itemArray = listRegistry.toArray(new TextButton[0]);
        navigationController = new MenuNavigationController(game, stage, itemArray);
        navigationController.setSelectedIndex(0);
    }

    private void appendGuideItem(String labelKey, String contentDescKey, String trailingVal, Table container, ArrayList<TextButton> componentList) {
        String baseLabel = GameSettings.getString(labelKey);
        if (!trailingVal.isEmpty()) {
            baseLabel += " ( " + trailingVal.toUpperCase() + " )";
        }

        TextButton itemBtn = new TextButton(baseLabel, styleNormal);

        itemBtn.setUserObject((Runnable) () -> {});

        container.add(itemBtn).padBottom(12f).left().row();
        componentList.add(itemBtn);
        indexDescriptionKeys.add(contentDescKey);
    }

    @Override
    public void render(float delta) {
        float backgroundBrightness = 0.05f * GameSettings.brightness;
        Gdx.gl.glClearColor(backgroundBrightness, backgroundBrightness, backgroundBrightness, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        game.batch.setColor(GameSettings.brightness, GameSettings.brightness, GameSettings.brightness, 1f);
        game.batch.draw(game.assetLoader.menuBackground, 0, 0, MainMenuScreen.VIRTUAL_WIDTH, MainMenuScreen.VIRTUAL_HEIGHT);
        game.batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        game.batch.end();

        stage.act(delta);

        if (navigationController != null) {
            int selectedIdx = navigationController.getSelectedIndex();
            if (selectedIdx != currentTrackingIndex && selectedIdx < indexDescriptionKeys.size()) {
                currentTrackingIndex = selectedIdx;
                String activeKey = indexDescriptionKeys.get(selectedIdx);

                if ("GUIDE_BACK".equals(activeKey)) {
                    rightInfoLabel.setText(GameSettings.getString("BACK TO MAIN MENU"));
                } else {
                    rightInfoLabel.setText(GameSettings.getString(activeKey));
                }

                float scrollPercentage = (float) selectedIdx / (float) indexDescriptionKeys.size();
                leftScroll.setScrollY(leftScroll.getMaxY() * scrollPercentage);
            }
        }

        stage.draw();
    }

    @Override
    public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override
    public void hide() { Gdx.input.setInputProcessor(null); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
