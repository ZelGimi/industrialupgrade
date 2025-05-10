package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemLens<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemLens(T element) {
        super(new Item.Properties().tab(IUCore.ItemTab), element);
    }

    public enum Types implements ISubEnum {
        aerlinse(0),
        earthlinse(1),
        netherlinse(2),
        endlinse(3),
        nightlinse(4),
        sunlinse(5),
        rainlinse(6),
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

        @Override
        public String getMainPath() {
            return "lens";
        }

        public int getId() {
            return this.ID;
        }
    }
}
