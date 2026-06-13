package com.reza.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.controller.MenuNavigationController;
import com.reza.hollowknight.model.SaveProfile;

public class StartMenuScreen extends ScreenAdapter {
    private final HollowGame game;
    private Stage stage;
    private FitViewport uiViewport;
    private MenuNavigationController navigationController;
    private final SaveProfile[] loadedProfiles = new SaveProfile[4];

    public StartMenuScreen(HollowGame game) {
        this.game = game;
        this.game.assetLoader.loadProfileMenuAssets();
    }

    @Override
    public void show() {
        uiViewport = new FitViewport(MainMenuScreen.VIRTUAL_WIDTH, MainMenuScreen.VIRTUAL_HEIGHT);
        stage = new Stage(uiViewport);
        Gdx.input.setInputProcessor(stage);

        loadProfiles();
        buildUi();
        setupInputListeners();
    }

    private void loadProfiles() {
        com.badlogic.gdx.utils.Json gdxJson = new com.badlogic.gdx.utils.Json();
        gdxJson.setIgnoreUnknownFields(true);

        for (int i = 0; i < 4; i++) {
            String filename = "slot_" + (i + 1) + ".json";
            String relativePath = "saves/" + filename;
            String clearedMarkerPath = relativePath + ".cleared";

            loadedProfiles[i] = null;

            if (Gdx.files.local(clearedMarkerPath).exists()) {
                continue;
            }

            if (Gdx.files.local(relativePath).exists()) {
                try {
                    loadedProfiles[i] = gdxJson.fromJson(SaveProfile.class, Gdx.files.local(relativePath));
                } catch (Exception e) {
                    System.out.println(e.getMessage()+"\n"+"Problem loading profiles from local path");
                }
            } else if (Gdx.files.internal(relativePath).exists()) {
                try {
                    loadedProfiles[i] = gdxJson.fromJson(SaveProfile.class, Gdx.files.internal(relativePath));
                } catch (Exception e) {
                    System.out.println(e.getMessage() +"\n"+"Problem loading profiles from internal path");
                }
            }
        }
    }

    private void buildUi() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.assetLoader.menuFont, com.badlogic.gdx.graphics.Color.WHITE);
        Label titleLabel = new Label("SELECT PROFILE", titleStyle);
        rootTable.add(titleLabel).padBottom(25f).row();

        Table slot1 = buildSlotRowCard(0);
        Table slot2 = buildSlotRowCard(1);
        Table slot3 = buildSlotRowCard(2);
        Table slot4 = buildSlotRowCard(3);

        rootTable.add(slot1).width(750f).height(110f).padBottom(10f).row();
        rootTable.add(slot2).width(750f).height(110f).padBottom(10f).row();
        rootTable.add(slot3).width(750f).height(110f).padBottom(10f).row();
        rootTable.add(slot4).width(750f).height(110f).padBottom(15f).row();

        TextButton.TextButtonStyle actionStyle = new TextButton.TextButtonStyle();
        actionStyle.font = game.assetLoader.menuFont;
        actionStyle.fontColor = com.badlogic.gdx.graphics.Color.GRAY;
        actionStyle.overFontColor = com.badlogic.gdx.graphics.Color.WHITE;
        actionStyle.focusedFontColor = com.badlogic.gdx.graphics.Color.WHITE;

        TextButton backBtn = new TextButton("BACK", actionStyle);
        backBtn.setUserObject((Runnable) () -> game.setScreen(new MainMenuScreen(game)));
        rootTable.add(backBtn).width(200f);

        Table[] items = new Table[]{slot1, slot2, slot3, slot4, backBtn};
        navigationController = new MenuNavigationController(game, stage, items);
    }

    private void setupInputListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.FORWARD_DEL || keycode == Input.Keys.DEL) {
                    int currentIdx = navigationController.getSelectedIndex();
                    if (currentIdx >= 0 && currentIdx < 4) {
                        executeSaveDeletion(currentIdx);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void executeSaveDeletion(int slotIndex) {
        if (loadedProfiles[slotIndex] == null) return;

        String filename = "slot_" + (slotIndex + 1) + ".json";
        String relativePath = "saves/" + filename;

        if (Gdx.files.local(relativePath).exists()) {
            Gdx.files.local(relativePath).delete();
        }

        if (Gdx.files.internal(relativePath).exists()) {
            Gdx.files.local(relativePath + ".cleared").writeString("true", false);
        }

        loadedProfiles[slotIndex] = null;

        if (game.assetLoader.buttonClick != null) {
            game.assetLoader.buttonClick.stop();
            game.assetLoader.buttonClick.play();
        }

        game.setScreen(new StartMenuScreen(game));
    }

    private Table buildSlotRowCard(final int index) {
        Table cardContainer = new Table();
        SaveProfile data = loadedProfiles[index];

        Image separationLine = new Image(game.assetLoader.profileLine);
        cardContainer.add(separationLine).width(750f).height(8f).colspan(3).padTop(4f).padBottom(4f).row();

        Table innerBody = new Table();
        innerBody.left();

        if (data == null) {
            Label.LabelStyle emptyStyle = new Label.LabelStyle(game.assetLoader.menuFont, com.badlogic.gdx.graphics.Color.GRAY);
            Label emptyText = new Label((index + 1) + ".   NEW GAME", emptyStyle);
            innerBody.add(emptyText).padLeft(40f).expandX().left();

            cardContainer.setUserObject((Runnable) () -> {
                String markerPath = "saves/slot_" + (index + 1) + ".json.cleared";
                if (Gdx.files.local(markerPath).exists()) {
                    Gdx.files.local(markerPath).delete();
                }
                System.out.println("Starting fresh campaign sequence on slot: " + (index + 1));
            });
        } else {
            innerBody.setBackground(new TextureRegionDrawable(game.assetLoader.getAreaBackground(data.currentScene)));

            Label.LabelStyle whiteStyle = new Label.LabelStyle(game.assetLoader.menuFont, com.badlogic.gdx.graphics.Color.WHITE);
            Label.LabelStyle detailedStyle = new Label.LabelStyle(game.assetLoader.subFont, com.badlogic.gdx.graphics.Color.LIGHT_GRAY);

            Label numLabel = new Label((index + 1) + ". ", whiteStyle);
            innerBody.add(numLabel).padLeft(25f);

            Image skullFrame = new Image(game.assetLoader.knightHead);
            innerBody.add(skullFrame).size(55f, 45f).padLeft(10f);

            Table primaryStatsStack = new Table().left();
            Table healthCluster = new Table().left();
            for (int h = 0; h < data.maxMasks; h++) {
                healthCluster.add(new Image(game.assetLoader.hpMask)).size(16f, 20f).padRight(2f);
            }
            primaryStatsStack.add(healthCluster).left().row();

            Table currencyRow = new Table().left();
            currencyRow.add(new Image(game.assetLoader.geoIcon)).size(18f, 18f).padRight(5f);
            currencyRow.add(new Label(String.valueOf(data.geo), detailedStyle));
            primaryStatsStack.add(currencyRow).left().padTop(4f);

            innerBody.add(primaryStatsStack).padLeft(25f).expandX().left();

            Table metaDataStack = new Table().right();
            String cleanAreaName = data.currentScene.replace("Scene", "")
                .replaceAll("(?<=[a-z])(?=[A-Z])", " ")
                .toUpperCase()
                .trim();

            Label areaLabel = new Label(cleanAreaName, whiteStyle);

            int activeMinutes = (int) (data.playTime / 60f);
            String temporalStr = (activeMinutes / 60) + "H " + (activeMinutes % 60) + "M";
            Label durationLabel = new Label(temporalStr, detailedStyle);

            metaDataStack.add(areaLabel).right().row();
            metaDataStack.add(durationLabel).right().padTop(6f);

            innerBody.add(metaDataStack).padRight(30f).right();

            TextButton.TextButtonStyle smallDeleteStyle = new TextButton.TextButtonStyle();
            smallDeleteStyle.font = game.assetLoader.menuFont;
            smallDeleteStyle.fontColor = com.badlogic.gdx.graphics.Color.GRAY;
            smallDeleteStyle.overFontColor = com.badlogic.gdx.graphics.Color.WHITE;

            TextButton rowClearBtn = new TextButton("CLEAR SAVE", smallDeleteStyle);
            rowClearBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    executeSaveDeletion(index);
                    event.cancel();
                }
            });
            innerBody.add(rowClearBtn).padRight(25f).right();

            cardContainer.setUserObject((Runnable) () -> {
                System.out.println("Resuming active campaign state loop on file index: " + (index + 1));
            });
        }

        cardContainer.add(innerBody).grow().colspan(3);
        return cardContainer;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.02f, 0.02f, 0.02f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        game.batch.draw(game.assetLoader.menuBackground, 0, 0, MainMenuScreen.VIRTUAL_WIDTH, MainMenuScreen.VIRTUAL_HEIGHT);
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
