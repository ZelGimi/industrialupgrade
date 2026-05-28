package com.denfop.client.intro;

public class IntroAddonEntry {

    private final String name;
    private final String subtitle;
    private final String iconSource;
    private final String modId;

    public IntroAddonEntry(String name, String subtitle, String iconSource, String modId) {
        this.name = name;
        this.subtitle = subtitle;
        this.iconSource = iconSource;
        this.modId = modId;
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getIconSource() {
        return iconSource;
    }

    public String getModId() {
        return modId;
    }
}