package com.reza.hollowknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.reza.hollowknight.assets.AssetLoader;
import com.reza.hollowknight.view.screen.MainMenuScreen;
import games.rednblack.miniaudio.MiniAudio;

public class HollowGame extends Game {
    public SpriteBatch batch;
    public AssetLoader assetLoader;
    public MiniAudio miniAudio;

    @Override
    public void create() {
        batch = new SpriteBatch();
        miniAudio = new MiniAudio();

        assetLoader = new AssetLoader(this);
        assetLoader.loadMenuAssets();

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        float safeDelta = Math.min(delta, 1f / 30f);

        if (screen != null) {
            screen.render(safeDelta);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        assetLoader.dispose();

        if (miniAudio != null) {
            miniAudio.dispose();
        }
    }
}
