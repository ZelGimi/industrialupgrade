package com.denfop.items.armour;

import com.denfop.IUCore;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemArmorNightvisionGoggles extends ItemArmorUtility implements EnergyItem {

    public ItemArmorNightvisionGoggles() {
        super("nightvision", Type.HELMET);
        this.armorName = "nightvision_goggles";
    }

    public boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            p_41392_.add(var4);
            p_41392_.add(new ItemStack(this, 1));
        }
    }

    public int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
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
            this.nameItem = "iu." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    public int getBarWidth(ItemStack stack) {

        return 13 - (int) (13.0F * Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        ));
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
        p_41423_.add(Component.literal("Nightvision Key: " + KeyboardClient.armormode.getKey().getDisplayName().getString()));

    }


    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public double getMaxEnergy(ItemStack stack) {
        return 200000.0;
    }

    public short getTierItem(ItemStack stack) {
        return 1;
    }

    public double getTransferEnergy(ItemStack stack) {
        return 200.0;
    }


    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
        if (p_41407_ >= Inventory.INVENTORY_SIZE && p_41407_ < Inventory.INVENTORY_SIZE + 4 && p_41406_ instanceof Player player)
            this.onArmorTick(p_41404_, p_41405_, (Player) p_41406_);
    }

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (world.isClientSide)
            return;
        CompoundTag nbtData = ModUtils.nbt(stack);
        boolean active = stack.getOrDefault(DataComponentsInit.NIGHT_VISION, false);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        if (IUCore.keyboard.isArmorKey(player) && toggleTimer == 0) {
            toggleTimer = 10;
            active = !active;
            if (!player.level().isClientSide()) {
                stack.set(DataComponentsInit.NIGHT_VISION, active);
                if (active) {
                    IUCore.proxy.messagePlayer(player, "Nightvision enabled.");
                } else {
                    IUCore.proxy.messagePlayer(player, "Nightvision disabled.");
                }
            }
        }

        if (!player.level().isClientSide() && toggleTimer > 0) {
            --toggleTimer;
            nbtData.putByte("toggleTimer", toggleTimer);
        }

        boolean ret = false;
        if (active && !player.level().isClientSide() && ElectricItem.manager.use(stack, 1.0, player)) {
            int skylight = player.level().getMaxLocalRawBrightness(player.blockPosition());
            if (skylight > 8) {
                IUCore.proxy.removePotion(player, MobEffects.NIGHT_VISION);
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, true, true));
            } else {
                IUCore.proxy.removePotion(player, MobEffects.BLINDNESS);
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, true, true));
            }

            ret = true;
        }

        if (ret) {
            player.containerMenu.broadcastChanges();
        }

    }


}
