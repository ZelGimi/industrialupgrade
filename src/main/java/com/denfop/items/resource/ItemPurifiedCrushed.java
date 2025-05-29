package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemPurifiedCrushed<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IItemTag {
    public ItemPurifiedCrushed(T element) {
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
            case 2:
                name = "vanady";
                break;


        }
        return new String[]{"forge:purifiedcrushed/" + name, "forge:purifiedcrushed"};
    }

    public enum Types implements ISubEnum {
        mikhail(0),
        aluminium(1),
        vanadium(2),
        wolfram(3),
        cobalt(6),
        magnesium(7),
        nickel(8),
        platinum(9),
        titanium(10),
        chromium(11),
        spinel(12),
        silver(14),
        zinc(15),
        manganese(16),
        iridium(17),
        germanium(18),
        copper(19),
        gold(20),
        iron(21),
        lead(22),
        tin(23),
        uranium(24),
        osmium(25),
        tantalum(26),
        cadmium(27),
        arsenic(28),
        barium(29),
        bismuth(30),
        gadolinium(31),
        gallium(32),
        hafnium(33),
        yttrium(34),
        molybdenum(35),
        neodymium(36),
        niobium(37),
        palladium(38),
        polonium(39),
        strontium(40),
        thallium(41),
        zirconium(42);

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
            return "purifiedcrushed";
        }

        public int getId() {
            return this.ID;
        }
    }

}
