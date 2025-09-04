package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.SubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemSmallDust<T extends Enum<T> & SubEnum> extends ItemMain<T> implements IItemTag {
    public ItemSmallDust(T element) {
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

        return new String[]{"forge:smalldust/" + name, "forge:smalldust"};
    }


    public enum Types implements SubEnum {
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
        bronze(19),
        copper(20),
        gold(21),
        iron(22),
        lapis(23),
        lead(24),
        obsidian(25),
        sulfur(26),
        tin(27),
        diamond(28),
        osmium(29),
        tantalum(30),
        cadmium(31),
        arsenic(32),
        barium(33),
        bismuth(34),
        gadolinium(35),
        gallium(36),
        hafnium(37),
        yttrium(38),
        molybdenum(39),
        neodymium(40),
        niobium(41),
        palladium(42),
        polonium(43),
        strontium(44),
        thallium(45),
        zirconium(46),
        emerald(47),
        quartz(48),
        ender_pearl(49),
        ghast(50),
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
            return "smalldust";
        }

        public int getId() {
            return this.ID;
        }
    }

}
