package com.denfop.items;

import com.denfop.blocks.SubEnum;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.datagen.itemtag.ItemTagProvider;
import com.denfop.tabs.IItemTab;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public abstract class ItemMain<T extends Enum<T> & SubEnum> extends Item implements IItemTab {

    private final T element;
    protected String nameItem;

    public ItemMain(Properties p_41383_, T element) {
        super(p_41383_);
        this.element = element;
        if (this instanceof IItemTag)
            ItemTagProvider.list.add((IItemTag) this);
        ;
    }

    public T getElement() {
        return element;
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


}
