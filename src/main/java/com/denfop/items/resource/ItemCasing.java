package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemCasing<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IItemTag {
    public ItemCasing(T element) {
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
            case 44:
                name = "adamantium";
                break;
            case 47:
                name = "meteoric";
                break;
            case 48:
                name = "mithril";
                break;

        }
        return new String[]{"c:casings/" + name, "c:casings"};
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
        bronze(19),
        copper(20),
        gold(21),
        iron(22),
        lead(23),
        steel(24),
        tin(25),
        osmium(26),
        tantalum(27),
        cadmium(28),
        arsenic(29),
        barium(30),
        bismuth(31),
        gadolinium(32),
        gallium(33),
        hafnium(34),
        yttrium(35),
        molybdenum(36),
        neodymium(37),
        niobium(38),
        palladium(39),
        polonium(40),
        strontium(41),
        thallium(42),
        zirconium(43),
        adamantite(44),
        bloodstone(45),
        draconid(46),
        meteoric_iron(47),
        mythril(48),
        orichalcum(49),
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
        public String getName() {
            return name;
        }

        @Override
        public String getMainPath() {
            return "casing";
        }

        public int getId() {
            return this.ID;
        }
    }

}
