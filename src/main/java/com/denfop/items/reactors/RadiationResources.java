package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.items.armour.ItemArmorAdvHazmat;
import com.denfop.items.armour.ItemArmorImprovemedQuantum;
import ic2.core.IC2Potion;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemMulti;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.type.IRadioactiveItemType;
import ic2.core.ref.ItemName;
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

public class RadiationResources extends ItemMulti<RadiationResources.Types> implements IModelRegister, IRadioactiveItemType {

    protected static final String NAME = "radiationresources";

    public RadiationResources() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ReactorsTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving) && !ItemArmorAdvHazmat.hasCompleteHazmat(entityLiving) && !ItemArmorImprovemedQuantum.hasCompleteHazmat(
                    entityLiving)) {
                IC2Potion.radiation.applyTo(entityLiving, 150, 10);
            }
        }

    }

    @Override
    public void registerModels() {
        this.registerModels(null);
    }

    @Override
    public int getRadiationDuration() {
        return 200;
    }

    @Override
    public int getRadiationAmplifier() {
        return 100;
    }

    public enum Types implements IIdProvider {
        americium_gem(0),
        neptunium_gem(1),
        curium_gem(2),
        california_gem(3),
        rad_toriy(4),
        mendelevium_gem(5),
        berkelium_gem(6),
        einsteinium_gem(7),
        uran233_gem(8),

        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ID;
        }
    }

}
