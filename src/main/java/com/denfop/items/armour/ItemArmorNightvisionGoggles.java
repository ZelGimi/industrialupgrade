package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.api.item.IEnergyItem;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemArmorNightvisionGoggles extends ItemArmorUtility implements IEnergyItem {

    public ItemArmorNightvisionGoggles() {
        super("nightvision", EquipmentSlot.HEAD);
        this.armorName = "nightvision_goggles";
    }

    public boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    public int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }
    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem ="iu."+ pathBuilder.toString().split("\\.")[2];
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
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
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


    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int suffix = (slot == EquipmentSlot.LEGS) ? 2 : 1;
        CompoundTag nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return Constants.TEXTURES + ":textures/armor/" + "armor" + slot.ordinal() + "_" + nbtData.getString("mode") + ".png";
        }


        return Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + suffix + ".png";
    }

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (world.isClientSide)
            return;
        CompoundTag nbtData = ModUtils.nbt(stack);
        boolean active = nbtData.getBoolean("active");
        byte toggleTimer = nbtData.getByte("toggleTimer");
        if (IUCore.keyboard.isArmorKey(player) && toggleTimer == 0) {
            toggleTimer = 10;
            active = !active;
            if (!player.getLevel().isClientSide()) {
                nbtData.putBoolean("active", active);
                if (active) {
                    IUCore.proxy.messagePlayer(player, "Nightvision enabled.");
                } else {
                    IUCore.proxy.messagePlayer(player, "Nightvision disabled.");
                }
            }
        }

        if (!player.getLevel().isClientSide() && toggleTimer > 0) {
            --toggleTimer;
            nbtData.putByte("toggleTimer", toggleTimer);
        }

        boolean ret = false;
        if (active && !player.getLevel().isClientSide() && ElectricItem.manager.use(stack, 1.0, player)) {
            int skylight = player.getLevel().getMaxLocalRawBrightness(new BlockPos(player.position()));
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

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> subItems) {
        if (this.allowedIn(p_41391_)) {
            ElectricItemManager.addChargeVariants(this, subItems);
        }
    }


}
