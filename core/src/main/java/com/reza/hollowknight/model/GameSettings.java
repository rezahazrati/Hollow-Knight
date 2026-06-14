package com.reza.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import java.util.HashMap;

public class GameSettings {
    private static final String PREFS_NAME = "hollow_knight_settings";
    private static Preferences prefs;

    public static final int DEFAULT_KEY_UP = Input.Keys.UP;
    public static final int DEFAULT_KEY_DOWN = Input.Keys.DOWN;
    public static final int DEFAULT_KEY_LEFT = Input.Keys.LEFT;
    public static final int DEFAULT_KEY_RIGHT = Input.Keys.RIGHT;
    public static final int DEFAULT_KEY_ATTACK = Input.Keys.X;
    public static final int DEFAULT_KEY_DASH = Input.Keys.C;
    public static final int DEFAULT_KEY_JUMP = Input.Keys.Z;

    public static int keyUp, keyDown, keyLeft, keyRight, keyAttack, keyDash, keyJump;

    public static float bgmVolume;
    public static boolean bgmEnabled;
    public static boolean sfxEnabled;
    public static float brightness;
    public static String currentLanguage;

    private static final HashMap<String, String> langEN = new HashMap<>();
    private static final HashMap<String, String> langFR = new HashMap<>();

    public static void init() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
        setupLanguages();
        load();
    }

    private static void setupLanguages() {
        // ==========================================
        // ENGLISH LOCALIZATION BUNDLE
        // ==========================================
        langEN.put("START GAME", "START GAME");
        langEN.put("SETTINGS", "SETTINGS");
        langEN.put("QUIT GAME", "QUIT GAME");
        langEN.put("BACK TO MAIN MENU", "BACK TO MAIN MENU");
        langEN.put("BGM VOL: ", "BGM VOL: ");
        langEN.put("BGM: ON", "BGM: ON");
        langEN.put("BGM: OFF", "BGM: OFF");
        langEN.put("SFX: ON", "SFX: ON");
        langEN.put("SFX: OFF", "SFX: OFF");
        langEN.put("BRIGHTNESS: ", "BRIGHTNESS: ");
        langEN.put("LANGUAGE: EN", "LANGUAGE: EN");
        langEN.put("LANGUAGE: FR", "LANGUAGE: FR");
        langEN.put("RESET TO DEFAULT", "RESET TO DEFAULT");
        langEN.put("PRESS ANY KEY...", "PRESS ANY KEY...");

        langEN.put("MOVE UP", "MOVE UP");
        langEN.put("MOVE DOWN", "MOVE DOWN");
        langEN.put("MOVE LEFT", "MOVE LEFT");
        langEN.put("MOVE RIGHT", "MOVE RIGHT");
        langEN.put("ATTACK", "ATTACK");
        langEN.put("DASH", "DASH");
        langEN.put("JUMP", "JUMP");

        langEN.put("SELECT PROFILE", "SELECT PROFILE");
        langEN.put("NEW GAME", "NEW GAME");
        langEN.put("BACK", "BACK");
        langEN.put("CLEAR SAVE", "CLEAR SAVE");

        langEN.put("CROSSROADS", "FORGOTTEN CROSSROADS");
        langEN.put("GREENPATH", "GREENPATH");
        langEN.put("CRYSTALMINES", "CRYSTAL PEAK");
        langEN.put("CITYOFTEARS", "CITY OF TEARS");
        langEN.put("DIRTMOUTH", "DIRTMOUTH");

        langFR.put("START GAME", "COMMENCER LE JEU");
        langFR.put("SETTINGS", "PARAMÈTRES");
        langFR.put("QUIT GAME", "QUITTER LE JEU");
        langFR.put("BACK TO MAIN MENU", "RETOUR AU MENU");
        langFR.put("BGM VOL: ", "VOL MUSIQUE: ");
        langFR.put("BGM: ON", "MUSIQUE: OUI");
        langFR.put("BGM: OFF", "MUSIQUE: NON");
        langFR.put("SFX: ON", "EFFETS: OUI");
        langFR.put("SFX: OFF", "EFFETS: NON");
        langFR.put("BRIGHTNESS: ", "LUMINOSITÉ: ");
        langFR.put("LANGUAGE: EN", "LANGUE: EN");
        langFR.put("LANGUAGE: FR", "LANGUE: FR");
        langFR.put("RESET TO DEFAULT", "RÉINITIALISER");
        langFR.put("PRESS ANY KEY...", "APPUYEZ SUR UNE TOUCHE...");

        langFR.put("MOVE UP", "MONTER");
        langFR.put("MOVE DOWN", "DESCENDRE");
        langFR.put("MOVE LEFT", "GAUCHE");
        langFR.put("MOVE RIGHT", "DROITE");
        langFR.put("ATTACK", "ATTAQUER");
        langFR.put("DASH", "DASH");
        langFR.put("JUMP", "SAUTER");

        langFR.put("SELECT PROFILE", "SÉLECTIONNER UN PROFIL");
        langFR.put("NEW GAME", "NOUVELLE PARTIE");
        langFR.put("BACK", "RETOUR");
        langFR.put("CLEAR SAVE", "EFFACER");

        langFR.put("CROSSROADS", "ROUTES CROISÉES");
        langFR.put("GREENPATH", "VERTCHEMIN");
        langFR.put("CRYSTALMINES", "MONT CRISTAL");
        langFR.put("CITYOFTEARS", "CITÉ DES LARMES");
        langFR.put("DIRTMOUTH", "DIRTMOUTH");
    }

    public static String getString(String key) {
        if ("FR".equals(currentLanguage) && langFR.containsKey(key)) {
            return langFR.get(key);
        }
        return langEN.getOrDefault(key, key);
    }

    public static void load() {
        bgmVolume = prefs.getFloat("bgmVolume", 0.5f);
        bgmEnabled = prefs.getBoolean("bgmEnabled", true);
        sfxEnabled = prefs.getBoolean("sfxEnabled", true);
        brightness = prefs.getFloat("brightness", 1.0f);
        currentLanguage = prefs.getString("currentLanguage", "EN");

        keyUp = prefs.getInteger("keyUp", DEFAULT_KEY_UP);
        keyDown = prefs.getInteger("keyDown", DEFAULT_KEY_DOWN);
        keyLeft = prefs.getInteger("keyLeft", DEFAULT_KEY_LEFT);
        keyRight = prefs.getInteger("keyRight", DEFAULT_KEY_RIGHT);
        keyAttack = prefs.getInteger("keyAttack", DEFAULT_KEY_ATTACK);
        keyDash = prefs.getInteger("keyDash", DEFAULT_KEY_DASH);
        keyJump = prefs.getInteger("keyJump", DEFAULT_KEY_JUMP);
    }

    public static void save() {
        prefs.putFloat("bgmVolume", bgmVolume);
        prefs.putBoolean("bgmEnabled", bgmEnabled);
        prefs.putBoolean("sfxEnabled", sfxEnabled);
        prefs.putFloat("brightness", brightness);
        prefs.putString("currentLanguage", currentLanguage);

        prefs.putInteger("keyUp", keyUp);
        prefs.putInteger("keyDown", keyDown);
        prefs.putInteger("keyLeft", keyLeft);
        prefs.putInteger("keyRight", keyRight);
        prefs.putInteger("keyAttack", keyAttack);
        prefs.putInteger("keyDash", keyDash);
        prefs.putInteger("keyJump", keyJump);
        prefs.flush();
    }

    public static void resetToDefaults() {
        bgmVolume = 0.5f;
        bgmEnabled = true;
        sfxEnabled = true;
        brightness = 1.0f;
        currentLanguage = "EN";

        keyUp = DEFAULT_KEY_UP;
        keyDown = DEFAULT_KEY_DOWN;
        keyLeft = DEFAULT_KEY_LEFT;
        keyRight = DEFAULT_KEY_RIGHT;
        keyAttack = DEFAULT_KEY_ATTACK;
        keyDash = DEFAULT_KEY_DASH;
        keyJump = DEFAULT_KEY_JUMP;
        save();
    }
}
