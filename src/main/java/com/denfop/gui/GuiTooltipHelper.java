package com.denfop.gui;

import ic2.core.GuiIC2;

public class GuiTooltipHelper {



    public static void drawAreaTooltip(final GuiIC2<?> gui, int i, int i1, String tooltip2, int i2, int i3, int i4, int i5) {
         new AdvArea(gui,i2,i3,i4,i5).withTooltip(tooltip2).drawForeground(i,i1);
    }

}
