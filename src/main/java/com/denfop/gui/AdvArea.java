package com.denfop.gui;


import com.denfop.api.gui.Area;

public class AdvArea extends Area {

    public AdvArea(final GuiCore<?> gui, final int x, final int y, final int x1, final int y1) {
        super(gui, x, y, x1 - x, y1 - y);
    }

}
