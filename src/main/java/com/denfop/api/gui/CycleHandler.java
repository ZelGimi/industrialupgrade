package com.denfop.api.gui;

import ic2.core.gui.INumericValueHandler;

public class CycleHandler implements IClickHandler, IOverlaySupplier {

    private final int uS;
    private final int vS;
    private final int uE;
    private final int vE;
    private final int overlayStep;
    private final boolean vertical;
    private final int options;
    private final INumericValueHandler handler;

    public CycleHandler(
            int uS,
            int vS,
            int uE,
            int vE,
            int overlayStep,
            boolean vertical,
            int options,
            INumericValueHandler handler
    ) {
        this.uS = uS;
        this.vS = vS;
        this.uE = uE;
        this.vE = vE;
        this.overlayStep = overlayStep;
        this.vertical = vertical;
        this.options = options;
        this.handler = handler;
    }

    public void onClick(MouseButton button) {
        int value = this.getValue();
        if (button == MouseButton.left) {
            value = (value + 1) % this.options;
        } else {
            if (button != MouseButton.right) {
                return;
            }

            value = (value + this.options - 1) % this.options;
        }

        this.handler.onChange(value);
    }

    public int getUS() {
        return this.vertical ? this.uS : this.uS + this.overlayStep * this.getValue();
    }

    public int getVS() {
        return !this.vertical ? this.vS : this.vS + this.overlayStep * this.getValue();
    }

    public int getUE() {
        return this.vertical ? this.uE : this.uS + this.overlayStep * (this.getValue() + 1);
    }

    public int getVE() {
        return !this.vertical ? this.vE : this.vS + this.overlayStep * (this.getValue() + 1);
    }

    protected int getValue() {
        int ret = this.handler.getValue();
        if (ret >= 0 && ret < this.options) {
            return ret;
        } else {
            throw new RuntimeException("invalid value: " + ret);
        }
    }

}
