package com.denfop.api.windsystem;

public class Wind {

    private int time;
    private double Wind_Strength;

    public Wind(double Wind_Strength, int time) {
        this.Wind_Strength = Wind_Strength;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getWind_Strength() {
        return Wind_Strength;
    }

    public void setWind_Strength(double Wind_Strength) {
        this.Wind_Strength = Wind_Strength;
    }

}
