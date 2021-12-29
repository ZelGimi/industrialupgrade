package com.denfop.integration.de;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemIC2;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SSPDEItem extends ItemIC2 implements IModelRegister {

    private final String name;
    private final String path;

    public SSPDEItem(String name) {
        this(name, "");
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4) + ".name";
    }

    public SSPDEItem(String name, String path) {
        super(null);
        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(64);

        this.name = name;
        this.path = path;
        setUnlocalizedName(name);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public boolean hasEffect(final ItemStack stack) {
        return true;
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + path + this.name, null)
        );
    }

}
