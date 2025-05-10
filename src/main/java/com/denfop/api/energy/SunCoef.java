package com.denfop.api.energy;

import net.minecraft.world.level.Level;

public class SunCoef {

    private final Level world;
    private double coef;

    public SunCoef(Level world) {
        this.world = world;
        this.coef = 0;
    }

    public double getCoef() {
        return coef;
    }

    public void calculate() {


        float k = 0;
        float celestialAngle = ((this.getWorld().getGameTime() % 24000)) * 360 / 24000F;
        celestialAngle %= 360;

        if (celestialAngle <= 90) {
            k = celestialAngle / 90;
        } else if (celestialAngle > 90 && celestialAngle < 180) {
            celestialAngle -= 90;
            k = 1 - celestialAngle / 90;
        } else if (celestialAngle > 180 && celestialAngle < 270) {
            celestialAngle -= 180;
            k = celestialAngle / 90;
        } else if (celestialAngle > 270 && celestialAngle < 360) {
            celestialAngle -= 270;
            k = 1 - celestialAngle / 90;
        }

        this.coef = k;
    }

    private Level getWorld() {
        return this.world;
    }

}
