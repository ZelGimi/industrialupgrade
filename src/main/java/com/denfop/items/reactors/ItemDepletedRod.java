package com.denfop.items.reactors;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.IC2Potion;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemIC2;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.ref.IItemModelProvider;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemDepletedRod extends ItemIC2 implements IReactorComponent, IModelRegister {


    public final String name;

    public ItemDepletedRod(String name) {
        super(null);
        this.name = name.toLowerCase(Locale.US);
        setUnlocalizedName(name);


        this.setCreativeTab(IUCore.ItemTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);

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

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, "depleted");
    }

    @SideOnly(Side.CLIENT)
    public void doModelGuf() {
        ((IItemModelProvider) this).registerModels(null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name) {
        registerModel(this, meta, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name, extraName);
    }

    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)) {
                IC2Potion.radiation.applyTo(entityLiving, 200, 100);
            }
        }
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName();
    }


    @Override
    public void processChamber(final ItemStack itemStack, final IReactor iReactor, final int i, final int i1, final boolean b) {

    }

    @Override
    public boolean acceptUraniumPulse(
            final ItemStack itemStack,
            final IReactor iReactor,
            final ItemStack itemStack1,
            final int i,
            final int i1,
            final int i2,
            final int i3,
            final boolean b
    ) {
        return false;
    }

    @Override
    public boolean canStoreHeat(final ItemStack itemStack, final IReactor iReactor, final int i, final int i1) {
        return false;
    }

    @Override
    public int getMaxHeat(final ItemStack itemStack, final IReactor iReactor, final int i, final int i1) {
        return 0;
    }

    @Override
    public int getCurrentHeat(final ItemStack itemStack, final IReactor iReactor, final int i, final int i1) {
        return 0;
    }

    @Override
    public int alterHeat(final ItemStack itemStack, final IReactor iReactor, final int i, final int i1, final int i2) {
        return 0;
    }

    @Override
    public float influenceExplosion(final ItemStack itemStack, final IReactor iReactor) {
        return 0;
    }

    @Override
    public boolean canBePlacedIn(final ItemStack itemStack, final IReactor iReactor) {
        return false;
    }

}
