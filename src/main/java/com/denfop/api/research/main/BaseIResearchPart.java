package com.denfop.api.research.main;

import net.minecraft.item.ItemStack;

public class BaseIResearchPart implements IResearchPart {

    public final int x;
    public final int y;
    public final ItemStack stack;
    public final EnumPartSampler part;

    public BaseIResearchPart(int x, int y, ItemStack stack, EnumPartSampler part) {
        this.x = part.startX + x;
        this.y = part.startY + y;
        this.stack = stack;
        this.part = part;

    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public ItemStack getItemStack() {
        return this.stack;
    }

    @Override
    public EnumPartSampler getPart() {
        return this.part;
    }

}
