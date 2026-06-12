package com.reza.hollowknight.model.entities;

import com.reza.hollowknight.model.Vec2;
import java.util.ArrayList;
import java.util.List;

public class Zote extends InteractiveEntity {
    private boolean hasTalkedBefore = false;
    private boolean inConversation = false;
    private int currentDialogueIndex = 0;
    private int currentPreceptIndex = 0;
    private final List<String> initialDialogues = new ArrayList<>();
    private final List<String> precepts = new ArrayList<>();

    public Zote() {
        super("npc_zote", new Vec2(0, 0));
        initializeDialogueData();
    }

    public Zote(Vec2 position) {
        super("npc_zote", position);
        initializeDialogueData();
    }

    private void initializeDialogueData() {
        initialDialogues.add("Leave me be, tiny creature! I am Zote the Mighty!");
        initialDialogues.add("I do not need the assistance of someone as insignificant as you.");
        initialDialogues.add("My weapon, Lifeaker, has slain thousands!");

        precepts.add("Precept One: Always Win Your Battles.");
        precepts.add("Precept Two: Never Let Them Laugh Choice.");
        precepts.add("Precept Three: Always Be Rested.");
        precepts.add("Precept Four: Forget Your Past.");
        precepts.add("Precept Five: Strength Beats Strength.");
    }

    public void startInteraction() {
        inConversation = true;
        if (!hasTalkedBefore) {
            currentDialogueIndex = 0;
        }
    }

    public String advanceDialogue() {
        if (!inConversation) {
            return "";
        }

        if (!hasTalkedBefore) {
            if (currentDialogueIndex < initialDialogues.size()) {
                String line = initialDialogues.get(currentDialogueIndex);
                currentDialogueIndex++;
                if (currentDialogueIndex >= initialDialogues.size()) {
                    hasTalkedBefore = true;
                    setInteracted(true);
                }
                return line;
            }
        }

        String preceptLine = precepts.get(currentPreceptIndex);
        currentPreceptIndex = (currentPreceptIndex + 1) % precepts.size();
        inConversation = false;
        return preceptLine;
    }

    public void endConversation() {
        inConversation = false;
    }

    public boolean isHasTalkedBefore() {
        return hasTalkedBefore;
    }

    public boolean isInConversation() {
        return inConversation;
    }

    public int getCurrentDialogueIndex() {
        return currentDialogueIndex;
    }

    public int getCurrentPreceptIndex() {
        return currentPreceptIndex;
    }
}
