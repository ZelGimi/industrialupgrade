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

public class ItemIngots<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IItemTag {
    public ItemIngots(T element) {
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
            case 43:
                name = "adamantium";
                break;
            case 46:
                name = "meteoric";
                break;
            case 47:
                name = "mithril";
                break;
        }
        return new String[]{"c:ingots/" + name.replace("_ingot", ""), "c:ingots"};
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.itemingots";
            String replacement = "ingot";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }

    public enum ItemIngotsTypes implements ISubEnum {
        mikhail_ingot(0),
        aluminium_ingot(1),
        vanadium_ingot(2),
        tungsten_ingot(3),
        invar_ingot(4),
        caravky_ingot(5),
        cobalt_ingot(6),
        magnesium_ingot(7),
        nickel_ingot(8),
        platinum_ingot(9),
        titanium_ingot(10),
        chromium_ingot(11),
        spinel_ingot(12),
        electrum_ingot(13),
        silver_ingot(14),
        zinc_ingot(15),
        manganese_ingot(16),
        iridium_ingot(17),
        germanium_ingot(18),
        alloy_ingot(19),
        bronze_ingot(20),
        copper_ingot(21),
        lead_ingot(22),
        steel_ingot(23),
        tin_ingot(24),
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
        zirconium(42),
        adamantium(43),
        bloodstone(44),
        draconid(45),
        meteoric_iron(46),
        mithril(47),
        orichalcum(48),
        ;

        private final String name;
        private final int ID;

        ItemIngotsTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static ItemIngotsTypes getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public boolean register() {
            return this != copper_ingot;
        }

        @Override
        public String getMainPath() {
            return "itemingots";
        }

        public int getId() {
            return this.ID;
        }
    }
}
