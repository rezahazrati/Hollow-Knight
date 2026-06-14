package com.reza.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.controller.MenuNavigationController;
import com.reza.hollowknight.model.GameSettings;

import java.util.ArrayList;

public class SettingsMenuScreen extends ScreenAdapter {
    // note: there are some problems and inconsistencies in toggling the language and SFXs and BGM
    // also the brightness should work like the volume 1 percent by 1 percent
    private final HollowGame game;
    private Stage stage;
    private FitViewport viewport;
    private MenuNavigationController navigationController;
    private TextButton.TextButtonStyle buttonStyle;
    private Label.LabelStyle headerStyle;

    private static int savedIndexCache = 0;
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

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.assetLoader.menuFont;
        buttonStyle.fontColor = com.badlogic.gdx.graphics.Color.GRAY;
        buttonStyle.overFontColor = com.badlogic.gdx.graphics.Color.WHITE;
        buttonStyle.focusedFontColor = com.badlogic.gdx.graphics.Color.WHITE;

        headerStyle = new Label.LabelStyle(game.assetLoader.menuFont, com.badlogic.gdx.graphics.Color.GOLDENROD);

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

        Table rootMasterTable = new Table();
        rootMasterTable.setFillParent(true);
        rootMasterTable.center();
        stage.addActor(rootMasterTable);

        ArrayList<TextButton> interactiveItemsList = new ArrayList<>();
        Table leftColumnTable = new Table();
        leftColumnTable.top().left();

        Label globalHeader = new Label(GameSettings.getString("SETTINGS"), headerStyle);
        leftColumnTable.add(globalHeader).padBottom(30f).row();

        String volumeLabel = GameSettings.getString("BGM VOL: ") + Math.round(GameSettings.bgmVolume * 100) + "%";
        TextButton bgmVolumeBtn = new TextButton(volumeLabel, buttonStyle);
        bgmVolumeBtn.setUserObject((MenuNavigationController.StepSliderListener) direction -> {
            GameSettings.bgmVolume = Math.min(1f, Math.max(0f, GameSettings.bgmVolume + (direction * 0.01f)));
            if (game.assetLoader.titleTheme != null) game.assetLoader.titleTheme.setVolume(GameSettings.bgmVolume);
            GameSettings.save();
            bgmVolumeBtn.setText(GameSettings.getString("BGM VOL: ") + Math.round(GameSettings.bgmVolume * 100) + "%");
        });
        leftColumnTable.add(bgmVolumeBtn).padBottom(20f).left().row();
        interactiveItemsList.add(bgmVolumeBtn);

        String bgmStateStr = GameSettings.bgmEnabled ? GameSettings.getString("BGM: ON") : GameSettings.getString("BGM: OFF");
        TextButton bgmToggleBtn = new TextButton(bgmStateStr, buttonStyle);
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
        leftColumnTable.add(bgmToggleBtn).padBottom(20f).left().row();
        interactiveItemsList.add(bgmToggleBtn);

        String sfxStateStr = GameSettings.sfxEnabled ? GameSettings.getString("SFX: ON") : GameSettings.getString("SFX: OFF");
        TextButton sfxToggleBtn = new TextButton(sfxStateStr, buttonStyle);
        sfxToggleBtn.setUserObject((Runnable) () -> {
            GameSettings.sfxEnabled = !GameSettings.sfxEnabled;
            GameSettings.save();
            rebuildUI();
        });
        leftColumnTable.add(sfxToggleBtn).padBottom(20f).left().row();
        interactiveItemsList.add(sfxToggleBtn);

        String brightLabel = GameSettings.getString("BRIGHTNESS: ") + Math.round(GameSettings.brightness * 100) + "%";
        TextButton brightnessBtn = new TextButton(brightLabel, buttonStyle);
        brightnessBtn.setUserObject((MenuNavigationController.StepSliderListener) direction -> {
            GameSettings.brightness = Math.clamp(GameSettings.brightness + (direction * 0.01f), 0.2f, 1.8f);
            GameSettings.save();
            brightnessBtn.setText(GameSettings.getString("BRIGHTNESS: ") + Math.round(GameSettings.brightness * 100) + "%");
        });
        leftColumnTable.add(brightnessBtn).padBottom(20f).left().row();
        interactiveItemsList.add(brightnessBtn);

        String languageLabel = GameSettings.getString("LANGUAGE: " + GameSettings.currentLanguage);
        TextButton langBtn = new TextButton(languageLabel, buttonStyle);
        langBtn.setUserObject((Runnable) () -> {
            GameSettings.currentLanguage = "EN".equals(GameSettings.currentLanguage) ? "FR" : "EN";
            GameSettings.save();

            game.setScreen(new SettingsMenuScreen(game));
        });
        leftColumnTable.add(langBtn).padBottom(20f).left().row();
        interactiveItemsList.add(langBtn);


        Table rightColumnTable = new Table();
        rightColumnTable.top().left();

        Label controllerHeader = new Label("CONTROLS", headerStyle);
        rightColumnTable.add(controllerHeader).padBottom(30f).row();

        buildRemapRowItem("MOVE UP", GameSettings.keyUp, rightColumnTable, interactiveItemsList);
        buildRemapRowItem("MOVE DOWN", GameSettings.keyDown, rightColumnTable, interactiveItemsList);
        buildRemapRowItem("MOVE LEFT", GameSettings.keyLeft, rightColumnTable, interactiveItemsList);
        buildRemapRowItem("MOVE RIGHT", GameSettings.keyRight, rightColumnTable, interactiveItemsList);
        buildRemapRowItem("ATTACK", GameSettings.keyAttack, rightColumnTable, interactiveItemsList);
        buildRemapRowItem("DASH", GameSettings.keyDash, rightColumnTable, interactiveItemsList);
        buildRemapRowItem("JUMP", GameSettings.keyJump, rightColumnTable, interactiveItemsList);



        rootMasterTable.add(leftColumnTable).top().padRight(120f);
        rootMasterTable.add(rightColumnTable).top().row();

        Table footerTable = new Table();
        footerTable.center();

        TextButton resetBtn = new TextButton(GameSettings.getString("RESET TO DEFAULT"), buttonStyle);
        resetBtn.setUserObject((Runnable) () -> {
            GameSettings.resetToDefaults();
            if (game.assetLoader.titleTheme != null) {
                game.assetLoader.titleTheme.setVolume(GameSettings.bgmVolume);
                if (GameSettings.bgmEnabled && !game.assetLoader.titleTheme.isPlaying()) game.assetLoader.titleTheme.play();
            }
            game.setScreen(new SettingsMenuScreen(game));
        });
        footerTable.add(resetBtn).padTop(40f).padBottom(15f).row();
        interactiveItemsList.add(resetBtn);

        TextButton backBtn = new TextButton(GameSettings.getString("BACK TO MAIN MENU"), buttonStyle);
        backBtn.setUserObject((Runnable) () -> {
            savedIndexCache = 0;
            game.setScreen(new MainMenuScreen(game));
        });
        footerTable.add(backBtn);
        interactiveItemsList.add(backBtn);

        rootMasterTable.add(footerTable).colspan(2).center();

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

        TextButton rowRemapButton = new TextButton(descriptiveText, buttonStyle);
        rowRemapButton.setUserObject((Runnable) () -> {
            if (!waitingForKeypress) {
                waitingForKeypress = true;
                targetRemapControl = controlKey;
                navigationController.setListeningForRemap(true);
                rebuildUI();
            }
        });

        targetTable.add(rowRemapButton).padBottom(12f).left().row();
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
        stage.draw();
    }

    @Override
    public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override
    public void hide() { Gdx.input.setInputProcessor(null); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
