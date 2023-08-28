package com.denfop.api.research.main;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IResearch {

    String getName();

    int getMinLevel();

    int pointsPractise();

    boolean IsUnique();

    boolean dependsOnOther();

    IResearch getDependencies();

    IResearchPages getResearchPage();

    ItemStack getItemStack();

    Icon getIcon();

    List<IResearchPart> getPartsResearch();

    void setPartsResearch(List<IResearchPart> list);

    int getID();


}
