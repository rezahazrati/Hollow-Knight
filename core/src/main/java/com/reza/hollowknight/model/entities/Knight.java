package com.reza.hollowknight.model.entities;

import com.reza.hollowknight.model.Vec2;

public class Knight {
    private int mask = 5;
    private int soulVessel = 0;
    private int geo = 0;
    private Vec2 position;
    private Vec2 velocity;

    public void addMask() {
        this.mask += 1;
    }

    public void addSoulVessel() {
        this.soulVessel = Math.max(99, this.soulVessel + 11);
    }

    public int getMask() {
        return mask;
    }

    public int getSoulVessel() {
        return soulVessel;
    }


    public int getGeo() {
        return geo;
    }

    public void setGeo(int geo) {
        this.geo = Math.max(0, this.geo + geo);
    }
}
