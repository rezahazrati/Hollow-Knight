package com.reza.hollowknight.view.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.reza.hollowknight.model.entities.Knight;

import java.util.HashMap;

public class KnightView implements Disposable {
    private final HashMap<Knight.State, Animation<TextureRegion>> animations;
    private final Array<Texture> loadedTextures;
    private float stateTime = 0f;
    private Knight.State previousState;

    private static class StateAssetConfig {
        Knight.State state;
        String folderName;
        String filePrefix;
        int startIndex; // 0 for most animations, 1 for Idle
        int totalFrames;
        float frameDuration;
        Animation.PlayMode playMode;

        StateAssetConfig(Knight.State state, String folderName, String filePrefix, int startIndex, int totalFrames, float frameDuration, Animation.PlayMode playMode) {
            this.state = state;
            this.folderName = folderName;
            this.filePrefix = filePrefix;
            this.startIndex = startIndex;
            this.totalFrames = totalFrames;
            this.frameDuration = frameDuration;
            this.playMode = playMode;
        }
    }

    public KnightView() {
        this.animations = new HashMap<>();
        this.loadedTextures = new Array<>();
        this.previousState = Knight.State.IDLE;

        loadAllAnimations();
    }

    private void loadAllAnimations() {
        StateAssetConfig[] configs = {
            new StateAssetConfig(Knight.State.IDLE, "Idle", "Idle", 1, 8, 0.1f, Animation.PlayMode.LOOP),

            new StateAssetConfig(Knight.State.RUN, "Run", "Run", 0, 13, 0.07f, Animation.PlayMode.LOOP),
            new StateAssetConfig(Knight.State.RUN_TO_IDLE, "Run to idle", "Run To Idle", 0, 6, 0.05f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.AIRBORNE, "Airborne", "Airborne", 0, 12, 0.08f, Animation.PlayMode.LOOP),
            new StateAssetConfig(Knight.State.FALL, "Fall", "Fall", 0, 6, 0.08f, Animation.PlayMode.LOOP),
            new StateAssetConfig(Knight.State.LANDING, "landing", "Landing", 0, 4, 0.06f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.WALLJUMP, "walljump", "Walljump", 0, 9, 0.07f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.DASH, "Dash", "Dash", 0, 12, 0.04f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.DOUBLE_JUMP, "Double jump", "Double Jump", 0, 8, 0.06f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.SLASH, "Slash", "Slash", 0, 5, 0.05f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.SLASHALT, "SlashAlt", "SlashAlt", 0, 5, 0.05f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.DOWNSLASH, "DownSlash", "DownSlash", 0, 5, 0.05f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.UPSLASH, "upslash", "UpSlash", 0, 5, 0.05f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.LOOKUP, "lookup", "LookUp", 0, 6, 0.1f, Animation.PlayMode.LOOP),
            new StateAssetConfig(Knight.State.LOOKDOWN, "lookdown", "LookDown", 0, 6, 0.1f, Animation.PlayMode.LOOP),
            new StateAssetConfig(Knight.State.FOCUS_START, "Focus Start", "Focus Start", 0, 3, 0.08f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.FOCUS, "Focus", "Focus", 0, 7, 0.08f, Animation.PlayMode.LOOP),
            new StateAssetConfig(Knight.State.FOCUS_GET, "Focus Get", "Focus Get", 0, 6, 0.06f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.FOCUS_END, "Focus End", "Focus End", 0, 3, 0.06f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.FIREBALL, "Fireball", "Fireball Cast", 0, 9, 0.05f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.IDLE_HURT, "Idle Hurt", "Idle Hurt", 0, 12, 0.06f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.SCREAM, "Scream", "Scream", 0, 7, 0.07f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.DEATH, "Death", "Death", 0, 18, 0.08f, Animation.PlayMode.NORMAL),
            new StateAssetConfig(Knight.State.WALK, "Walk", "Walk", 4, 3, 0.1f, Animation.PlayMode.LOOP) // Starts at _004 based on files
        };

        for (StateAssetConfig cfg : configs) {
            Array<TextureRegion> frames = new Array<>();

            int lastFrameIndex = (cfg.startIndex + cfg.totalFrames) - 1;

            for (int i = cfg.startIndex; i <= lastFrameIndex; i++) {
                String frameIndexStr = String.format("%03d", i);
                String path = "knight-raw/" + cfg.folderName + "/" + cfg.filePrefix + "_" + frameIndexStr + ".png";

                Texture tex = new Texture(Gdx.files.internal(path));
                loadedTextures.add(tex);
                frames.add(new TextureRegion(tex));
            }

            animations.put(cfg.state, new Animation<>(cfg.frameDuration, frames, cfg.playMode));
        }
    }

    public void draw(SpriteBatch batch, Knight knight, float delta) {
        Knight.State currentState = knight.getCurrentState();

        if (currentState != previousState) {
            stateTime = 0f;
            previousState = currentState;
        } else {
            stateTime += delta;
        }

        Animation<TextureRegion> animation = animations.get(currentState);
        if (animation == null) {
            animation = animations.get(Knight.State.IDLE);
        }

        TextureRegion currentFrame = animation.getKeyFrame(stateTime);
        float drawWidth = 64f;
        float drawHeight = 128f;
        float drawX = knight.getPosition().x - 8f;
        float drawY = knight.getPosition().y - 8f;

        if (!knight.isFacingLeft()) {
            batch.draw(currentFrame, drawX + drawWidth, drawY, -drawWidth, drawHeight);
        } else {
            batch.draw(currentFrame, drawX, drawY, drawWidth, drawHeight);
        }
    }

    @Override
    public void dispose() {
        for (Texture texture : loadedTextures) {
            if (texture != null) texture.dispose();
        }
        loadedTextures.clear();
    }
}
