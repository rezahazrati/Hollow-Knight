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

        langEN.put("GUIDE_MENU_TITLE", "GUIDE & CONTROLS");
        langEN.put("DESCRIPTION_HEADER", "INFORMATION");
        langEN.put("GUIDE_BACK", "RETURN TO MENU");

        langEN.put("LBL_MOVE_UP", "ACTION: MOVE UP");
        langEN.put("LBL_MOVE_DOWN", "ACTION: MOVE DOWN");
        langEN.put("LBL_MOVE_LEFT", "ACTION: MOVE LEFT");
        langEN.put("LBL_MOVE_RIGHT", "ACTION: MOVE RIGHT");
        langEN.put("LBL_ATTACK", "ACTION: NAIL SLASH");
        langEN.put("LBL_DASH", "ACTION: DASH EFF");
        langEN.put("LBL_JUMP", "ACTION: JUMP MVT");
        langEN.put("LBL_HEALTH", "CHAR: MASKS & HEALTH");
        langEN.put("LBL_SOUL", "CHAR: SOUL VESSEL");
        langEN.put("LBL_CHEAT_TELEPORT", "CHEAT: ARENA TELEPORT");
        langEN.put("LBL_CHEAT_NOCLIP", "CHEAT: NOCLIP FLIGHT");
        langEN.put("LBL_CHEAT_HEAL", "CHEAT: EMERGENCY HEAL");
        langEN.put("LBL_CHEAT_SOUL", "CHEAT: MAX SOUL REFILL");
        langEN.put("LBL_CHEAT_GOD", "CHEAT: GOD MODE TOGGLE");

        langEN.put("DESC_MOVE_UP", "Directs the Knight upwards or looking toward higher terrain.");
        langEN.put("DESC_MOVE_DOWN", "Directs the Knight downwards, crouches, or prompts a lower strike.");
        langEN.put("DESC_MOVE_LEFT", "Moves the character smoothly toward the left side of the screen.");
        langEN.put("DESC_MOVE_RIGHT", "Moves the character smoothly toward the right side of the screen.");
        langEN.put("DESC_ATTACK", "Slashes your primary Nail weapon forward to inflict damage on hostiles.");
        langEN.put("DESC_DASH", "Triggers a quick, horizontal dash maneuver to cross gaps or dodge.");
        langEN.put("DESC_JUMP", "Propels the Knight vertically up into the air based on physics curves.");
        langEN.put("DESC_HEALTH", "Represents your structural life force. Lose masks on damage; zero means defeat.");
        langEN.put("DESC_SOUL", "Mystic energy gained by hitting enemies with your weapon. Used to cast spells or heal.");
        langEN.put("DESC_CHEAT_TELEPORT", "Debug Shortcut:\nPress [Left Ctrl + T]\nInstantly warps the character to the False Knight boss room arena.");
        langEN.put("DESC_CHEAT_NOCLIP", "Debug Shortcut:\nPress [Left Ctrl + N]\nDisables boundaries and gravity, allowing free-flight exploration.");
        langEN.put("DESC_CHEAT_HEAL", "Debug Shortcut:\nPress [Left Ctrl + H]\nInstantly restores all health masks back to max capacity.");
        langEN.put("DESC_CHEAT_SOUL", "Debug Shortcut:\nPress [Left Ctrl + S]\nInstantly fills your active soul vessel reserve gauge to 100%.");
        langEN.put("DESC_CHEAT_GOD", "Debug Shortcut:\nPress [Left Ctrl + G]\nToggle complete invulnerability. Immune to all spikes, hazards, and enemy attacks.");

        langEN.put("ACHIEVEMENTS", "ACHIEVEMENTS");
        langEN.put("ACH_UNLOCKED_BANNER", "ACHIEVEMENT UNLOCKED");
        langEN.put("ACH_COMPLETION_TITLE", "Completion");
        langEN.put("ACH_COMPLETION_DESC", "Conquer the dark depths and finish the main story quest.");
        langEN.put("ACH_SPEEDRUN_TITLE", "Speedrun");
        langEN.put("ACH_SPEEDRUN_DESC", "Complete the game in under 45 minutes of active runtime play.");
        langEN.put("ACH_TRUE_HUNTER", "True Hunter");
        langEN.put("ACH_TRUE_HUNTER_TITLE", "True Hunter");
        langEN.put("ACH_TRUE_HUNTER_DESC", "Defeat 3 or more unique variations of underground enemy creatures.");
        langEN.put("ACH_DEFEAT_FALSE_KNIGHT_TITLE", "Defeat False Knight");
        langEN.put("ACH_DEFEAT_FALSE_KNIGHT_DESC", "Overcome the mighty False Knight in single combat.");
        langEN.put("ACH_GENOCIDE_TITLE", "Pest Control");
        langEN.put("ACH_GENOCIDE_DESC", "Clear out 50 enemies across your gameplay run.");
        langEN.put("ACH_GEO_HOARDER_TITLE", "Geo Hoarder");
        langEN.put("ACH_GEO_HOARDER_DESC", "Amass 5,000 Geo in your wallet at once. Shiny, but don't lose it all to a shade!");

        langFR.put("ACH_GEO_HOARDER_TITLE", "Banquier des Abysses");
        langFR.put("ACH_GEO_HOARDER_DESC", "Accumulez 5 000 Géo dans votre portefeuille en une fois. Brillant, mais ne perdez pas tout face à votre Ombre !");
        langFR.put("ACH_UNLOCKED_BANNER", "SUCCÈS DÉVERROUILLÉ");
        langFR.put("ACH_COMPLETION_TITLE", "Complétion");
        langFR.put("ACH_COMPLETION_DESC", "Triomphez des abysses et terminez l'histoire principale.");
        langFR.put("ACH_SPEEDRUN_TITLE", "Speedrun");
        langFR.put("ACH_SPEEDRUN_DESC", "Terminez le jeu complet en moins de 45 minutes de jeu.");
        langFR.put("ACH_TRUE_HUNTER_TITLE", "Vrai Chasseur");
        langFR.put("ACH_TRUE_HUNTER_DESC", "Éliminez au moins 3 types de créatures souterraines uniques.");
        langFR.put("ACH_DEFEAT_FALSE_KNIGHT_TITLE", "Faux Chevalier Vaincu");
        langFR.put("ACH_DEFEAT_FALSE_KNIGHT_DESC", "Triomphez du puissant Faux Chevalier au combat.");
        langFR.put("ACH_GENOCIDE_TITLE", "Contrôle des Nuisibles");
        langFR.put("ACH_GENOCIDE_DESC", "Éliminez un total de 50 ennemis au cours de votre partie.");
        langFR.put("ACHIEVEMENTS", "TROPHÉES");
        langFR.put("GUIDE_MENU_TITLE", "GUIDE & COMMANDES");
        langFR.put("DESCRIPTION_HEADER", "INFORMATIONS");
        langFR.put("GUIDE_BACK", "RETOUR AU MENU");

        langFR.put("LBL_MOVE_UP", "ACTION: MONTER");
        langFR.put("LBL_MOVE_DOWN", "ACTION: DESCENDRE");
        langFR.put("LBL_MOVE_LEFT", "ACTION: GAUCHE");
        langFR.put("LBL_MOVE_RIGHT", "ACTION: DROITE");
        langFR.put("LBL_ATTACK", "ACTION: COUP D'ÉPÉE");
        langFR.put("LBL_DASH", "ACTION: HAUT DASH");
        langFR.put("LBL_JUMP", "ACTION: SAUTER");
        langFR.put("LBL_HEALTH", "STATS: MASQUES & VIE");
        langFR.put("LBL_SOUL", "STATS: RÉSERVE D'ÂME");
        langFR.put("LBL_CHEAT_TELEPORT", "TRICHE: TÉLÉPORTATION");
        langFR.put("LBL_CHEAT_NOCLIP", "TRICHE: MODE VOL NOCLIP");
        langFR.put("LBL_CHEAT_HEAL", "TRICHE: SOIN D'URGENCE");
        langFR.put("LBL_CHEAT_SOUL", "TRICHE: MAX ÂME REFILL");
        langFR.put("LBL_CHEAT_GOD", "TRICHE: MODE DIEU INVINC");

        langFR.put("DESC_MOVE_UP", "Oriente le Chevalier vers le haut ou permet de regarder plus haut.");
        langFR.put("DESC_MOVE_DOWN", "Oriente le Chevalier vers le bas ou permet de s'accroupir.");
        langFR.put("DESC_MOVE_LEFT", "Déplace le personnage vers le côté gauche de l'écran.");
        langFR.put("DESC_MOVE_RIGHT", "Déplace le personnage vers le côté droit de l'écran.");
        langFR.put("DESC_ATTACK", "Donne un coup d'aiguillon vers l'avant pour blesser les ennemis.");
        langFR.put("DESC_DASH", "Exécute une esquive horizontale rapide pour franchir les obstacles.");
        langFR.put("DESC_JUMP", "Propulse le Chevalier verticalement dans les airs.");
        langFR.put("DESC_HEALTH", "Représente vos points de vie. Perdre tous les masques provoque la défaite.");
        langFR.put("DESC_SOUL", "Énergie mystique obtenue en frappant les ennemis. Utilisée pour soigner ou lancer des sorts.");
        langFR.put("DESC_CHEAT_TELEPORT", "Raccourci Débug:\nAppuyez sur [Ctrl Gauche + T]\nTéléporte instantanément dans l'arène du boss False Knight.");
        langFR.put("DESC_CHEAT_NOCLIP", "Raccourci Débug:\nAppuyez sur [Ctrl Gauche + N]\nDésactive la physique et la gravité pour voler librement.");
        langFR.put("DESC_CHEAT_HEAL", "Raccourci Débug:\nAppuyez sur [Ctrl Gauche + H]\nRestaure instantanément tous les masques de vie.");
        langFR.put("DESC_CHEAT_SOUL", "Raccourci Débug:\nAppuyez sur [Ctrl Gauche + S]\nRemplat entièrement votre jauge de réserve d'âme.");
        langFR.put("DESC_CHEAT_GOD", "Raccourci Débug:\nAppuyez sur [Ctrl Gauche + G]\nActive l'invulnérabilité totale face aux ennemis et aux pics.");

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
