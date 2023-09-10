package com.denfop.items.reactors;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemDepletedRod extends Item implements IReactorComponent, IModelRegister, IRadioactiveItemType {


    public final String name;

    public ItemDepletedRod(String name) {
        super();
        this.name = name.toLowerCase(Locale.US);
        setUnlocalizedName(name);


        this.setCreativeTab(IUCore.ReactorsTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);

        IUCore.proxy.addIModelRegister(this);
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
        if (extraName != null) {
            loc.append(extraName).append("/").append(name);
        } else {
            loc.append(name);
        }
        return new ModelResourceLocation(loc.toString(), null);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item", "iu").replace(".name", ""));
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, "depleted");
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name) {
        registerModel(this, meta, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name, extraName);
    }


    public String getUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName();
    }


    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if (!IHazmatLike.hasCompleteHazmat(entityLiving)) {
                IUPotion.radiation.applyTo(entityLiving, this.getRadiationDuration(), this.getRadiationAmplifier());
            }
        }

    }


    @Override
    public boolean canBePlacedIn(final ItemStack itemStack, final IAdvReactor AdvReactor) {
        return false;
    }

    @Override
    public int getRadiationDuration() {
        return 200;
    }

    @Override
    public int getRadiationAmplifier() {
        return 100;
    }

}
