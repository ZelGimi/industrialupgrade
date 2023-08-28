package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRadioactive extends Item implements IModelRegister {

    protected final int radiationLength;
    protected final int amplifier;
    private final String name;
    private final String path;

    public ItemRadioactive(String name, int radiationLength1, int amplifier1) {
        super();
        this.setCreativeTab(IUCore.ReactorsTab);
        this.setMaxStackSize(64);
        setUnlocalizedName(name);
        this.name = name;
        this.radiationLength = radiationLength1;
        this.amplifier = amplifier1;
        this.path = "reactors";
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item", "iu").replace(".name", ""));
    }

    @Override
    public void registerModels() {
        registerModel(0);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + path + "/" + this.name, null)
        );
    }


}
