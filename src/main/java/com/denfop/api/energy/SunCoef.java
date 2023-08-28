package com.denfop.api.energy;

import net.minecraft.world.World;

public class SunCoef {

    private final World world;
    private double coef;

    public SunCoef(World world) {
        this.world = world;
        this.coef = 0;
    }

    public double getCoef() {
        return coef;
    }

    public void calculate() {
        double k = 0;
        float angle = this.getWorld().getCelestialAngle(1.0F) - 0.784690560F < 0 ? 1.0F - 0.784690560F : -0.784690560F;
        float celestialAngle = (this.getWorld().getCelestialAngle(1.0F) + angle) * 360.0F;
        celestialAngle %= 360;
        celestialAngle += 12;
        //TODO: end code GC
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

    private World getWorld() {
        return this.world;
    }

}
