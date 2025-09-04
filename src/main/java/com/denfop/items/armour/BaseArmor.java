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

public class BaseArmor extends ArmorItem {
    private final int render;
    private final String armor_type;
    private String nameItem;

    public BaseArmor(ArmorMaterial p_40386_, EquipmentSlot slot, String name_type) {
        super(p_40386_, slot, new Properties().tab(IUCore.EnergyTab));
        this.render = slot.getIndex();
        this.armor_type = name_type;

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
            this.nameItem = "item." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (this.render != 1) {
            return Constants.TEXTURES + ":" + "textures/armor/" + armor_type + "_layer_1.png";
        } else {
            return Constants.TEXTURES + ":" + "textures/armor/" + armor_type + "_layer_2.png";
        }
    }
}
