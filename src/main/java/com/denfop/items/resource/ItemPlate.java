package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemPlate<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IItemTag {
    public ItemPlate(T element) {
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
        return new String[]{"forge:plates/" + name.split("_")[0], "forge:plates"};
    }

    public enum Types implements ISubEnum {
        mikhail_plate(0),
        aluminium_plate(1),
        vanadium_plate(2),
        tungsten_plate(3),
        invar_plate(4),
        caravky_plate(5),
        cobalt_plate(6),
        magnesium_plate(7),
        nickel_plate(8),
        platinum_plate(9),
        titanium_plate(10),
        chromium_plate(11),
        spinel_plate(12),
        electrum_plate(13),
        silver_plate(14),
        zinc_plate(15),
        manganese_plate(16),
        iridium_plate(17),
        germanium_plate(18),
        bronze_plate(19),
        copper_plate(20),
        gold_plate(21),
        iron_plate(22),
        lapis_plate(23),
        lead_plate(24),
        obsidian_plate(25),
        steel_plate(26),
        tin_plate(27),

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
        adamantite(46),
        bloodstone(47),
        draconid(48),
        meteoric_iron(49),
        mythril(50),
        orichalcum(51),
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
            return "itemplates";
        }

        public int getId() {
            return this.ID;
        }
    }

}
