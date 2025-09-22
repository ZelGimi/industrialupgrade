package com.denfop.items.space;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemSpace extends ItemSubTypes<ItemSpace.Type> implements IModelRegister {

    protected static final String NAME = "itemspace";

    public ItemSpace() {
        super(Type.class);
        this.setCreativeTab(IUCore.RecourseTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item stack, final int meta, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":itemspace/" + Type.getFromID(meta).getName(), null)
        );
    }

    public enum Type implements ISubEnum {
        ariel_boulder(),
        asteroids_boulder(),
        callisto_boulder(),
        ceres_boulder(),
        charon_boulder(),
        deimos_boulder(),
        dione_boulder(),
        enceladus_boulder(),
        eris_boulder(),
        europe_boulder(),
        ganymede_boulder(),
        haumea_boulder(),
        io_boulder(),
        makemake_boulder(),
        mars_boulder(),
        mercury_boulder(),
        mimas_boulder(),
        miranda_boulder(),
        moon_boulder(),
        oberon_boulder(),
        phobos_boulder(),
        pluto_boulder(),
        proteus_boulder(),
        rhea_boulder(),
        tethys_boulder(),
        titan_boulder(),
        titania_boulder(),
        triton_boulder(),
        umbriel_boulder(),
        venus_boulder(),
        ariel_pebble(),
        asteroids_pebble(),
        callisto_pebble(),
        ceres_pebble(),
        charon_pebble(),
        deimos_pebble(),
        dione_pebble(),
        enceladus_pebble(),
        eris_pebble(),
        europe_pebble(),
        ganymede_pebble(),
        haumea_pebble(),
        io_pebble(),
        makemake_pebble(),
        mars_pebble(),
        mercury_pebble(),
        mimas_pebble(),
        miranda_pebble(),
        moon_pebble(),
        oberon_pebble(),
        phobos_pebble(),
        pluto_pebble(),
        proteus_pebble(),
        rhea_pebble(),
        tethys_pebble(),
        titan_pebble(),
        titania_pebble(),
        triton_pebble(),
        umbriel_pebble(),
        venus_pebble(),

        ;

        private final String name;

        Type() {
            this.name = this.name().toLowerCase(Locale.US);
        }

        public static Type getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ordinal();
        }
    }

}
