package com.reza.hollowknight.model.atmosphere;

public interface Atmosphere {
    String getBgmPath();
    String getTilesetPath();
    String getEnvironmentName();
    boolean hasWeatherEffects();
    float HazardsDamageMultiplier();
}
