package com.denfop.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.EnumMap;
import java.util.Map;

public final class AbilityData {

    public static final String ROOT_TAG = "iu_ability_data";

    private final EnumMap<EnumPlayerAbility, Long> cooldownEndTicks = new EnumMap<>(EnumPlayerAbility.class);

    public static AbilityData fromPlayer(final Player player) {
        final CompoundTag root = player.getPersistentData().getCompound(ROOT_TAG);
        return fromTag(root);
    }

    public static AbilityData fromTag(final CompoundTag tag) {
        final AbilityData data = new AbilityData();
        for (final EnumPlayerAbility ability : EnumPlayerAbility.values()) {
            if (tag.contains(ability.getId())) {
                data.cooldownEndTicks.put(ability, tag.getLong(ability.getId()));
            }
        }
        return data;
    }

    public CompoundTag toTag() {
        final CompoundTag tag = new CompoundTag();
        for (final Map.Entry<EnumPlayerAbility, Long> entry : cooldownEndTicks.entrySet()) {
            tag.putLong(entry.getKey().getId(), entry.getValue());
        }
        return tag;
    }

    public void saveToPlayer(final Player player) {
        player.getPersistentData().put(ROOT_TAG, this.toTag());
    }

    public void setCooldownEndTick(final EnumPlayerAbility ability, final long endTick) {
        cooldownEndTicks.put(ability, endTick);
    }

    public long getCooldownEndTick(final EnumPlayerAbility ability) {
        return cooldownEndTicks.getOrDefault(ability, 0L);
    }

    public int getRemainingTicks(final EnumPlayerAbility ability, final long now) {
        return (int) Math.max(0L, this.getCooldownEndTick(ability) - now);
    }

    public boolean isOnCooldown(final EnumPlayerAbility ability, final long now) {
        return this.getRemainingTicks(ability, now) > 0;
    }

    public void clearCooldown(final EnumPlayerAbility ability) {
        cooldownEndTicks.remove(ability);
    }
}
