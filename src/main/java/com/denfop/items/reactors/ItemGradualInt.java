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

import javax.annotation.Nonnull;

public class ItemGradualInt extends ItemIC2 implements ICustomDamageItem, IModelRegister {

    private final int maxDamage;
    private final String name;

    public ItemGradualInt(String name, int maxDamage) {
        super(null);
        this.setNoRepair();
        this.maxDamage = maxDamage;
        this.name = name;
        this.setCreativeTab(IUCore.ReactorsTab);
        this.setUnlocalizedName(name);
        BlocksItems.registerItem(this, IUCore.getIdentifier(name));
        IUCore.proxy.addIModelRegister(this);
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

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return true;
    }

    public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
        return (double) this.getCustomDamage(stack) / (double) this.getMaxCustomDamage(stack);
    }

    public boolean isDamageable() {
        return true;
    }

    public boolean isDamaged(@Nonnull ItemStack stack) {
        return this.getCustomDamage(stack) > 0;
    }

    public int getDamage(@Nonnull ItemStack stack) {
        return this.getCustomDamage(stack);
    }

    public int getCustomDamage(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return 0;
        } else {
            assert stack.getTagCompound() != null;
            return stack.getTagCompound().getInteger("advDmg");
        }
    }

    public int getMaxDamage(@Nonnull ItemStack stack) {
        return this.getMaxCustomDamage(stack);
    }

    public int getMaxCustomDamage(ItemStack stack) {
        return this.maxDamage;
    }

    public void setDamage(@Nonnull ItemStack stack, int damage) {
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

    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            this.setCustomDamage(stack, 0);
            subItems.add(stack);
        }
    }

}
