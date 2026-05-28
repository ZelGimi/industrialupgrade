package com.denfop.client.intro;

import org.jetbrains.annotations.Nullable;

public class IntroPartnerEntry {

    private final String name;
    private final String subtitle;
    @Nullable
    private final String iconSource;

    public IntroPartnerEntry(String name, String subtitle, @Nullable String iconSource) {
        this.name = name;
        this.subtitle = subtitle;
        this.iconSource = iconSource;
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    @Nullable
    public String getIconSource() {
        return iconSource;
    }
}