package com.reza.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.reza.hollowknight.HollowGame;

public class MainMenuScreen extends ScreenAdapter {
    private final HollowGame game;
    private Stage stage;
    private TextureRegionDrawable buttonHoverBackground;
    public static final float VIRTUAL_WIDTH = 1280f;
    public static final float VIRTUAL_HEIGHT = 720f;
    private Viewport uiViewport;

    private TextButton[] menuButtons;
    private int selectedIndex = 0;

    public MainMenuScreen(HollowGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        uiViewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        stage = new Stage(uiViewport);
        Gdx.input.setInputProcessor(stage);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1f, 1f, 1f, 0.15f));
        pixmap.fill();
        buttonHoverBackground = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.assetLoader.menuFont;
        buttonStyle.fontColor = Color.GRAY;

        buttonStyle.overFontColor = Color.WHITE;
        buttonStyle.over = buttonHoverBackground;

        buttonStyle.focusedFontColor = Color.WHITE;
        buttonStyle.focused = buttonHoverBackground;

        Image logoImg = new Image(game.assetLoader.gameLogo);

        TextButton startBtn = new TextButton("START GAME", buttonStyle);
        TextButton settingsBtn = new TextButton("SETTINGS", buttonStyle);
        TextButton guideBtn = new TextButton("GUIDE", buttonStyle);
        TextButton achievementBtn = new TextButton("ACHIEVEMENTS", buttonStyle);
        TextButton quitBtn = new TextButton("QUIT GAME", buttonStyle);

        menuButtons = new TextButton[]{startBtn, settingsBtn, guideBtn, achievementBtn, quitBtn};

        addButtonListeners(startBtn, 0, () -> game.setScreen(new StartMenuScreen()));
        addButtonListeners(settingsBtn, 1, () -> game.setScreen(new SettingsMenuScreen()));
        addButtonListeners(guideBtn, 2, () -> game.setScreen(new GuideScreen()));
        addButtonListeners(achievementBtn, 3, () -> game.setScreen(new AchievementsScreen()));
        addButtonListeners(quitBtn, 4, () -> Gdx.app.exit());

        table.add(logoImg).padBottom(40f).row();
        table.add(startBtn).width(300f).padBottom(15f).row();
        table.add(settingsBtn).width(300f).padBottom(15f).row();
        table.add(guideBtn).width(300f).padBottom(15f).row();
        table.add(achievementBtn).width(300f).padBottom(15f).row();
        table.add(quitBtn).width(300f);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                    selectedIndex--;
                    if (selectedIndex < 0) selectedIndex = menuButtons.length - 1;
                    stage.setKeyboardFocus(menuButtons[selectedIndex]);
                    playHoverSound();
                    return true;
                }
                if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                    selectedIndex++;
                    if (selectedIndex >= menuButtons.length) selectedIndex = 0;
                    stage.setKeyboardFocus(menuButtons[selectedIndex]);
                    playHoverSound();
                    return true;
                }
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.NUMPAD_ENTER) {
                    Runnable action = (Runnable) menuButtons[selectedIndex].getUserObject();
                    if (action != null) {
                        triggerButtonAction(action);
                    }
                    return true;
                }
                return false;
            }
        });

        stage.setKeyboardFocus(menuButtons[selectedIndex]);

        if (game.assetLoader.titleTheme != null) {
            game.assetLoader.titleTheme.setLooping(true);
            game.assetLoader.titleTheme.setVolume(0.5f);
            game.assetLoader.titleTheme.play();
        }
    }

    private void addButtonListeners(TextButton button, int index, Runnable clickAction) {
        button.setUserObject(clickAction);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                triggerButtonAction(clickAction);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    if (selectedIndex != index) {
                        selectedIndex = index;
                        stage.setKeyboardFocus(button);
                        playHoverSound();
                    }
                }
            }
        });
    }

    private void playHoverSound() {
        if (game.assetLoader.buttonHover != null) {
            game.assetLoader.buttonHover.setVolume(0.35f);
            game.assetLoader.buttonHover.stop();
            game.assetLoader.buttonHover.play();
        }
    }

    private void triggerButtonAction(Runnable action) {
        if (game.assetLoader.buttonClick != null) {
            game.assetLoader.buttonClick.setVolume(0.5f);
            game.assetLoader.buttonClick.stop();
            game.assetLoader.buttonClick.play();
        }
        action.run();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(stage.getCamera().combined);

        game.batch.begin();
        game.batch.draw(game.assetLoader.menuBackground, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
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
