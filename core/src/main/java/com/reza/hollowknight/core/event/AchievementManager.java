package com.reza.hollowknight.core.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.HashSet;

public class AchievementManager implements GameEventListener {
    private static final String SAVE_NAME = "hollow_knight_achievements";
    private final Preferences prefs;

    private final HashSet<String> unlockedIds = new HashSet<>();
    private final HashSet<String> defeatedEnemyTypes = new HashSet<>();
    private int totalKills = 0;

    private static AchievementManager instance;
    private AchievementPopupNotify currentNotificationListener;

    public interface AchievementPopupNotify {
        void triggerPopup(String titleKey, String descKey);
    }

    public static AchievementManager getInstance() {
        if (instance == null) instance = new AchievementManager();
        return instance;
    }

    private AchievementManager() {
        prefs = Gdx.app.getPreferences(SAVE_NAME);
        loadAchievements();

        EventDispatcher.register(GameEvent.ENEMY_KILLED, this);
        EventDispatcher.register(GameEvent.BOSS_DEFEATED_FALSE_KNIGHT, this);
        EventDispatcher.register(GameEvent.GAME_COMPLETED, this);
    }

    public void setNotificationListener(AchievementPopupNotify listener) {
        this.currentNotificationListener = listener;
    }

    @Override
    public void onEvent(GameEvent event, Object data) {
        switch (event) {
            case ENEMY_KILLED -> {
                totalKills++;
                if (data instanceof String enemyType) {
                    defeatedEnemyTypes.add(enemyType);
                }
                checkHuntingAchievements();
            }
            case BOSS_DEFEATED_FALSE_KNIGHT -> unlock("ACH_DEFEAT_FALSE_KNIGHT");
            case GAME_COMPLETED -> {
                unlock("ACH_COMPLETION");
                if (data instanceof Float timeElapsedSeconds) {
                    if (timeElapsedSeconds <= 2700f) {
                        unlock("ACH_SPEEDRUN");
                    }
                }
            }
        }
    }

    private void checkHuntingAchievements() {
        if (defeatedEnemyTypes.size() >= 3) {
            unlock("ACH_TRUE_HUNTER");
        }
        if (totalKills >= 50) {
            unlock("ACH_GENOCIDE");
        }
    }

    public void unlock(String id) {
        if (!unlockedIds.contains(id)) {
            unlockedIds.add(id);
            prefs.putBoolean(id, true);
            prefs.flush();

            if (currentNotificationListener != null) {
                currentNotificationListener.triggerPopup(id + "_TITLE", id + "_DESC");
            }
        }
    }

    public boolean isUnlocked(String id) {
        return unlockedIds.contains(id);
    }

    private void loadAchievements() {
        String[] ids = {"ACH_COMPLETION", "ACH_SPEEDRUN", "ACH_TRUE_HUNTER", "ACH_DEFEAT_FALSE_KNIGHT", "ACH_GENOCIDE", "ACH_GEO_HOARDER"};
        for (String id : ids) {
            if (prefs.getBoolean(id, false)) {
                unlockedIds.add(id);
            }
        }
    }
}
