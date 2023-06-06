package com.denfop.api.gui;

public class OverlaySupplier implements IOverlaySupplier {

    private final int uS;
    private final int vS;
    private final int uE;
    private final int vE;

    public OverlaySupplier(int uS, int vS, int uE, int vE) {
        this.uS = uS;
        this.vS = vS;
        this.uE = uE;
        this.vE = vE;
    }

    public int getUS() {
        return this.uS;
    }

    public int getVS() {
        return this.vS;
    }

    public int getUE() {
        return this.uE;
    }

    public int getVE() {
        return this.vE;
    }

}
