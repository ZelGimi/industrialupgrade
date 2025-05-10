package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.IUCore;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemArmorBase extends ArmorItem {
    String armorName;
    String nameItem;

    public ItemArmorBase(ArmorMaterial p_40386_, String armorName, EquipmentSlot p_40387_) {
        super(p_40386_, p_40387_, new Properties().tab(IUCore.EnergyTab).stacksTo(1).setNoRepair());
        this.armorName = armorName;
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int suffix1 = slot == EquipmentSlot.LEGS ? 2 : 1;
        return Constants.MOD_ID + ":textures/armor/" + this.armorName + '_' + suffix1 + ".png";
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
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }
}
