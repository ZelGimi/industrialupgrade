package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemVerySmallDust<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IItemTag {
    public ItemVerySmallDust(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.RecourseTab;
    }

    @Override
    public String[] getTags() {
        String name = getElement().getName();
        switch (this.getElement().getId()) {
            case 3:
                name = "tungsten";
                break;
            case 9:
                name = "platinum";
                break;
            case 13:
                name = "electrum";
                break;

        }
        return new String[]{"c:verysmalldust/" + name, "c:verysmalldust"};
    }

    public enum Types implements ISubEnum {
        mikhail(0),
        aluminium(1),
        vanadium(2),
        tungsten(3),
        invar(4),
        caravky(5),
        cobalt(6),
        magnesium(7),
        nickel(8),
        platinum(9),
        titanium(10),
        chromium(11),
        spinel(12),
        electrum(13),
        silver(14),
        zinc(15),
        manganese(16),
        iridium(17),
        germanium(18),

        osmium(19),
        tantalum(20),
        cadmium(21);

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
            return "verysmalldust";
        }

        public int getId() {
            return this.ID;
        }
    }

}
