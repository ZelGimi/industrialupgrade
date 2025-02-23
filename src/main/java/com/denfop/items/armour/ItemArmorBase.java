package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorBase extends ItemArmor implements IModelRegister {


    private final String name = "";
    public String armorName;

    public ItemArmorBase(
            ItemArmor.ArmorMaterial armorMaterial,
            String armorName,
            EntityEquipmentSlot armorType
    ) {
        super(armorMaterial, -1, armorType);
        this.armorName = armorName;
        this.setNoRepair();
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "armour" + "/" + name + extraName;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, getModelLocation1(this.name, ""));
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        int suffix1 = this.armorType == EntityEquipmentSlot.LEGS ? 2 : 1;
        return Constants.MOD_ID + ":textures/armor/" + this.armorName + '_' + suffix1 + ".png";
    }


    public String getUnlocalizedName() {
        return Constants.ABBREVIATION + "." + super.getUnlocalizedName().substring(5);
    }

    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName();
    }

    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return this.getUnlocalizedName(stack);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return Localization.translate(this.getUnlocalizedName(stack));
    }

    protected boolean isInCreativeTab(CreativeTabs tab) {
        return super.isInCreativeTab(tab);
    }

    public boolean isMetalArmor(ItemStack itemstack, EntityPlayer player) {
        return true;
    }

}
