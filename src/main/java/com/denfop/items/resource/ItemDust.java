package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemDust<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IItemTag {
    public ItemDust(T element) {
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
        return new String[]{"c:dusts/" + name, "c:dusts"};
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.item";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem.replace("_dust", "");
    }

    public enum ItemDustTypes implements ISubEnum {
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
        clay(20),
        coal(21),
        copper(22),
        diamond(23),
        energium(24),
        gold(25),
        iron(26),
        lapis(27),
        lead(28),
        obsidian(29),
        stone(30),
        sulfur(31),
        tin(32),
        silicon_dioxide(33),
        osmium(34),
        tantalum(35),
        cadmium(36),
        potassium(37),
        iron_potassium(38),
        potassium_iron_syneride(39),
        nanoactivated_mixture(40),
        calcium_dust(41),
        zincbromide(42),
        slaked_lime(43),
        arsenic(44),
        barium(45),
        bismuth(46),
        gadolinium(47),
        gallium(48),
        hafnium(49),
        yttrium(50),
        molybdenum(51),
        neodymium(52),
        niobium(53),
        palladium(54),
        polonium(55),
        strontium(56),
        thallium(57),
        zirconium(58),
        aluminumsulfide(59),
        silicon(60),
        ammonium_chloride(61),
        silver_nitrate(62),
        silver_chloride(63),
        sodium(64),
        calcium_fluoride(65),
        calcium_phosphate(66),
        calcium_sulfate(67),
        alkaline_mixture(68),

        sodium_phosphate(69),

        potassium_phosphate(70),

        calcium_silicate(71),
        trinitrotoluene(72),
        lead_sodium(73),
        emerald(74),
        ender(75),
        quartz(76),
        ghast(77),
        excited_uranium(78),
        iron_chloride(79),
        ;

        private final String name;
        private final int ID;

        ItemDustTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static ItemDustTypes getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "itemdust";
        }

        @Override
        public String getOtherPart() {
            return "_dust";
        }

        public int getId() {
            return this.ID;
        }
    }
}
