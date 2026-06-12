package com.reza.hollowknight.model.atmosphere;

public abstract class BaseAtmosphere implements Atmosphere {
    protected String bgmPath;
    protected String tilesetPath;
    protected String environmentName;
    protected boolean weatherEffects;
    protected float hazardsMultiplier;

    public BaseAtmosphere(String name, String bgm, String tileset, boolean weather, float hazardsMult) {
        this.environmentName = name;
        this.bgmPath = bgm;
        this.tilesetPath = tileset;
        this.weatherEffects = weather;
        this.hazardsMultiplier = hazardsMult;
    }

    @Override
    public String getBgmPath() {
        return bgmPath;
    }

    @Override
    public String getTilesetPath() {
        return tilesetPath;
    }

    @Override
    public String getEnvironmentName() {
        return environmentName;
    }

    @Override
    public boolean hasWeatherEffects() {
        return weatherEffects;
    }

    @Override
    public float HazardsDamageMultiplier() {
        return hazardsMultiplier;
    }
}
