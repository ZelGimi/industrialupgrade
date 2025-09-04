package com.denfop.items.energy;

import com.denfop.IUCore;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.datagen.itemtag.ItemTagProvider;
import com.denfop.tabs.IItemTab;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;

import static net.minecraft.tags.ItemTags.AXES;

public class ItemAxe extends AxeItem implements IItemTab, IItemTag {
    private final String name;
    private String nameItem;

    public ItemAxe(String name) {
        super(IUTiers.RUBY, new Properties().stacksTo(1).attributes(DiggerItem.createAttributes(IUTiers.RUBY, IUTiers.RUBY.getAttackDamageBonus(), IUTiers.RUBY.getSpeed())));
        this.name = name;
        ItemTagProvider.list.add(this);
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
            this.nameItem = "item." + name;
        }

        return this.nameItem;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (allowedIn(p_41391_))
            p_41392_.add(new ItemStack(this));
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public String[] getTags() {
        return new String[]{AXES.location().toString()};
    }

}
