package com.denfop.ability;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class AbilityClientState {

    private static final EnumMap<EnumPlayerAbility, Long> CLIENT_END_TICKS = new EnumMap<>(EnumPlayerAbility.class);

    private AbilityClientState() {
    }

    public static void apply(final Map<EnumPlayerAbility, Integer> remainingTicks) {
        final Minecraft mc = Minecraft.getInstance();
        final long now = mc.level != null ? mc.level.getGameTime() : 0L;

        for (final EnumPlayerAbility ability : EnumPlayerAbility.values()) {
            final int remaining = remainingTicks.getOrDefault(ability, 0);
            CLIENT_END_TICKS.put(ability, now + Math.max(0, remaining));
        }
    }

    public static int getRemainingTicks(final EnumPlayerAbility ability) {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return 0;
        }

        final long now = mc.level.getGameTime();
        final long end = CLIENT_END_TICKS.getOrDefault(ability, 0L);
        return (int) Math.max(0L, end - now);
    }

    public static float getReadyProgress(final EnumPlayerAbility ability) {
        final int remaining = getRemainingTicks(ability);
        final int total = Math.max(1, ability.getDefaultCooldownTicks());
        return remaining <= 0 ? 1.0F : 1.0F - (remaining / (float) total);
    }
}
