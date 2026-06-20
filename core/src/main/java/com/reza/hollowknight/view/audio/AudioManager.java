package com.reza.hollowknight.view.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.reza.hollowknight.model.GameSettings;
import com.reza.hollowknight.model.entities.Knight;

import java.util.HashMap;

public class AudioManager implements Disposable {
    private static AudioManager instance;

    private final HashMap<Knight.State, Sound> stateSounds;
    private final HashMap<Knight.State, Long> activeLoopIds;
    private Knight.State lastState;

    private AudioManager() {
        stateSounds = new HashMap<>();
        activeLoopIds = new HashMap<>();
        loadSFX();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    private void loadSFX() {
        // Place audio files under your assets directory structure (e.g., assets/audio/...)
        String root = "audio/sfx/";

        registerSound(Knight.State.RUN, root + "hero_run_footsteps_stone.wav");
        registerSound(Knight.State.WALK, root + "hero_run_footsteps_stone.wav");
        registerSound(Knight.State.AIRBORNE, root + "hero_jump.wav");
        registerSound(Knight.State.DOUBLE_JUMP, root + "hero_wings.wav");
        registerSound(Knight.State.LANDING, root + "hero_land_soft.wav");
        registerSound(Knight.State.WALLJUMP, root + "hero_wall_jump.wav");
        registerSound(Knight.State.DASH, root + "hero_dash.wav");

        // Combat Slashes
        registerSound(Knight.State.SLASH, root + "hero_butterfly_blade.wav");
        registerSound(Knight.State.SLASHALT, root + "hero_butterfly_blade.wav");
        registerSound(Knight.State.DOWNSLASH, root + "hero_butterfly_blade.wav");
        registerSound(Knight.State.UPSLASH, root + "hero_butterfly_blade.wav");

        // Spells & Vulnerabilities
        registerSound(Knight.State.FIREBALL, root + "hero_fireball.wav");
        registerSound(Knight.State.IDLE_HURT, root + "hero_damage.wav");
        registerSound(Knight.State.SCREAM, root + "hero_scream_spell.wav");
        registerSound(Knight.State.DEATH, root + "hero_death_v2.wav");
    }

    private void registerSound(Knight.State state, String path) {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            stateSounds.put(state, sound);
        } catch (Exception e) {
            Gdx.app.error("AudioManager", "Failed loading sound resource: " + path, e);
        }
    }

    /**
     * Intercepts framework state updates to handle sound loops and context triggers cleanly.
     */
    public void updateStateAudio(Knight.State currentState) {
        if (currentState == lastState) return; // Guard logic against repetitive frame invocations

        // 1. Terminate running loop sequences if state changes
        stopLoopingState(lastState);

        // 2. Short-circuit if preferences global toggle state is disabled
        if (!GameSettings.sfxEnabled) {
            lastState = currentState;
            return;
        }

        // 3. Play sound based on target metadata properties
        Sound targetSound = stateSounds.get(currentState);
        if (targetSound != null) {
            if (currentState == Knight.State.RUN || currentState == Knight.State.WALK) {
                // Loop sound instances for continuous movement frames safely
                long id = targetSound.loop(1.0f);
                activeLoopIds.put(currentState, id);
            } else {
                // Instanced single-shot play profiles for independent triggers (Dash, Attack...)
                targetSound.play(1.0f);
            }
        }

        lastState = currentState;
    }

    private void stopLoopingState(Knight.State state) {
        if (state != null && activeLoopIds.containsKey(state)) {
            Sound sound = stateSounds.get(state);
            if (sound != null) {
                sound.stop(activeLoopIds.get(state));
            }
            activeLoopIds.remove(state);
        }
    }

    @Override
    public void dispose() {
        for (Sound sound : stateSounds.values()) {
            if (sound != null) sound.dispose();
        }
        stateSounds.clear();
        activeLoopIds.clear();
    }
}
