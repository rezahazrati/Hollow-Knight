package com.reza.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.controller.MenuNavigationController;
import com.reza.hollowknight.model.GameSettings;

import java.util.ArrayList;

public class SettingsMenuScreen extends ScreenAdapter {
    private final HollowGame game;
    private Stage stage;
    private FitViewport viewport;
    private MenuNavigationController navigationController;
    private TextButton.TextButtonStyle style;

    private Table scrollContainerTable;
    private ScrollPane scrollPane;
    private int savedIndexCache = 0;
    private boolean waitingForKeypress = false;
    private String targetRemapControl = "";

    public SettingsMenuScreen(HollowGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(MainMenuScreen.VIRTUAL_WIDTH, MainMenuScreen.VIRTUAL_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        style = new TextButton.TextButtonStyle();
        style.font = game.assetLoader.menuFont;
        style.fontColor = com.badlogic.gdx.graphics.Color.GRAY;
        style.overFontColor = com.badlogic.gdx.graphics.Color.WHITE;
        style.focusedFontColor = com.badlogic.gdx.graphics.Color.WHITE;

        setupGlobalKeyCatchListener();
        rebuildUI();
    }

    private void setupGlobalKeyCatchListener() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (!waitingForKeypress) return false;

                if (keycode != Input.Keys.ESCAPE) {
                    assignRemappedKey(targetRemapControl, keycode);
                }

                waitingForKeypress = false;
                navigationController.setListeningForRemap(false);
                rebuildUI();
                return true;
            }
        });
    }

    private void rebuildUI() {
        if (navigationController != null) {
            savedIndexCache = navigationController.getSelectedIndex();
        }
        stage.getActors().clear();

        scrollContainerTable = new Table();
        scrollContainerTable.top().padTop(40f).padBottom(40f);

        ArrayList<TextButton> interactiveItemsList = new ArrayList<>();

        String volumeLabel = GameSettings.getString("BGM VOL: ") + Math.round(GameSettings.bgmVolume * 100) + "%";
        TextButton bgmVolumeBtn = new TextButton(volumeLabel, style);
        bgmVolumeBtn.setUserObject((MenuNavigationController.StepSliderListener) direction -> {
            GameSettings.bgmVolume += (direction * 0.01f);
            if (GameSettings.bgmVolume < 0f) GameSettings.bgmVolume = 0f;
            if (GameSettings.bgmVolume > 1f) GameSettings.bgmVolume = 1f;
            if (game.assetLoader.titleTheme != null) {
                game.assetLoader.titleTheme.setVolume(GameSettings.bgmVolume);
            }
            GameSettings.save();
            bgmVolumeBtn.setText(GameSettings.getString("BGM VOL: ") + Math.round(GameSettings.bgmVolume * 100) + "%");
        });
        scrollContainerTable.add(bgmVolumeBtn).padBottom(15f).width(600f).row();
        interactiveItemsList.add(bgmVolumeBtn);

        String bgmStateStr = GameSettings.bgmEnabled ? GameSettings.getString("BGM: ON") : GameSettings.getString("BGM: OFF");
        TextButton bgmToggleBtn = new TextButton(bgmStateStr, style);
        bgmToggleBtn.setUserObject((Runnable) () -> {
            GameSettings.bgmEnabled = !GameSettings.bgmEnabled;
            if (game.assetLoader.titleTheme != null) {
                if (GameSettings.bgmEnabled) {
                    game.assetLoader.titleTheme.setVolume(GameSettings.bgmVolume);
                    if (!game.assetLoader.titleTheme.isPlaying()) game.assetLoader.titleTheme.play();
                } else {
                    game.assetLoader.titleTheme.stop();
                }
            }
            GameSettings.save();
            rebuildUI();
        });
        scrollContainerTable.add(bgmToggleBtn).padBottom(15f).width(600f).row();
        interactiveItemsList.add(bgmToggleBtn);

        String sfxStateStr = GameSettings.sfxEnabled ? GameSettings.getString("SFX: ON") : GameSettings.getString("SFX: OFF");
        TextButton sfxToggleBtn = new TextButton(sfxStateStr, style);
        sfxToggleBtn.setUserObject((Runnable) () -> {
            GameSettings.sfxEnabled = !GameSettings.sfxEnabled;
            GameSettings.save();
            rebuildUI();
        });
        scrollContainerTable.add(sfxToggleBtn).padBottom(15f).width(600f).row();
        interactiveItemsList.add(sfxToggleBtn);

        String brightLabel = GameSettings.getString("BRIGHTNESS: ") + Math.round(GameSettings.brightness * 100) + "%";
        TextButton brightnessBtn = new TextButton(brightLabel, style);
        brightnessBtn.setUserObject((MenuNavigationController.StepSliderListener) direction -> {
            GameSettings.brightness += (direction * 0.01f);
            if (GameSettings.brightness < 0.2f) GameSettings.brightness = 0.2f;
            if (GameSettings.brightness > 1.8f) GameSettings.brightness = 1.8f;
            GameSettings.save();
            brightnessBtn.setText(GameSettings.getString("BRIGHTNESS: ") + Math.round(GameSettings.brightness * 100) + "%");
        });
        scrollContainerTable.add(brightnessBtn).padBottom(15f).width(600f).row();
        interactiveItemsList.add(brightnessBtn);

        String languageLabel = GameSettings.getString("LANGUAGE: " + GameSettings.currentLanguage);
        TextButton langBtn = new TextButton(languageLabel, style);
        langBtn.setUserObject((Runnable) () -> {
            GameSettings.currentLanguage = "EN".equals(GameSettings.currentLanguage) ? "FR" : "EN";
            GameSettings.save();
            rebuildUI();
        });
        scrollContainerTable.add(langBtn).padBottom(30f).width(600f).row();
        interactiveItemsList.add(langBtn);

        buildRemapRowItem("MOVE UP", GameSettings.keyUp, scrollContainerTable, interactiveItemsList);
        buildRemapRowItem("MOVE DOWN", GameSettings.keyDown, scrollContainerTable, interactiveItemsList);
        buildRemapRowItem("MOVE LEFT", GameSettings.keyLeft, scrollContainerTable, interactiveItemsList);
        buildRemapRowItem("MOVE RIGHT", GameSettings.keyRight, scrollContainerTable, interactiveItemsList);
        buildRemapRowItem("ATTACK", GameSettings.keyAttack, scrollContainerTable, interactiveItemsList);
        buildRemapRowItem("DASH", GameSettings.keyDash, scrollContainerTable, interactiveItemsList);
        buildRemapRowItem("JUMP", GameSettings.keyJump, scrollContainerTable, interactiveItemsList);

        TextButton resetBtn = new TextButton(GameSettings.getString("RESET TO DEFAULT"), style);
        resetBtn.setUserObject((Runnable) () -> {
            GameSettings.resetToDefaults();
            if (game.assetLoader.titleTheme != null) {
                game.assetLoader.titleTheme.setVolume(GameSettings.bgmVolume);
                if (GameSettings.bgmEnabled && !game.assetLoader.titleTheme.isPlaying()) {
                    game.assetLoader.titleTheme.play();
                }
            }
            rebuildUI();
        });
        scrollContainerTable.add(resetBtn).padTop(20f).padBottom(15f).width(600f).row();
        interactiveItemsList.add(resetBtn);

        TextButton backBtn = new TextButton(GameSettings.getString("BACK TO MAIN MENU"), style);
        backBtn.setUserObject((Runnable) () -> game.setScreen(new MainMenuScreen(game)));
        scrollContainerTable.add(backBtn).width(600f);
        interactiveItemsList.add(backBtn);

        scrollPane = new ScrollPane(scrollContainerTable);
        scrollPane.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);
        stage.addActor(scrollPane);

        TextButton[] finalItemsArray = interactiveItemsList.toArray(new TextButton[0]);
        navigationController = new MenuNavigationController(game, stage, finalItemsArray);

        if (savedIndexCache < finalItemsArray.length) {
            navigationController.setSelectedIndex(savedIndexCache);
        }
    }

    private void buildRemapRowItem(final String controlKey, int activeKeycode, Table targetTable, ArrayList<TextButton> itemRegistry) {
        String descriptiveText = GameSettings.getString(controlKey) + ": " + Input.Keys.toString(activeKeycode).toUpperCase();

        if (waitingForKeypress && targetRemapControl.equals(controlKey)) {
            descriptiveText = GameSettings.getString(controlKey) + ": " + GameSettings.getString("PRESS ANY KEY...");
        }

        TextButton rowRemapButton = new TextButton(descriptiveText, style);
        rowRemapButton.setUserObject((Runnable) () -> {
            if (!waitingForKeypress) {
                waitingForKeypress = true;
                targetRemapControl = controlKey;
                navigationController.setListeningForRemap(true);
                rebuildUI();
            }
        });

        targetTable.add(rowRemapButton).padBottom(10f).width(600f).row();
        itemRegistry.add(rowRemapButton);
    }

    private void assignRemappedKey(String controlKey, int targetNewKeycode) {
        switch (controlKey) {
            case "MOVE UP" -> GameSettings.keyUp = targetNewKeycode;
            case "MOVE DOWN" -> GameSettings.keyDown = targetNewKeycode;
            case "MOVE LEFT" -> GameSettings.keyLeft = targetNewKeycode;
            case "MOVE RIGHT" -> GameSettings.keyRight = targetNewKeycode;
            case "ATTACK" -> GameSettings.keyAttack = targetNewKeycode;
            case "DASH" -> GameSettings.keyDash = targetNewKeycode;
            case "JUMP" -> GameSettings.keyJump = targetNewKeycode;
        }
        GameSettings.save();
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
        game.batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        game.batch.end();

        stage.act(delta);

        if (navigationController != null && scrollPane != null) {
            float totalItems = navigationController.getSelectedIndex();
            float scrollPercentage = totalItems / 15f;
            scrollPane.setScrollY(scrollPane.getMaxY() * scrollPercentage);
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
