package com.denfop.ability;

import com.denfop.network.packet.PacketSyncAbilityCooldowns;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumMap;
import java.util.Map;

public final class AbilityManager {

    private AbilityManager() {
    }

    public static IPlayerAbility getAbility(final ItemStack stack) {
        return AbilityToolHelper.resolveAbility(stack);
    }

    protected static BlockHitResult getPlayerPOVHitResult(Level pLevel, Player pPlayer, ClipContext.Fluid pFluidMode) {
        float f = pPlayer.getXRot();
        float f1 = pPlayer.getYRot();
        Vec3 vec3 = pPlayer.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = pPlayer.getReachDistance();
        Vec3 vec31 = vec3.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
        return pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, pPlayer));
    }

    public static void handleActivationPacket(final ServerPlayer player) {
        if (player == null || !player.isAlive()) {
            return;
        }

        final ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            return;
        }

        final IPlayerAbility ability = getAbility(stack);
        if (ability == null) {
            return;
        }

        final AbilityData data = AbilityData.fromPlayer(player);
        final long now = player.getLevel().getGameTime();

        if (data.isOnCooldown(ability.type(), now)) {
            player.displayClientMessage(Component.translatable("iu.ability.cooldown"), true);
            sync(player);
            return;
        }

        final BlockHitResult hitResult = getPlayerPOVHitResult(player.getLevel(), player, ClipContext.Fluid.NONE);
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            player.displayClientMessage(Component.translatable("iu.ability.no_target_block"), true);
            return;
        }

        final AbilityActivationResult result = ability.activate(player, stack, hitResult.getBlockPos());
        if (!result.success()) {
            if (result.message() != null) {
                player.displayClientMessage(result.message(), true);
            }
            sync(player);
            return;
        }

        if (!player.isCreative()) {
            consumeFood(player, result.foodCost());
        }

        data.setCooldownEndTick(ability.type(), now + result.cooldownTicks());
        data.saveToPlayer(player);

        player.swing(InteractionHand.MAIN_HAND, true);
        sync(player);
    }

    private static void consumeFood(final ServerPlayer player, final int amount) {
        if (amount <= 0) {
            return;
        }

        final int currentFood = player.getFoodData().getFoodLevel();
        final float currentSaturation = player.getFoodData().getSaturationLevel();

        player.getFoodData().setFoodLevel(Math.max(0, currentFood - amount));
        player.getFoodData().setSaturation(Math.max(0.0F, currentSaturation - amount * 0.5F));
    }

    public static void sync(final ServerPlayer player) {
        new PacketSyncAbilityCooldowns(player, buildRemainingTicksMap(player));
    }

    public static Map<EnumPlayerAbility, Integer> buildRemainingTicksMap(final ServerPlayer player) {
        final AbilityData data = AbilityData.fromPlayer(player);
        final long now = player.getLevel().getGameTime();
        final EnumMap<EnumPlayerAbility, Integer> result = new EnumMap<>(EnumPlayerAbility.class);

        for (final EnumPlayerAbility ability : EnumPlayerAbility.values()) {
            result.put(ability, data.getRemainingTicks(ability, now));
        }

        return result;
    }
}
