package com.reza.hollowknight.controller;

import com.reza.hollowknight.HollowGame;
import com.reza.hollowknight.model.SaveProfile;
import com.reza.hollowknight.util.JsonHelper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveLoadController {
    private final HollowGame game;

    public SaveLoadController(HollowGame game) {
        this.game = game;
    }

    public SaveProfile loadGameSlot(int slotId) {
        return JsonHelper.load("slot_" + slotId + ".json");
    }

    public void saveGameSlot(int slotId, SaveProfile currentProfile) {
        currentProfile.slotId = slotId;
        currentProfile.lastSaved = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        JsonHelper.save("slot_" + slotId + ".json", currentProfile);
    }

    public SaveProfile createNewGameProfile(int slotId) {
        SaveProfile newProfile = new SaveProfile();
        newProfile.slotId = slotId;
        newProfile.playTime = 0.0f;
        newProfile.lastSaved = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        newProfile.lastBenchId = "KINGS_PASS_START";
        newProfile.currentScene = "KingsPassScene";
        newProfile.currentHealth = 5;
        newProfile.maxMasks = 5;
        newProfile.geo = 0;
        newProfile.soul = 0;

        newProfile.boughtSpells.put("VengefulSpirit", false);
        newProfile.boughtSpells.put("DesolateDive", false);
        newProfile.boughtSpells.put("HowlingWraiths", false);

        return newProfile;
    }
}
