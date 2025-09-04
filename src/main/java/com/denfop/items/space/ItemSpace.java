package com.denfop.items.space;

import com.denfop.IUCore;
import com.denfop.blocks.SubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemSpace<T extends Enum<T> & SubEnum> extends ItemMain<T> {
    public ItemSpace(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.SpaceTab;
    }

    public enum Types implements SubEnum {
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
        venus_pebble();

        private final String name;
        private final int ID;

        Types() {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = this.ordinal();
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getMainPath() {
            return "itemspace";
        }

        public int getId() {
            return this.ID;
        }
    }

}
