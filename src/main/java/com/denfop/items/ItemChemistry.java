package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IDamageItem;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemChemistry extends Item implements IDamageItem, IModelRegister {

    private final String name;
    private final String path;


    public ItemChemistry(String name) {
        super();
        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(1);

        this.name = name;
        setMaxDamage(20000);
        this.path = "chemistry";
        setUnlocalizedName(name);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu.").replace(".name", ""));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + path + "/" + this.name, null)
        );
    }


    @Override
    public int getCustomDamage(ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        return nbt.getInteger("advDmg");
    }

    @Override
    public int getMaxCustomDamage(ItemStack stack) {
        return 250;
    }


    @Override
    public void setCustomDamage(ItemStack stack, int damage) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setInteger("advDmg", damage);
        int maxStackDamage = stack.getMaxDamage();
        if (maxStackDamage > 2) {

            stack.setItemDamage(1 + (damage / 250 * ((maxStackDamage - 2))));
        }
    }

    public boolean isDamaged(@Nonnull ItemStack stack) {
        return (getDamage(stack) > 1);
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.values()[0];
    }

    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean applyCustomDamage(ItemStack stack, int damage, EntityLivingBase src) {
        this.setCustomDamage(stack, this.getCustomDamage(stack) + damage);
        return true;
    }

}
