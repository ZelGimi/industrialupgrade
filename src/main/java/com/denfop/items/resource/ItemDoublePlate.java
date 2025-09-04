package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.SubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemDoublePlate<T extends Enum<T> & SubEnum> extends ItemMain<T> implements IItemTag {
    public ItemDoublePlate(T element) {
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
            case 46:
                name = "adamantium";
                break;
            case 49:
                name = "meteoric";
                break;
            case 50:
                name = "mithril";
                break;

        }
        return new String[]{"forge:doubleplate/" + name.replace("_doubleplate", ""), "forge:doubleplate"};
    }

    public enum ItemDoublePlateTypes implements SubEnum {
        mikhail_doubleplate(0),
        aluminium_doubleplate(1),
        vanadium_doubleplate(2),
        tungsten_doubleplate(3),
        invar_doubleplate(4),
        caravky_doubleplate(5),
        cobalt_doubleplate(6),
        magnesium_doubleplate(7),
        nickel_doubleplate(8),
        platinum_doubleplate(9),
        titanium_doubleplate(10),
        chromium_doubleplate(11),
        spinel_doubleplate(12),
        electrum_doubleplate(13),
        silver_doubleplate(14),
        zinc_doubleplate(15),
        manganese_doubleplate(16),
        iridium_doubleplate(17),
        germanium_doubleplate(18),
        bronze_doubleplate(19),
        copper_doubleplate(20),
        gold_doubleplate(21),
        iron_doubleplate(22),
        lapis_doubleplate(23),
        lead_doubleplate(24),
        obsidian_doubleplate(25),
        steel_doubleplate(26),
        tin_doubleplate(27),
        osmium(28),
        tantalum(29),
        cadmium(30),
        arsenic(31),
        barium(32),
        bismuth(33),
        gadolinium(34),
        gallium(35),
        hafnium(36),
        yttrium(37),
        molybdenum(38),
        neodymium(39),
        niobium(40),
        palladium(41),
        polonium(42),
        strontium(43),
        thallium(44),
        zirconium(45),
        adamantium(46),
        bloodstone(47),
        draconid(48),
        meteoric_iron(49),
        mithril(50),
        orichalcum(51),
        ;

        private final String name;
        private final int ID;

        ItemDoublePlateTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static ItemDoublePlateTypes getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "itemdoubleplates";
        }

        public int getId() {
            return this.ID;
        }
    }
}
