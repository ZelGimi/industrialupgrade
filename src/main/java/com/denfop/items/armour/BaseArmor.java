package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.core.init.BlocksItems;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BaseArmor extends ItemArmor implements IModelRegister {

    private final String name;
    private final String armor_type;
    private final int render;

    public BaseArmor(String name, ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String name_type) {
        super(material, renderIndex, slot);
        setUnlocalizedName(name);
        setCreativeTab(IUCore.EnergyTab);

        this.name = name;
        this.render = slot.getSlotIndex();
        this.armor_type = name_type;
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');
        loc.append("armour").append("/").append(name);

        return new ModelResourceLocation(loc.toString(), null);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {

        if (this.render != 2) {
            return Constants.TEXTURES + ":" + "textures/armor/" + armor_type + "_layer_1.png";
        } else {
            return Constants.TEXTURES + ":" + "textures/armor/" + armor_type + "_layer_2.png";
        }
    }

    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> getModelLocation1(name));

        ModelBakery.registerItemVariants(this, getModelLocation1(name));


    }

}
