package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.IItemTab;
import com.denfop.IUCore;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class BaseArmor extends ArmorItem implements IItemTab {
    private final int render;
    private final String armor_type;
    private String nameItem;

    public BaseArmor(ArmorMaterial p_40386_, Type slot, String name_type) {
        super(p_40386_, slot, new Properties());
        this.render = slot.getSlot().getIndex();
        this.armor_type = name_type;

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
            this.nameItem ="item."+ pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    @Override
    public  String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (this.render != 1) {
            return Constants.TEXTURES + ":" + "textures/armor/" + armor_type + "_layer_1.png";
        } else {
            return Constants.TEXTURES + ":" + "textures/armor/" + armor_type + "_layer_2.png";
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.add(new ItemStack(this));
        }
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }
}
