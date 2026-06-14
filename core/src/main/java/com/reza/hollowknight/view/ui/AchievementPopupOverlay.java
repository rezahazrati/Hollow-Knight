package com.reza.hollowknight.view.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.core.event.AchievementManager;
import com.reza.hollowknight.model.GameSettings;
import com.reza.hollowknight.view.screen.MainMenuScreen;

public class AchievementPopupOverlay implements AchievementManager.AchievementPopupNotify {
    private final HollowGame game;
    private Stage activeStage;
    private Table containerTable;

    public AchievementPopupOverlay(HollowGame game) {
        this.game = game;
        AchievementManager.getInstance().setNotificationListener(this);
    }

    public void updateActiveStage(Stage newStage) {
        this.activeStage = newStage;
    }

    @Override
    public void triggerPopup(String titleKey, String descKey) {

        if (containerTable != null) containerTable.remove();

        containerTable = new Table();
        containerTable.center();

        containerTable.setBackground(activeStage.getBatch().getColor() != null ?
            containerTable.getSkin() != null ? containerTable.getSkin().getDrawable("white") : null : null);
        containerTable.setColor(0.12f, 0.12f, 0.14f, 0.92f);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.assetLoader.menuFont, Color.GOLDENROD);
        Label.LabelStyle contentStyle = new Label.LabelStyle(game.assetLoader.menuFont, Color.WHITE);

        Label banner = new Label("🏆 " + GameSettings.getString("ACH_UNLOCKED_BANNER") + "! 🏆", titleStyle);
        Label title = new Label(GameSettings.getString(titleKey), contentStyle);
        Label desc = new Label(GameSettings.getString(descKey), contentStyle);
        desc.setFontScale(0.8f);

        containerTable.add(banner).padBottom(4f).row();
        containerTable.add(title).padBottom(2f).row();
        containerTable.add(desc);

        containerTable.setSize(450f, 100f);
        containerTable.setPosition((MainMenuScreen.VIRTUAL_WIDTH - 450f) / 2f, -120f);
        activeStage.addActor(containerTable);

        containerTable.addAction(Actions.sequence(
            Actions.moveTo((MainMenuScreen.VIRTUAL_WIDTH - 450f) / 2f, 25f, 0.4f, com.badlogic.gdx.math.Interpolation.pow3Out),
            Actions.delay(3.0f),
            Actions.parallel(
                Actions.moveBy(0, -150f, 0.5f, com.badlogic.gdx.math.Interpolation.pow3In),
                Actions.fadeOut(0.5f)
            ),
            Actions.removeActor()
        ));
    }
}
