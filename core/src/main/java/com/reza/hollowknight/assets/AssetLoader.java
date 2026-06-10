package com.reza.hollowknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.reza.hollowknight.HollowGame;
import games.rednblack.miniaudio.MASound; // Import miniaudio sound container

public class AssetLoader {
    private final HollowGame game;

    public Texture menuBackground;
    public Texture gameLogo;
    public Texture menuPointer;

    public MASound titleTheme;
    public MASound buttonHover;
    public MASound buttonClick;
    public BitmapFont menuFont;

    public AssetLoader(HollowGame game) {
        this.game = game;
    }

    public void loadMenuAssets() {
        menuBackground = new Texture(Gdx.files.internal("textures/ui/menu_background.png"));
        gameLogo = new Texture(Gdx.files.internal("textures/ui/hollow_knight_logo.png"));
       // menuPointer = new Texture(Gdx.files.internal("textures/ui/menu_pointer.png"));

        titleTheme = game.miniAudio.createSound("audio/bgm/title_theme.mp3");
        buttonHover = game.miniAudio.createSound("audio/sfx/button_hover.wav");
        buttonClick = game.miniAudio.createSound("audio/sfx/button_click.wav");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/primary_font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 26;

        menuFont = generator.generateFont(parameter);
        generator.dispose();
    }

    public void dispose() {
        if (menuBackground != null) menuBackground.dispose();
        if (gameLogo != null) gameLogo.dispose();
        if (menuPointer != null) menuPointer.dispose();
        if (menuFont != null) menuFont.dispose();

        if (titleTheme != null) titleTheme.dispose();
        if (buttonHover != null) buttonHover.dispose();
        if (buttonClick != null) buttonClick.dispose();
    }
}
