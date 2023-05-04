package com.denfop.api.gui;

public interface IGuiValueProvider {

    double getGuiValue(String var1);

    interface IActiveGuiValueProvider extends IGuiValueProvider {

        boolean isGuiValueActive(String var1);

    }

}
