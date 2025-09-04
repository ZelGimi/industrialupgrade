package com.denfop.api.widget;


import com.denfop.screen.ScreenIndustrialUpgrade;

public class AdvancedTooltipWidget extends TooltipWidget {

    public AdvancedTooltipWidget(final ScreenIndustrialUpgrade<?> gui, final int x, final int y, final int x1, final int y1) {
        super(gui, x, y, x1 - x, y1 - y);
    }

}
