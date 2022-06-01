package com.denfop.api.research.main;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BaseResearchPage implements IResearchPages {

    public final String name;
    public final ItemStack stack;
    public final int minLevel;
    public final ResourceLocation background;
    List<IResearch> list = new ArrayList<>();

    public BaseResearchPage(String name, ItemStack stack, int minLevel, ResourceLocation background) {
        this.name = name;
        this.stack = stack;
        this.minLevel = minLevel;
        this.background = background;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<IResearch> getResearch() {
        return this.list;
    }

    @Override
    public ItemStack getIcon() {
        return this.stack;
    }

    @Override
    public int getMinLevel() {
        return this.minLevel;
    }

    @Override
    public boolean hasMinLevel() {
        return false;
    }

    @Override
    public ResourceLocation getBackground() {
        return this.background;
    }

}
