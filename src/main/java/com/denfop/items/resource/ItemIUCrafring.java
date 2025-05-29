package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemIUCrafring<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemIUCrafring(T element) {
        super(new Item.Properties(), element);
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }
    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            int index = pathBuilder.indexOf(targetString);
            while (index != -1) {
                pathBuilder.replace(index, index + targetString.length(), replacement);
                index = pathBuilder.indexOf(targetString, index + replacement.length());
            }
            this.nameItem = pathBuilder.toString();
        }
        return this.nameItem;
    }

    public enum Types implements ISubEnum {
        itemIrradiantUranium(0),
        itemIrradiantGlassPane(1),
        itemUranIngot(2),
        itemMTCore(3);

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "itemiucrafring";

        }

        public int getId() {
            return this.ID;
        }
    }
}
