package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemGear<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IItemTag {
    public ItemGear(T element) {
        super(new Item.Properties().tab(IUCore.RecourseTab), element);
    }

    @Override
    public Item getItem() {
        return this;
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
        return new String[]{"forge:gears/" + name, "forge:gears"};
    }

    public enum Types implements ISubEnum {
        mikhail(0),
        aluminium(1),
        vanady(2),
        wolfram(3),
        invar(4),
        caravky(5),
        cobalt(6),
        magnesium(7),
        nickel(8),
        platium(9),
        titanium(10),
        chromium(11),
        spinel(12),
        electrium(13),
        silver(14),
        zinc(15),
        manganese(16),
        iridium(17),
        germanium(18),
        osmium(19),
        tantalum(20),
        cadmium(21),
        arsenic(22),
        barium(23),
        bismuth(24),
        gadolinium(25),
        gallium(26),
        hafnium(27),
        yttrium(28),
        molybdenum(29),
        neodymium(30),
        niobium(31),
        palladium(32),
        polonium(33),
        strontium(34),
        thallium(35),
        zirconium(36),
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
            return "gear";
        }

        public int getId() {
            return this.ID;
        }
    }
}
