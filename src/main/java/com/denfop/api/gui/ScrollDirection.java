package com.denfop.api.gui;

public enum ScrollDirection {
    stopped(0),
    up(-1),
    down(1);

    public final byte multiplier;

    ScrollDirection(int multiplier) {
        this.multiplier = (byte) multiplier;
    }
}
