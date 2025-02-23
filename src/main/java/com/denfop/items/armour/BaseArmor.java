package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.register.Register;
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

import javax.annotation.Nonnull;


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
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "armour" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    public String getArmorTexture(
            @Nonnull ItemStack stack,
            @Nonnull Entity entity,
            @Nonnull EntityEquipmentSlot slot,
            @Nonnull String type
    ) {

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
