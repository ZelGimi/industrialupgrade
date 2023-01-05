package com.denfop.api.research;

import com.denfop.api.research.main.BaseLevelSystem;
import com.denfop.api.research.main.IResearch;
import com.denfop.api.research.main.IResearchPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public interface IResearchSystem {

    void uploadData(EntityPlayer player);

    void downloadData(EntityPlayer player);

    boolean checkData(EntityPlayer player);

    List<String> getUUIDs();

    IResearch getResearchFromItem(ItemStack stack);

    IResearch getResearchFromID(int id);

    List<IResearchPart> getCopy(IResearch research);

    BaseLevelSystem getLevel(EntityPlayer player);

    Map<String, BaseLevelSystem> getMap_level();

    Map<String, List<IResearch>> getMap_players();

}
