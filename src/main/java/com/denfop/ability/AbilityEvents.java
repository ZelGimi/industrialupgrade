package com.denfop.ability;

import com.denfop.IUCore;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = IUCore.MODID)
public final class AbilityEvents {

    private AbilityEvents() {
    }

    @SubscribeEvent
    public static void onLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AbilityManager.sync(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AbilityManager.sync(player);
        }
    }

    @SubscribeEvent
    public static void onChangedDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AbilityManager.sync(player);
        }
    }

    @SubscribeEvent
    public static void onClone(final PlayerEvent.Clone event) {
        if (!event.getOriginal().getPersistentData().contains(AbilityData.ROOT_TAG, Tag.TAG_COMPOUND)) {
            return;
        }

        event.getEntity().getPersistentData().put(
                AbilityData.ROOT_TAG,
                event.getOriginal().getPersistentData().getCompound(AbilityData.ROOT_TAG).copy()
        );
    }
}
