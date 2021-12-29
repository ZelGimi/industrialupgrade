package com.denfop.tiles.solidmatter;

import com.denfop.IUItem;
import net.minecraft.item.ItemStack;

public enum EnumSolidMatter {
    AER(new ItemStack(IUItem.matter, 1, 7)),
    AQUA(new ItemStack(IUItem.matter, 1, 2)),
    EARTH(new ItemStack(IUItem.matter, 1, 5)),
    END(new ItemStack(IUItem.matter, 1, 6)),
    NETHER(new ItemStack(IUItem.matter, 1, 3)),
    NIGHT(new ItemStack(IUItem.matter, 1, 4)),
    MATTER(new ItemStack(IUItem.matter, 1, 0)),
    SUN(new ItemStack(IUItem.matter, 1, 1)),
    ;


    public final ItemStack stack;

    EnumSolidMatter(ItemStack stack) {
        this.stack = stack;
    }
}
