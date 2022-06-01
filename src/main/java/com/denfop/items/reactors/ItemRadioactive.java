package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemIC2;
import ic2.core.item.type.IRadioactiveItemType;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRadioactive extends ItemIC2 implements IRadioactiveItemType, IModelRegister {

    protected final int radiationLength;
    protected final int amplifier;
    private final String name;
    private final String path;

    public ItemRadioactive(String name, int radiationLength1, int amplifier1) {
        super(null);
        this.setCreativeTab(IUCore.ReactorsTab);
        this.setMaxStackSize(64);
        setUnlocalizedName(name);
        this.name = name;
        this.radiationLength = radiationLength1;
        this.amplifier = amplifier1;
        this.path = "reactors";
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
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
                new ModelResourceLocation(Constants.MOD_ID + ":" + path + "/" + this.name, null)
        );
    }

    @Override
    public int getRadiationDuration() {
        return this.radiationLength;
    }

    @Override
    public int getRadiationAmplifier() {
        return this.amplifier;
    }

}
