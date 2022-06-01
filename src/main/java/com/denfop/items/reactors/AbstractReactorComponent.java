package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemIC2;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractReactorComponent extends ItemIC2 implements IReactorComponent, IModelRegister {

    private final String name;

    protected AbstractReactorComponent(String name) {
        super(null);
        this.setNoRepair();
        this.setCreativeTab(IUCore.ReactorsTab);
        this.setUnlocalizedName(name);
        BlocksItems.registerItem(this, IUCore.getIdentifier(name));
        IUCore.proxy.addIModelRegister(this);
        this.name = name;
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "reactors" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name) {
        registerModel(this, meta, name);
    }


    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {
    }

    public boolean acceptUraniumPulse(
            ItemStack stack,
            IReactor reactor,
            ItemStack pulsingStack,
            int youX,
            int youY,
            int pulseX,
            int pulseY,
            boolean heatrun
    ) {
        return false;
    }

    public boolean canStoreHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return false;
    }

    public int getMaxHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return 0;
    }

    public int getCurrentHeat(ItemStack stack, IReactor reactor, int x, int y) {
        return 0;
    }

    public int alterHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        return heat;
    }

    public float influenceExplosion(ItemStack stack, IReactor reactor) {
        return 0.0F;
    }

    public boolean canBePlacedIn(ItemStack stack, IReactor reactor) {
        return true;
    }

}
