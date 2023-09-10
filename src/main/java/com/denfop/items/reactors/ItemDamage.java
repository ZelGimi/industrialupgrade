package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IDamageItem;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
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

public class ItemDamage extends Item implements IDamageItem, IModelRegister {

    private final int maxDamage;
    private final String name;

    public ItemDamage(String name, int maxDamage) {
        super();
        this.setNoRepair();
        this.maxDamage = maxDamage;
        this.name = name;
        this.setCreativeTab(IUCore.ReactorsTab);
        this.setUnlocalizedName(name);
        Register.registerItem(this, IUCore.getIdentifier(name));
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                name;
        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
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


    public int getCustomDamage(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return 0;
        } else {
            assert stack.getTagCompound() != null;
            return stack.getTagCompound().getInteger("damage");
        }
    }


    public int getMaxCustomDamage(ItemStack stack) {
        return this.maxDamage;
    }

    public void setDamage(@Nonnull ItemStack stack, int damage) {

    }

    public void setCustomDamage(ItemStack stack, int damage) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setInteger("damage", damage);
    }

    public boolean applyCustomDamage(ItemStack stack, int damage, EntityLivingBase src) {
        this.setCustomDamage(stack, this.getCustomDamage(stack) - damage);
        return true;
    }

    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            subItems.add(stack);
        }
    }

}
