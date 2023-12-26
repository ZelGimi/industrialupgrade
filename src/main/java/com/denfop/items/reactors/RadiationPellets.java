package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IHazmatLike;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
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

public class RadiationPellets extends ItemSubTypes<RadiationPellets.Types> implements IModelRegister, IRadioactiveItemType {

    protected static final String NAME = "pellets";

    public RadiationPellets() {
        super(Types.class);
        this.setCreativeTab(IUCore.ReactorsTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if (!IHazmatLike.hasCompleteHazmat(entityLiving)) {
                IUPotion.radiation.applyTo(
                        entityLiving,
                        this.getRadiationDuration(),
                        this.getRadiationAmplifier()
                );
            }
        }

    }


    @Override
    public int getRadiationDuration() {
        return 200;
    }

    @Override
    public int getRadiationAmplifier() {
        return 100;
    }

    public enum Types implements ISubEnum {
        americium_pellet(0),
        neptunium_pellet(1),
        curium_pellet(2),
        california_pellet(3),
        rad_toriy_pellet(4),
        mendelevium_gem_pellet(5),
        berkelium_gem_pellet(6),
        einsteinium_gem_pellet(7),
        uran233_gem_pellet(8),
       proton_pellet(9),
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
