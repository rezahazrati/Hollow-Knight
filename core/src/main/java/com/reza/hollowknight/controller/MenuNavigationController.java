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

public class MenuNavigationController {
    private final HollowGame game;
    private final Stage stage;
    private final Actor[] menuItems;
    private final Image leftPointer;
    private final Image rightPointer;
    private int selectedIndex = 0;
    private final Vector2 workingVector = new Vector2();

    public MenuNavigationController(HollowGame game, Stage stage, Actor[] menuItems) {
        this.game = game;
        this.stage = stage;
        this.menuItems = menuItems;

        this.leftPointer = new Image(game.assetLoader.pointerLeft);
        this.rightPointer = new Image(game.assetLoader.pointerRight);
        float sleekWidth = 30f;
        float sleekHeight = 25f;
        this.leftPointer.setSize(sleekWidth, sleekHeight);
        this.rightPointer.setSize(sleekWidth, sleekHeight);
        this.leftPointer.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
        this.rightPointer.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);

        stage.addActor(leftPointer);
        stage.addActor(rightPointer);

        setupInputHandling();
        setupMouseHoverListeners();
        updatePointerPositions();
    }

    private void setupInputHandling() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                    navigate(-1);
                    return true;
                }
                if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                    navigate(1);
                    return true;
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
                    if (pointer == -1 && selectedIndex != index) {
                        selectedIndex = index;
                        playHoverSound();
                        updatePointerPositions();
                    }
                }
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    triggerSelection();
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

        float absoluteStageX = workingVector.x;
        float absoluteStageY = workingVector.y;

        float itemCenterY = absoluteStageY + (activeItem.getHeight() / 2f);
        float leftX = absoluteStageX - leftPointer.getWidth() - 20f;
        float rightX = absoluteStageX + activeItem.getWidth() + 20f;

        leftPointer.setPosition(leftX, itemCenterY - (leftPointer.getHeight() / 2f));
        rightPointer.setPosition(rightX, itemCenterY - (rightPointer.getHeight() / 2f));
    }

    public void triggerSelection() {
        if (game.assetLoader.buttonClick != null) {
            game.assetLoader.buttonClick.stop();
            game.assetLoader.buttonClick.play();
        }
        if (menuItems[selectedIndex].getUserObject() != null) {
            ((Runnable) menuItems[selectedIndex].getUserObject()).run();
        }
    }

    private void playHoverSound() {
        if (game.assetLoader.buttonHover != null) {
            game.assetLoader.buttonHover.stop();
            game.assetLoader.buttonHover.play();
        }
    }

    public int getSelectedIndex() { return selectedIndex; }
}
