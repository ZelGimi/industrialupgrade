package com.denfop.client.intro;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public final class IntroLocalization {

    private static final String INTRO_PREFIX = "intro.";

    private IntroLocalization() {
    }

    public static boolean isTranslationKey(String value) {
        return value != null && value.startsWith(INTRO_PREFIX);
    }

    public static String text(String value) {
        if (value == null) {
            return "";
        }
        if (isTranslationKey(value)) {
            return I18n.exists(value) ? I18n.get(value) : value;
        }
        return value;
    }

    public static String tr(String key) {
        return I18n.exists(key) ? I18n.get(key) : key;
    }

    public static String tr(String key, Object... args) {
        return I18n.exists(key) ? I18n.get(key, args) : key;
    }

    public static Component component(String value) {
        if (isTranslationKey(value)) {
            return Component.translatable(value);
        }
        return Component.literal(text(value));
    }
}
