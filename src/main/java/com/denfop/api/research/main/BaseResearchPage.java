package com.denfop.api.research.main;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BaseResearchPage implements IResearchPages {

    public final String name;
    public final ItemStack stack;
    List<IResearch> list = new ArrayList<>();
    public BaseResearchPage(String name,ItemStack stack){
        this.name=name;
        this.stack=stack;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<IResearch> getResearch() {
        return this.list;
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }

    @Override
    public int getMinLevel() {
        return 0;
    }

    @Override
    public boolean hasMinLevel() {
        return false;
    }

}
