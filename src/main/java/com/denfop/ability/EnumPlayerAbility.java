package com.denfop.ability;

public enum EnumPlayerAbility {

    PICKAXE_VEIN("pickaxe_vein", "VEIN", 100, 1, 0xFF4FC3F7, 0xFF122030),
    AXE_TREE("axe_tree", "TREE", 100, 1, 0xFF73D673, 0xFF142416);

    private final String id;
    private final String hudLabel;
    private final int defaultCooldownTicks;
    private final int minimumFoodCost;
    private final int accentColor;
    private final int panelColor;

    EnumPlayerAbility(
            final String id,
            final String hudLabel,
            final int defaultCooldownTicks,
            final int minimumFoodCost,
            final int accentColor,
            final int panelColor
    ) {
        this.id = id;
        this.hudLabel = hudLabel;
        this.defaultCooldownTicks = defaultCooldownTicks;
        this.minimumFoodCost = minimumFoodCost;
        this.accentColor = accentColor;
        this.panelColor = panelColor;
    }

    public String getId() {
        return id;
    }

    public String getHudLabel() {
        return hudLabel;
    }

    public int getDefaultCooldownTicks() {
        return defaultCooldownTicks;
    }

    public int getMinimumFoodCost() {
        return minimumFoodCost;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public int getPanelColor() {
        return panelColor;
    }
}
