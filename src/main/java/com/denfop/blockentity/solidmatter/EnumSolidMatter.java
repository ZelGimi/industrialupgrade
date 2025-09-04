package com.denfop.blockentity.solidmatter;

import com.denfop.IUItem;
import net.minecraft.world.item.ItemStack;

public enum EnumSolidMatter {
    AER(new ItemStack(IUItem.matter.getItemFromMeta(7), 1)),
    AQUA(new ItemStack(IUItem.matter.getItemFromMeta(2), 1)),
    EARTH(new ItemStack(IUItem.matter.getItemFromMeta(5), 1)),
    END(new ItemStack(IUItem.matter.getItemFromMeta(6), 1)),
    NETHER(new ItemStack(IUItem.matter.getItemFromMeta(3), 1)),
    NIGHT(new ItemStack(IUItem.matter.getItemFromMeta(4), 1)),
    MATTER(new ItemStack(IUItem.matter.getItemFromMeta(0), 1)),
    SUN(new ItemStack(IUItem.matter.getItemFromMeta(1), 1)),
    ;


    public final ItemStack stack;

    EnumSolidMatter(ItemStack stack) {
        this.stack = stack;
    }
}
