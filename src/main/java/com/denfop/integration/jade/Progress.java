package com.denfop.integration.jade;

import java.awt.*;

public class Progress {
    private final int progress;
    private final int max;
    private final String suffix;
    private final String prefix;
    private final int r;
    private final int g;
    private final int b;

    public Progress(int progress, int max, String suffix, int color,String prefix) {
        this.progress = progress;
        this.max=max;
        this.suffix = suffix;
        Color color1 = new Color(color);
        this.r = color1.getRed();
        this.g = color1.getGreen();
        this.b = color1.getBlue();
        this.prefix = prefix;

    }

    public Progress(int progress, int max, String suffix, int color) {
        this.progress = progress;
        this.max=max;
        this.suffix = suffix;
        Color color1 = new Color(color);
        this.r = color1.getRed();
        this.g = color1.getGreen();
        this.b = color1.getBlue();
        this.prefix="";

    }



    public String getPrefix() {
        return prefix;
    }

    public Progress(int progress, int max, String suffix, int r, int g, int b, String prefix) {
        this.progress = progress;
        this.max=max;
        this.suffix = suffix;

        this.r =r;
        this.g =g;
        this.b = b;
        this.prefix = prefix;
    }
    public Progress(int progress, int max, String suffix, int r, int g, int b) {
        this.progress = progress;
        this.max=max;
        this.suffix = suffix;

        this.r =r;
        this.g =g;
        this.b = b;
        this.prefix = "";
    }
    public static int[] intToRgb(int hexColor) {
        int r = (hexColor >> 16) & 0xFF;
        int g = (hexColor >> 8) & 0xFF;
        int b = hexColor & 0xFF;
        return new int[]{r, g, b};
    }
    public int getMax() {
        return max;
    }

    public int getG() {
        return g;
    }

    public int getR() {
        return r;
    }

    public int getB() {
        return b;
    }

    public int getProgress() {
        return progress;
    }

    public String getSuffix() {
        return suffix;
    }
}
