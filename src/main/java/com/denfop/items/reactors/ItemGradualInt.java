//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.api.item.ICustomDamageItem;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.item.BaseElectricItem;
import ic2.core.item.ItemIC2;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGradualInt extends ItemIC2 implements ICustomDamageItem, IModelRegister {

    private static final boolean alwaysShowDurability = true;
    private static final String nbtKey = "advDmg";
    private final int maxDamage;
    private final String name;

    public ItemGradualInt(String name, int maxDamage) {
        super(null);
        this.setNoRepair();
        this.maxDamage = maxDamage;
        this.name = name;
        this.setCreativeTab(IUCore.ItemTab);
        this.setUnlocalizedName(name);
        BlocksItems.registerItem(this, IUCore.getIdentifier(name));
        IUCore.proxy.addIModelRegister(this);
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
        registerModel(this, meta, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name, extraName);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name, String extraName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name, extraName));
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name, String extraName) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');

        loc.append("reactors").append("/").append(name);
        return new ModelResourceLocation(loc.toString(), null);
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return (double) this.getCustomDamage(stack) / (double) this.getMaxCustomDamage(stack);
    }

    public boolean isDamageable() {
        return true;
    }

    public boolean isDamaged(ItemStack stack) {
        return this.getCustomDamage(stack) > 0;
    }

    public int getDamage(ItemStack stack) {
        return this.getCustomDamage(stack);
    }

    public int getCustomDamage(ItemStack stack) {
        return !stack.hasTagCompound() ? 0 : stack.getTagCompound().getInteger("advDmg");
    }

    public int getMaxDamage(ItemStack stack) {
        return this.getMaxCustomDamage(stack);
    }

    public int getMaxCustomDamage(ItemStack stack) {
        return this.maxDamage;
    }

    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getCustomDamage(stack);
        if (damage != prev && BaseElectricItem.logIncorrectItemDamaging) {
            IC2.log.warn(
                    LogCategory.Armor,
                    new Throwable(),
                    "Detected invalid gradual item damage application (%d):",
                    damage - prev
            );
        }

    }

    public void setCustomDamage(ItemStack stack, int damage) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        nbt.setInteger("advDmg", damage);
    }

    public boolean applyCustomDamage(ItemStack stack, int damage, EntityLivingBase src) {
        this.setCustomDamage(stack, this.getCustomDamage(stack) + damage);
        return true;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            this.setCustomDamage(stack, 0);
            subItems.add(stack);
        }
    }

}
