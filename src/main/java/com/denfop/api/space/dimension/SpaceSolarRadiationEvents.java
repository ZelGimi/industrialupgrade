package com.denfop.api.space.dimension;

import com.denfop.IUCore;
import com.denfop.api.item.armor.HazmatLike;
import com.denfop.api.pollution.radiation.EnumLevelRadiation;
import com.denfop.network.packet.PacketRadiationUpdateValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = IUCore.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class SpaceSolarRadiationEvents {

    private static final int RADIATION_PULSE_TICKS = 40;

    private SpaceSolarRadiationEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTick(final PlayerTickEvent.Post event) {


        final Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }

        if ((player.tickCount % RADIATION_PULSE_TICKS) != 0) {
            return;
        }

        final SpaceDimensionProfile profile = SpaceBodyProfiles.byDimensionPath(
                player.level().dimension().location().getPath()
        );

        if (profile == null || !SpaceSolarRadiationLogic.hasSolarHazard(profile)) {
            return;
        }

        final BlockPos checkPos = player.blockPosition().above();
        final boolean canSeeSky = player.level().canSeeSky(checkPos);
        final boolean isDay = player.level().isDay();

        final float exposure = SpaceSolarRadiationLogic.computeExposure(
                profile,
                player.getY(),
                canSeeSky,
                isDay
        );

        if (exposure <= 0.0F) {
            return;
        }

        final EnumLevelRadiation requiredProtection = getRequiredProtectionLevel(exposure);


        final double dose = getDosePerPulse(exposure);
        if (dose <= 0.0D) {
            return;
        }

        final double current = player.getPersistentData().getDouble("radiation");
        final double updated = current + dose;

        player.getPersistentData().putDouble("radiation", updated);

        if (player instanceof ServerPlayer) {
            new PacketRadiationUpdateValue(player, updated);
        }
    }

    private static EnumLevelRadiation getRequiredProtectionLevel(final float exposure) {
        if (exposure >= 1.05F) {
            return EnumLevelRadiation.VERY_HIGH;
        }
        if (exposure >= 0.75F) {
            return EnumLevelRadiation.HIGH;
        }
        if (exposure >= 0.40F) {
            return EnumLevelRadiation.MEDIUM;
        }
        if (exposure >= 0.15F) {
            return EnumLevelRadiation.DEFAULT;
        }
        return EnumLevelRadiation.LOW;
    }

    private static double getDosePerPulse(final float exposure) {
        if (exposure >= 1.10F) {
            return 0.28D;
        }
        if (exposure >= 0.80F) {
            return 0.14D;
        }
        if (exposure >= 0.50F) {
            return 0.07D;
        }
        if (exposure >= 0.22F) {
            return 0.025D;
        }
        return 0.008D;
    }
}