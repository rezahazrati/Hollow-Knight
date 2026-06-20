package com.reza.hollowknight.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.model.GameSettings;

public class MenuNavigationController {
    private final HollowGame game;
    private final Stage stage;
    private final Actor[] menuItems;
    private final Image leftPointer;
    private final Image rightPointer;
    private int selectedIndex = 0;
    private final Vector2 workingVector = new Vector2();
    private boolean listeningForRemap = false;

    public interface StepSliderListener {
        void onStepChanged(int direction);
    }

    public MenuNavigationController(HollowGame game, Stage stage, Actor[] menuItems) {
        this.game = game;
        this.stage = stage;
        this.menuItems = menuItems;

        this.leftPointer = new Image(game.assetLoader.pointerLeft);
        this.rightPointer = new Image(game.assetLoader.pointerRight);
        this.leftPointer.setSize(30f, 25f);
        this.rightPointer.setSize(30f, 25f);
        this.leftPointer.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
        this.rightPointer.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);

        stage.addActor(leftPointer);
        stage.addActor(rightPointer);

        setupInputHandling();
        setupMouseHoverListeners();
        updatePointerPositions();
    }

    public void setListeningForRemap(boolean processing) {
        this.listeningForRemap = processing;
    }

    private void setupInputHandling() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (listeningForRemap) return false;

                if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                    navigate(-1);
                    return true;
                }
                if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                    navigate(1);
                    return true;
                }

                if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
                    Actor active = menuItems[selectedIndex];
                    if (active.getUserObject() instanceof StepSliderListener) {
                        ((StepSliderListener) active.getUserObject()).onStepChanged(-1);
                        return true;
                    }
                }
                if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
                    Actor active = menuItems[selectedIndex];
                    if (active.getUserObject() instanceof StepSliderListener) {
                        ((StepSliderListener) active.getUserObject()).onStepChanged(1);
                        return true;
                    }
                }

                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.NUMPAD_ENTER) {
                    triggerSelection();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupMouseHoverListeners() {
        for (int i = 0; i < menuItems.length; i++) {
            final int index = i;
            menuItems[i].addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (!listeningForRemap && pointer == -1 && selectedIndex != index) {
                        selectedIndex = index;
                        playHoverSound();
                        updatePointerPositions();
                    }
                }
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!listeningForRemap) triggerSelection();
                }
            });
        }
    }

    private void navigate(int direction) {
        selectedIndex += direction;
        if (selectedIndex < 0) selectedIndex = menuItems.length - 1;
        else if (selectedIndex >= menuItems.length) selectedIndex = 0;
        playHoverSound();
        updatePointerPositions();
    }

    public void updatePointerPositions() {
        if (menuItems == null || menuItems.length == 0) return;
        Actor activeItem = menuItems[selectedIndex];
        stage.setKeyboardFocus(activeItem);
        workingVector.set(0, 0);
        activeItem.localToStageCoordinates(workingVector);

        float itemCenterY = workingVector.y + (activeItem.getHeight() / 2f);
        leftPointer.setPosition(workingVector.x - leftPointer.getWidth() - 20f, itemCenterY - (leftPointer.getHeight() / 2f));
        rightPointer.setPosition(workingVector.x + activeItem.getWidth() + 20f, itemCenterY - (rightPointer.getHeight() / 2f));
    }

    public void triggerSelection() {
        if (GameSettings.sfxEnabled && game.assetLoader.buttonClick != null) {
            game.assetLoader.buttonClick.stop();
            game.assetLoader.buttonClick.play();
        }
        if (menuItems[selectedIndex].getUserObject() instanceof Runnable) {
            ((Runnable) menuItems[selectedIndex].getUserObject()).run();
        }
    }

    private void playHoverSound() {
        if (GameSettings.sfxEnabled && game.assetLoader.buttonHover != null) {
            game.assetLoader.buttonHover.stop();
            game.assetLoader.buttonHover.play();
        }
    }

    public int getSelectedIndex() { return selectedIndex; }
    public void setSelectedIndex(int idx) { this.selectedIndex = idx; updatePointerPositions(); }
}
