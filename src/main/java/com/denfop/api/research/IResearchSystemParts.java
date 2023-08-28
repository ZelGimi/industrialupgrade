package com.denfop.api.research;

import com.denfop.api.research.main.IResearch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IResearchSystemParts {

    boolean checkPressing(int x, int y, ItemStack research, EntityPlayer player, IResearch original, List<ItemStack> stack);


}
