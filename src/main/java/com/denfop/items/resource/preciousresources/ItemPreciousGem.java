package com.denfop.items.resource.preciousresources;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.items.ItemMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemPreciousGem<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IItemTag {
    public ItemPreciousGem(T element) {
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
        return new String[]{"c:gems/" + getElement().getName().substring(0, getElement().getName().indexOf("_")), "c:gems"};
    }

    public enum Types implements ISubEnum {
        ruby_gem(0),
        sapphire_gem(1),
        topaz_gem(2);


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
            return "preciousgem";
        }

        public int getId() {
            return this.ID;
        }
    }

}
