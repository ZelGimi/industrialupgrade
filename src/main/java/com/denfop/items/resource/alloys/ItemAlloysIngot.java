package com.denfop.items.resource.alloys;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemAlloysIngot<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IItemTag {
    public ItemAlloysIngot(T element) {
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
        return new String[]{"forge:ingots/" + getElement().getName().replace("_alloy", "").replace("_", ""), "forge:ingots"};
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
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

        return this.nameItem;
    }

    public enum Type implements ISubEnum {
        aluminum_bronze(0),
        alumel(1),
        red_brass(2),
        muntsa(3),
        nichrome(4),
        alcled(5),
        vanadoalumite(6),
        vitalium(7),
        duralumin(8),
        ferromanganese(9),
        aluminium_silicon(10),
        beryllium_bronze(11),
        zeliber(12),
        stainless_steel(13),
        inconel(14),
        nitenol(15),
        stellite(16),
        hafnium_boride_alloy(17),
        woods(18),
        nimonic(19),
        tantalum_tungsten_hafnium(20),
        permalloy(21),
        aluminium_lithium_alloy(22),
        cobalt_chrome(23),
        hafnium_carbide(24),
        molybdenum_steel(25),
        niobium_titanium(26),
        osmiridium(27),
        superalloy_haynes(28),
        superalloy_rene(29),
        yttrium_aluminium_garnet(30),
        gallium_arsenic(31),
        ;

        private final String name;
        private final int ID;

        Type(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Type getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }


        @Override
        public String getMainPath() {
            return "alloyingot";
        }

        public int getId() {
            return this.ID;
        }
    }
}
