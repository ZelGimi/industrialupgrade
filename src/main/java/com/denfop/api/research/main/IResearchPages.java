package com.denfop.api.research.main;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IResearchPages {

      String getName();

      List<IResearch> getResearch();

      ItemStack getIcon();

      int getMinLevel();

      boolean hasMinLevel();


}
