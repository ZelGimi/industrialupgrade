package com.denfop.items.resource;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IHazmatLike;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.reactors.IRadioactiveItemType;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemNuclearResource extends ItemSubTypes<ItemNuclearResource.Types> implements IModelRegister {

    protected static final String NAME = "nuclearresource";

    public ItemNuclearResource() {
        super(Types.class);
        this.setCreativeTab(IUCore.ReactorsTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    public void onUpdate(ItemStack stack, World world, Entity rawEntity, int slotIndex, boolean isCurrentItem) {
        Item item = stack.getItem();
        if (item instanceof ItemSubTypes) {
            Types rawType = ((ItemSubTypes<Types>) item).getType(stack);
            if (rawType != null) {
                if (rawEntity instanceof EntityLivingBase) {
                    EntityLivingBase entity = (EntityLivingBase) rawEntity;
                    if (!IHazmatLike.hasCompleteHazmat(entity)) {
                        IUPotion.radiation.applyTo(
                                entity,
                                ((IRadioactiveItemType) rawType).getRadiationDuration() * 20,
                                ((IRadioactiveItemType) rawType).getRadiationAmplifier()
                        );
                    }
                }
            }
        }
    }

    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {

            for (final Types type : this.typeProperty.getAllowedValues()) {
                subItems.add(this.getItemStackUnchecked(type));
            }

        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item stack, final int meta, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements ISubEnum, IRadioactiveItemType {

        uranium(0, 60, 100),
        uranium_235(1, 150, 100),
        uranium_238(2, 10, 90),
        plutonium(3, 150, 100),
        mox(4, 300, 100),
        small_uranium_235(5, 150, 100),
        small_uranium_238(6, 10, 90),
        small_plutonium(7, 150, 100),
        uranium_pellet(8, 60, 100),
        mox_pellet(9, 300, 100),
        rtg_pellet(10, 2, 90);

        private final int id;
        private final int radLen;
        private final int radAmplifier;

        Types(int id, int radLen, int radAmplifier) {
            this.id = id;
            this.radLen = radLen;
            this.radAmplifier = radAmplifier;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name();
        }

        public int getId() {
            return this.id;
        }

        public int getRadiationDuration() {
            return this.radLen;
        }

        public int getRadiationAmplifier() {
            return this.radAmplifier;
        }

    }

}
