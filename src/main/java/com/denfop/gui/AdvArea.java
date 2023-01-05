package com.denfop.gui;

import ic2.core.GuiIC2;
import ic2.core.gui.Area;

public class AdvArea extends Area {

    public AdvArea(final GuiIC2<?> gui, final int x, final int y, final int x1, final int y1) {
        super(gui, x, y, x1 - x, y1 - y);
    }

}
