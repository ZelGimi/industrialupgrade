package com.denfop.api.widget;


import com.denfop.screen.ScreenIndustrialUpgrade;

public class TooltipWidget extends ScreenWidget {

    public TooltipWidget(ScreenIndustrialUpgrade<?> gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height, new WidgetDefault<>(new EmptyWidget()));
    }

}
