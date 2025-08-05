package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemSunnariumPanel<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemSunnariumPanel(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    public enum Types implements ISubEnum {
        hsp(0),
        usp(1),
        qsp(2),
        spsp(3),
        psp(4),
        ssp(5),
        admsp(6),
        phsp(7),
        nsp(8),
        bsp(9),
        adspp(10),
        gsp(11),
        ksp(12);

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


        @Override
        public String getMainPath() {
            return "itemsunnariumpanel";
        }

        public int getId() {
            return this.ID;
        }
    }
}
