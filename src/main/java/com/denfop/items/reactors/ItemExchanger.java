package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IDamageItem;
import com.denfop.register.Register;
import com.denfop.tiles.reactors.graphite.ICapacitor;
import com.denfop.tiles.reactors.graphite.ICapacitorItem;
import com.denfop.tiles.reactors.graphite.IExchanger;
import com.denfop.tiles.reactors.graphite.IExchangerItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemExchanger  extends Item implements  IModelRegister, IExchangerItem {

    private final double percent;
    private final int level;
    private final String name;

    public ItemExchanger(final String name, int level, double percent) {

        this.percent = percent;
        this.level=level;
        this.setNoRepair();
        this.name = name;
        this.setCreativeTab(IUCore.ReactorsTab);
        this.setUnlocalizedName("item_"+name);
        Register.registerItem(this, IUCore.getIdentifier("item_"+name));
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public double getPercent() {
        return percent;
    }

    public int getLevel() {
        return level;
    }


    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name) {
        registerModel(this, meta, name);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item", "iu").replace(".name", ""));
    }
    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "exchanger" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }
}
