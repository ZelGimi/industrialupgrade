package com.denfop.tiles.bee;

public enum FrameAttributeLevel {
    SLOW_AGING_I(0.2, FrameAttribute.SLOW_AGING),
    SLOW_AGING_II(0.35, FrameAttribute.SLOW_AGING),
    SLOW_AGING_III(0.45, FrameAttribute.SLOW_AGING),

    PRODUCING_I(0.25, FrameAttribute.PRODUCING),
    PRODUCING_II(0.5, FrameAttribute.PRODUCING),
    PRODUCING_III(0.75, FrameAttribute.PRODUCING),

    SPEED_CROP_I(0.25, FrameAttribute.SPEED_CROP),
    SPEED_CROP_II(0.5, FrameAttribute.SPEED_CROP),
    SPEED_CROP_III(1, FrameAttribute.SPEED_CROP),

    SPEED_BIRTH_RATE_I(0.25, FrameAttribute.SPEED_BIRTH_RATE),
    SPEED_BIRTH_RATE_II(0.5, FrameAttribute.SPEED_BIRTH_RATE),
    SPEED_BIRTH_RATE_III(1, FrameAttribute.SPEED_BIRTH_RATE),

    CHANCE_CROSSING_I(3, FrameAttribute.CHANCE_CROSSING),
    CHANCE_CROSSING_II(5, FrameAttribute.CHANCE_CROSSING),
    CHANCE_CROSSING_III(8, FrameAttribute.CHANCE_CROSSING),
    STORAGE_JELLY_I(1.5, FrameAttribute.STORAGE_JELLY),
    STORAGE_JELLY_II(2, FrameAttribute.STORAGE_JELLY),
    STORAGE_JELLY_III(3, FrameAttribute.STORAGE_JELLY),
    STORAGE_FOOD_I(1.5, FrameAttribute.STORAGE_FOOD),
    STORAGE_FOOD_II(2, FrameAttribute.STORAGE_FOOD),
    STORAGE_FOOD_III(3, FrameAttribute.STORAGE_FOOD),
    CHANCE_HEALING_I(0.1, FrameAttribute.CHANCE_HEALING),
    CHANCE_HEALING_II(0.15, FrameAttribute.CHANCE_HEALING),
    CHANCE_HEALING_III(0.2, FrameAttribute.CHANCE_HEALING);

    private final double value;
    private final FrameAttribute attribute;

    FrameAttributeLevel(double value, FrameAttribute attribute) {
        this.value = value;
        this.attribute = attribute;
    }

    public double getValue() {
        return value;
    }

    public FrameAttribute getAttribute() {
        return attribute;
    }
}
