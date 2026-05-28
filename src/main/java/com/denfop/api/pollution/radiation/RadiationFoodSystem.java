package com.denfop.api.pollution.radiation;

import com.denfop.network.packet.PacketRadiationUpdateValue;
import com.denfop.potion.IUPotion;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Locale;

public class RadiationFoodSystem {

    private static final int CONTAMINATION_INTERVAL_TICKS = 120;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {


        Player player = event.getEntity();
        if (player.level().isClientSide || player.level().dimension() != Level.OVERWORLD) {
            return;
        }

        if (player.level().getGameTime() % CONTAMINATION_INTERVAL_TICKS != 0L) {
            return;
        }

        RadiationSystem system = RadiationSystem.rad_system;
        if (system == null) {
            return;
        }

        Radiation radiation = system.getMap().get(new ChunkPos(player.blockPosition()));
        if (radiation == null) {
            return;
        }

        contaminateInventoryConsumables(player, radiation.getLevel());
    }

    private void contaminateInventoryConsumables(Player player, EnumLevelRadiation level) {
        Inventory inventory = player.getInventory();
        boolean changed = false;

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!RadiationFoodHelper.isConsumableCandidate(stack)) {
                continue;
            }

            double increment = RadiationFoodHelper.getContaminationIncrement(level, stack);
            if (increment <= 0.0D) {
                continue;
            }

            RadiationFoodHelper.addFoodRadiationDose(stack, increment);

            if (RadiationFoodHelper.shouldSpoilIntoRottenFlesh(stack)) {
                ItemStack spoiled = RadiationFoodHelper.createSpoiledStack(stack);
                inventory.setItem(slot, spoiled);
            }

            changed = true;
        }

        if (changed) {
            inventory.setChanged();
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.inventoryMenu.broadcastChanges();
            }
        }
    }

    @SubscribeEvent
    public void onItemConsumed(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        ItemStack consumed = event.getItem();
        if (!RadiationFoodHelper.isConsumableCandidate(consumed)) {
            return;
        }

        double foodDose = RadiationFoodHelper.getFoodRadiationDose(consumed);
        if (foodDose <= 0.0D) {
            return;
        }

        double current = player.getPersistentData().getDouble("radiation");
        double updated = current + foodDose;
        player.getPersistentData().putDouble("radiation", updated);
        new PacketRadiationUpdateValue(player, updated);
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!RadiationFoodHelper.isConsumableCandidate(stack)) {
            return;
        }

        double dose = RadiationFoodHelper.getFoodRadiationDose(stack);
        if (dose <= 0.0D) {
            return;
        }

        double threshold = RadiationFoodHelper.getSpoilThreshold(stack);
        ChatFormatting color = RadiationFoodHelper.getDoseFormatting(dose, threshold);

        event.getToolTip().add(
                Component.translatable("iu.tooltip.radiation_food.contaminated")
                        .withStyle(ChatFormatting.GRAY)
        );

        event.getToolTip().add(
                Component.translatable(
                                "iu.tooltip.radiation_food.category",
                                Component.translatable(RadiationFoodHelper.getCategory(stack).getTranslationKey())
                        )
                        .withStyle(ChatFormatting.DARK_GRAY)
        );

        event.getToolTip().add(
                Component.translatable("iu.tooltip.radiation_food.dose", RadiationFoodHelper.formatDose(dose))
                        .withStyle(color)
        );

        event.getToolTip().add(
                Component.translatable("iu.tooltip.radiation_food.threshold", RadiationFoodHelper.formatDose(threshold))
                        .withStyle(ChatFormatting.GRAY)
        );

        if (RadiationFoodHelper.isWaterLikeCategory(stack)) {
            if (dose >= threshold) {
                event.getToolTip().add(
                        Component.translatable("iu.tooltip.radiation_food.heavily_irradiated_drink")
                                .withStyle(ChatFormatting.RED)
                );
            }
            return;
        }

        if (!stack.is(Items.ROTTEN_FLESH) && dose >= threshold) {
            event.getToolTip().add(
                    Component.translatable("iu.tooltip.radiation_food.spoiled")
                            .withStyle(ChatFormatting.DARK_RED)
            );
        } else if (!stack.is(Items.ROTTEN_FLESH) && dose >= threshold * 0.75D) {
            event.getToolTip().add(
                    Component.translatable("iu.tooltip.radiation_food.spoils_soon")
                            .withStyle(ChatFormatting.RED)
            );
        }
    }
    private static final String ANTI_RAD_COOLDOWN_END_TAG = "iu_anti_rad_food_cooldown_end";
    private static final int COOLDOWN_TICKS = 1200;

    private static final double GOLDEN_CARROT_MAX_DOSE = 1.0D;
    private static final double GOLDEN_APPLE_MAX_DOSE = 2.0D;

    @SubscribeEvent
    public void onFinishUse(LivingEntityUseItemEvent.Finish event) {
        LivingEntity living = event.getEntity();
        if (!(living instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        ItemStack consumed = event.getItem();
        if (consumed.isEmpty()) {
            return;
        }

        Item item = consumed.getItem();
        if (!isGoldenAntiRadiationFood(item)) {
            return;
        }

        long gameTime = player.level().getGameTime();
        long cooldownEnd = player.getPersistentData().getLong(ANTI_RAD_COOLDOWN_END_TAG);

        if (cooldownEnd > gameTime) {
            int secondsLeft = (int) Math.ceil((cooldownEnd - gameTime) / 20.0D);
            sendStatus(
                    player,
                    Component.translatable("iu.message.anti_rad_food.cooldown", secondsLeft)
                            .withStyle(ChatFormatting.YELLOW)
            );
            return;
        }

        double currentRadiation = player.getPersistentData().getDouble("radiation");
        if (currentRadiation <= 0.0D) {
            return;
        }

        double maxTreatableDose = getMaxTreatableDose(item);
        if (currentRadiation > maxTreatableDose) {
            sendStatus(
                    player,
                    Component.translatable("iu.message.anti_rad_food.too_high", formatDose(maxTreatableDose))
                            .withStyle(ChatFormatting.RED)
            );
            return;
        }

        player.getPersistentData().putDouble("radiation", 0.0D);
        new PacketRadiationUpdateValue(player, 0.0D);

        if (player.hasEffect(IUPotion.rad)) {
            player.removeEffect(IUPotion.rad);
        }

        long newCooldownEnd = gameTime + COOLDOWN_TICKS;
        player.getPersistentData().putLong(ANTI_RAD_COOLDOWN_END_TAG, newCooldownEnd);

        applyVisualCooldown(player);

        sendStatus(
                player,
                Component.translatable("iu.message.anti_rad_food.cleansed")
                        .withStyle(ChatFormatting.GREEN)
        );
    }

    private static boolean isGoldenAntiRadiationFood(Item item) {
        return item == Items.GOLDEN_CARROT
                || item == Items.GOLDEN_APPLE
                || item == Items.ENCHANTED_GOLDEN_APPLE;
    }

    private static double getMaxTreatableDose(Item item) {
        if (item == Items.GOLDEN_CARROT) {
            return GOLDEN_CARROT_MAX_DOSE;
        }
        return GOLDEN_APPLE_MAX_DOSE;
    }

    private static void applyVisualCooldown(Player player) {
        player.getCooldowns().addCooldown(Items.GOLDEN_CARROT, COOLDOWN_TICKS);
        player.getCooldowns().addCooldown(Items.GOLDEN_APPLE, COOLDOWN_TICKS);
        player.getCooldowns().addCooldown(Items.ENCHANTED_GOLDEN_APPLE, COOLDOWN_TICKS);
    }

    private static void sendStatus(Player player, Component component) {
        if (player instanceof ServerPlayer) {
            player.displayClientMessage(component, true);
        }
    }

    private static String formatDose(double dose) {
        return String.format(Locale.ROOT, "%.1f", dose);
    }
}