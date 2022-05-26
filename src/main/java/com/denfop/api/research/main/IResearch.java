package com.denfop.api.research.main;

import net.minecraft.item.ItemStack;

public interface IResearch {

    String getName();

    int getMinLevel();

    int pointsPractise();

    boolean IsUnique();

    boolean dependsOnOther();

    IResearch getDependencies();

    IResearchPages getResearchPage();

    ItemStack getIcon();

}
