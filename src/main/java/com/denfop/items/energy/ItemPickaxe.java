package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPickaxe extends net.minecraft.item.ItemPickaxe implements IModelRegister {

    private final String name;

    public ItemPickaxe(String name) {
        super(ToolMaterial.DIAMOND);
        setUnlocalizedName(name);
        this.name = name;
        this.setMaxDamage((int) (ToolMaterial.IRON.getMaxUses() * 2.5));
        setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModelLocation(String name) {
        final String loc = Constants.MOD_ID +
                ':' + name;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, getModelLocation(name));
    }

}
