package com.denfop.api.gui;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class GuiPageButtonList {
    @OnlyIn(Dist.CLIENT)
    public interface GuiResponder {
        void setEntryValue(int id, boolean value);

        void setEntryValue(int id, float value);

        void setEntryValue(int id, String value);
    }
}
