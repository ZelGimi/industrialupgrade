package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.MainConfig;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.IPseudoDamageItem;
import ic2.core.util.ConfigUtil;
import ic2.core.util.LogCategory;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseElectricItem extends Item implements IPseudoDamageItem, IElectricItem, IItemHudInfo {

    public static final boolean logIncorrectItemDamaging = ConfigUtil.getBool(MainConfig.get(), "debug/logIncorrectItemDamaging");
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int tier;
    public String name;

    public BaseElectricItem(String name, double maxCharge, double transferLimit, int tier) {
        super();
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setCreativeTab(IUCore.EnergyTab);
        this.name = name;
        setUnlocalizedName(name);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);

    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name, String extraName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name, extraName));
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name, String extraName) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');

        loc.append("energy").append("/").append(name);
        return new ModelResourceLocation(loc.toString(), null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name) {
        registerModel(this, meta, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name, extraName);
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public double getMaxCharge(ItemStack stack) {
        return this.maxCharge;
    }

    public int getTier(ItemStack stack) {
        return this.tier;
    }

    public double getTransferLimit(ItemStack stack) {
        return this.transferLimit;
    }

    public List<String> getHudInfo(ItemStack stack, boolean advanced) {
        List<String> info = new ArrayList<>();
        info.add(ElectricItem.manager.getToolTip(stack));
        return info;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            ElectricItemManager.addChargeVariants(this, subItems);
        }
    }

    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);
        if (damage != prev && logIncorrectItemDamaging) {
            IC2.log.warn(
                    LogCategory.Armor,
                    new Throwable(),
                    "Detected invalid armor damage application (%d):",
                    damage - prev
            );
        }

    }

    public void setStackDamage(ItemStack stack, int damage) {
        super.setDamage(stack, damage);
    }

}
