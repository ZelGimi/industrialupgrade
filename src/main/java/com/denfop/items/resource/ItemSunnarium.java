package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.SubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemSunnarium<T extends Enum<T> & SubEnum> extends ItemMain<T> {
    public ItemSunnarium(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    public enum Types implements SubEnum {
        sunnarium_enriched(0),
        sunnarium_enriched_plate(1),
        sunnarium_plate(2),
        sunnarium2(3),
        sunnariumpart(4);


        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
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
            return "itemsunnarium";
        }

        public int getId() {
            return this.ID;
        }
    }

}
