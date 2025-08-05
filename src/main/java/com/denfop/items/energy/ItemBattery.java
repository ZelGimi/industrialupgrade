package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.item.IEnergyItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.BaseEnergyItem;
import com.denfop.items.IProperties;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemBattery extends BaseEnergyItem implements IProperties {

    private static final int maxLevel = 4;
    public final boolean wirelessCharge;

    public ItemBattery(double maxCharge, double transferLimit, int tier, boolean wirelessCharge) {
        super(new Properties().component(DataComponentsInit.MODE, 0), maxCharge, transferLimit, tier);
        this.wirelessCharge = wirelessCharge;
        IUCore.proxy.addProperties(this);
    }

    public ItemBattery(double maxCharge, double transferLimit, int tier) {
        super(new Properties().component(DataComponentsInit.MODE, 0), maxCharge, transferLimit, tier);
        this.wirelessCharge = false;
        IUCore.proxy.addProperties(this);

    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "item." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }


    @Override
    public void appendHoverText(
            ItemStack stack, @Nullable TooltipContext world, List<Component> tooltip, TooltipFlag flag
    ) {
        if (this.wirelessCharge) {
            int mode = stack.getOrDefault(DataComponentsInit.MODE, 0);
            if (mode > 4 || mode < 0) {
                mode = 0;
            }

            tooltip.add(Component.translatable("message.text.mode")
                    .append(": ")
                    .append(Component.translatable("message.battery.mode." + mode))
                    .withStyle(ChatFormatting.GREEN));

            if (!Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("press.lshift"));
            } else {
                tooltip.add(Component.translatable("iu.changemode_key")
                        .append(Component.translatable("iu.changemode_rcm1")));
            }
        }
        super.appendHoverText(stack, world, tooltip, flag);
    }


    @Override
    public void inventoryTick(ItemStack itemStack, Level world, net.minecraft.world.entity.Entity entity, int slot, boolean isSelected) {
        if (!(entity instanceof Player player) || world.isClientSide || !wirelessCharge) {
            return;
        }

        int mode = itemStack.getOrDefault(DataComponentsInit.MODE, 0);
        if (mode < 1 || mode > 4) return;

        if (world.getGameTime() % ((mode == 4) ? 40 : 10) == 0) {
            boolean transferred = false;

            switch (mode) {
                case 1 -> transferred = chargeItems(player.getInventory().items, itemStack);
                case 2 -> transferred = chargeItems(player.getInventory().items, itemStack);
                case 3 -> transferred = chargeItems(player.getInventory().armor, itemStack);
                case 4 -> {
                    transferred = chargeItems(player.getInventory().armor, itemStack);
                    if (world.getGameTime() % 40 == 0) {
                        transferred |= chargeItems(player.getInventory().items, itemStack);
                    }
                }
            }

            if (transferred) {
                player.containerMenu.broadcastChanges();
            }
        }
    }

    private boolean chargeItems(Iterable<ItemStack> inventory, ItemStack battery) {
        boolean transferred = false;
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty() && stack.getItem() instanceof IEnergyItem && !(stack.getItem() instanceof ItemBattery)) {
                double transfer = ElectricItem.manager.discharge(battery, 2.0D * this.transferLimit, Integer.MAX_VALUE, true, true, true);
                if (transfer > 0) {
                    transfer = ElectricItem.manager.charge(stack, transfer, Integer.MAX_VALUE, true, false);
                    if (transfer > 0) {
                        ElectricItem.manager.discharge(battery, transfer, Integer.MAX_VALUE, true, true, false);
                        transferred = true;
                    }
                }
            }
        }
        return transferred;
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide && wirelessCharge) {
            ItemStack stack = player.getItemInHand(hand);
            int mode = stack.get(DataComponentsInit.MODE);
            mode = (mode + 1) % 5;
            stack.set(DataComponentsInit.MODE, mode);
            player.displayClientMessage(
                    Component.literal(ChatFormatting.GREEN + Localization.translate("message.text.mode") + ": " +
                            Localization.translate("message.battery.mode." + mode)), true);
        }

        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide && ModUtils.getSize(stack) == 1) {
            if (ElectricItem.manager.getCharge(stack) > 0.0D) {
                boolean transferred = false;

                for (int i = 0; i < 9; ++i) {
                    ItemStack target = player.getInventory().items.get(i);
                    if (!target.isEmpty() && target.getItem() instanceof IEnergyItem && target != stack &&
                            !(ElectricItem.manager.discharge(target, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, true, true) > 0.0D)) {
                        double transfer = ElectricItem.manager.discharge(stack, 2.0D * this.transferLimit, Integer.MAX_VALUE, true, true, true);
                        if (transfer > 0.0D) {
                            transfer = ElectricItem.manager.charge(target, transfer, this.tier, true, false);
                            if (transfer > 0.0D) {
                                ElectricItem.manager.discharge(stack, transfer, Integer.MAX_VALUE, true, true, false);
                                transferred = true;
                            }
                        }
                    }
                }

                if (transferred) {
                    player.containerMenu.broadcastChanges();
                }
            }

            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }


    @Override
    public String[] properties() {
        return new String[]{"level"};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack stack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        double damage = ElectricItem.manager.getCharge(stack);
        double maxDamage = ElectricItem.manager.getMaxCharge(stack);
        return (float) ((int) (ItemBattery.maxLevel * damage / maxDamage) * 0.25);
    }


}
