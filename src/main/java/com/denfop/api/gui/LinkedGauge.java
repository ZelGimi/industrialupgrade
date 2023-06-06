package com.denfop.api.gui;

import com.denfop.gui.GuiIC2;

public class LinkedGauge extends Gauge<LinkedGauge> {

    protected final String name;
    private final IGuiValueProvider provider;

    public LinkedGauge(GuiIC2<?> gui, int x, int y, IGuiValueProvider provider, String name, IGaugeStyle style) {
        super(gui, x, y, style.getProperties());
        this.provider = provider;
        this.name = name;
    }

    protected double getRatio() {
        return this.provider.getGuiValue(this.name);
    }

}
