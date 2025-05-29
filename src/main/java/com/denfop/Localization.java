package com.denfop;

import net.minecraft.network.chat.Component;

public class Localization {

    public Localization() {
    }


    public static String translate(String key) {
        return Component.translatable(key).getString();
    }

    public static String translate(String key, Object... args) {
        return Component.translatable(key, args).getString();
    }

}
