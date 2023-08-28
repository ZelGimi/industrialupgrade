package com.denfop;

public class Localization {

    public Localization() {
    }


    public static String translate(String key) {
        return net.minecraft.util.text.translation.I18n.translateToLocal(key);
    }

    public static String translate(String key, Object... args) {
        return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(key, args);
    }

}
