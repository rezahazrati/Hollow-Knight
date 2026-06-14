package com.reza.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.reza.hollowknight.model.entities.Knight;

public class GamePlayScreen extends ScreenAdapter {
    public void handleGameplayCheatCodes(float delta, Knight activeKnight) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
                activeKnight.setPosition(new com.reza.hollowknight.model.Vec2(2500, 400));
                System.out.println("[DEBUG] Boss Arena Teleport Triggered!");
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                activeKnight.toggleNoclip();
                System.out.println("[DEBUG] Noclip Status Modded To: " + activeKnight.isNoclipEnabled());
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                activeKnight.cheatEmergencyHeal();
                System.out.println("[DEBUG] Emergency Health Restored!");
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                activeKnight.cheatRefillSoul();
                System.out.println("[DEBUG] Soul Vessel Reservoir Capped!");
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                activeKnight.toggleGodMode();
                System.out.println("[DEBUG] God Mode Status Modded To: " + activeKnight.isGodModeEnabled());
            }
        }
    }
}
