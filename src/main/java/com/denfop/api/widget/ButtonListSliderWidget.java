package com.denfop.api.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ButtonListSliderWidget {
    @OnlyIn(Dist.CLIENT)
    public interface WidgetResponder {
        void setEntryValue(int id, boolean value);

        void setEntryValue(int id, float value);

        void setEntryValue(int id, String value);
    }
}
