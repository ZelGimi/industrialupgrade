package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemRawMetals<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IItemTag {
    public ItemRawMetals(T element) {
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
                name = "vanadium";
                break;


        }
        return new String[]{"c:raw_materials/" + name, "c:raw_materials"};
    }

    public enum Types implements ISubEnum {
        mikhail(0),
        aluminium(1),
        vanadium(2),
        tungsten(3),
        cobalt(4),
        magnesium(5),
        nickel(6),
        platinum(7),
        titanium(8),
        chromium(9),
        spinel(10),
        silver(11),
        zinc(12),
        manganese(13),
        iridium(14),
        germanium(15),
        lead(19),
        tin(20),
        osmium(22),
        tantalum(23),
        cadmium(24),
        arsenic(25),
        barium(26),
        bismuth(27),
        gadolinium(28),
        gallium(29),
        hafnium(30),
        yttrium(31),
        molybdenum(32),
        neodymium(33),
        niobium(34),
        palladium(35),
        polonium(36),
        strontium(37),
        thallium(38),
        zirconium(39),
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

        @Override
        public String getSerializedName() {
            return "raw_" + name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getMainPath() {
            return "raw_metals";
        }

        public int getId() {
            return this.ID;
        }
    }

}
