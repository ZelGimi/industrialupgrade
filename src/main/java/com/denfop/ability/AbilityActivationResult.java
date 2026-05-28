package com.denfop.ability;

import net.minecraft.network.chat.Component;

public record AbilityActivationResult(
        boolean success,
        int brokenBlocks,
        int foodCost,
        int cooldownTicks,
        Component message
) {

    public static AbilityActivationResult success(
            final int brokenBlocks,
            final int foodCost,
            final int cooldownTicks
    ) {
        return new AbilityActivationResult(true, brokenBlocks, foodCost, cooldownTicks, null);
    }

    public static AbilityActivationResult fail(final Component message) {
        return new AbilityActivationResult(false, 0, 0, 0, message);
    }
}
