package com.reza.hollowknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.reza.hollowknight.HollowGame;
import games.rednblack.miniaudio.MASound;

import java.util.HashMap;

public class AssetLoader {
    private final HollowGame game;

    public Texture menuBackground;
    public Texture gameLogo;

    public TextureRegion pointerLeft;
    public TextureRegion pointerRight;

    public Texture profileLine;
    public Texture knightHead;
    public Texture hpMask;
    public Texture geoIcon;

    private HashMap<String, Texture> areaBackgrounds;

    public MASound titleTheme;
    public MASound buttonHover;
    public MASound buttonClick;
    public BitmapFont menuFont;
    public BitmapFont subFont;

    public AssetLoader(HollowGame game) {
        this.game = game;
        this.areaBackgrounds = new HashMap<>();
    }

    public void loadMenuAssets() {
        menuBackground = new Texture(Gdx.files.internal("textures/ui/menu_background.png"));
        gameLogo = new Texture(Gdx.files.internal("textures/ui/hollow_knight_logo.png"));

        Texture pointerTex = new Texture(Gdx.files.internal("textures/ui/main_menu_pointer_anim0007.png"));
        pointerRight = new TextureRegion(pointerTex);
        pointerLeft = new TextureRegion(pointerTex);
        pointerLeft.flip(true, false);

        titleTheme = game.miniAudio.createSound("audio/bgm/title_theme.mp3");
        buttonHover = game.miniAudio.createSound("audio/sfx/button_hover.wav");
        buttonClick = game.miniAudio.createSound("audio/sfx/button_click.wav");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/primary_font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 24;
        menuFont = generator.generateFont(parameter);

        parameter.size = 16;
        subFont = generator.generateFont(parameter);
        generator.dispose();
    }

    public void loadProfileMenuAssets() {
        profileLine = new Texture(Gdx.files.internal("textures/ui/profile_fleur0012.png"));
        knightHead = new Texture(Gdx.files.internal("textures/ui/select_game_HUD_0002_health_frame.png"));
        hpMask = new Texture(Gdx.files.internal("textures/ui/HP_UI_040004.png"));
        geoIcon = new Texture(Gdx.files.internal("textures/ui/Geo - HUD_coin_shop.png"));

        areaBackgrounds.put("CROSSROADS", new Texture(Gdx.files.internal("textures/ui/Area_Forgotten Crossroads.png")));
        areaBackgrounds.put("GREENPATH", new Texture(Gdx.files.internal("textures/ui/Area_Green_Path.png")));
        areaBackgrounds.put("CRYSTAL_MINES", new Texture(Gdx.files.internal("textures/ui/Area_Crystal_Mines.png")));
        areaBackgrounds.put("CITY_OF_TEARS", new Texture(Gdx.files.internal("textures/ui/Area_Art_City_of_Tears.png")));

        areaBackgrounds.put("DIRTMOUTH", new Texture(Gdx.files.internal("textures/ui/Area_Art_City_of_Tears.png")));
    }

    public Texture getAreaBackground(String sceneName) {
        if (sceneName == null) return areaBackgrounds.get("CITY_OF_TEARS");

        String key = sceneName.toUpperCase().replace("SCENE", "").trim();

        if (key.equals("CROSSROADS")) return areaBackgrounds.get("CROSSROADS");
        if (key.equals("GREENPATH")) return areaBackgrounds.get("GREENPATH");
        if (key.equals("CRYSTALMINES")) return areaBackgrounds.get("CRYSTAL_MINES");
        if (key.equals("CITYOFTEARS")) return areaBackgrounds.get("CITY_OF_TEARS");

        return areaBackgrounds.get("CITY_OF_TEARS");
    }

    public void dispose() {
        if (menuBackground != null) menuBackground.dispose();
        if (gameLogo != null) gameLogo.dispose();
        if (pointerRight != null) pointerRight.getTexture().dispose();
        if (profileLine != null) profileLine.dispose();
        if (knightHead != null) knightHead.dispose();
        if (hpMask != null) hpMask.dispose();
        if (geoIcon != null) geoIcon.dispose();

        for (Texture tex : areaBackgrounds.values()) {
            if (tex != null) tex.dispose();
        }
        areaBackgrounds.clear();

        if (menuFont != null) menuFont.dispose();
        if (subFont != null) subFont.dispose();
        if (titleTheme != null) titleTheme.dispose();
        if (buttonHover != null) buttonHover.dispose();
        if (buttonClick != null) buttonClick.dispose();
    }
}
