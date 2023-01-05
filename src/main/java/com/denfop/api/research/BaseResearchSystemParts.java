package com.denfop.api.research;

import com.denfop.api.research.main.BaseLevelSystem;
import com.denfop.api.research.main.EnumLeveling;
import com.denfop.api.research.main.IResearch;
import com.denfop.api.research.main.IResearchPart;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class BaseResearchSystemParts implements IResearchSystemParts {

    @Override
    public boolean checkPressing(
            final int x, final int y, final ItemStack research, final EntityPlayer player,
            final IResearch original, List<ItemStack> stack_list
    ) {
        final List<IResearchPart> part = ResearchSystem.instance.getCopy(ResearchSystem.instance.getResearchFromItem(research));
        final List<IResearchPart> part_copy = new ArrayList<>(part);
        List<IResearchPart> copy = new ArrayList<>(original.getPartsResearch());
        copy.removeAll(part);
        for (IResearchPart parts : copy) {
            int temp_x = parts.getX();
            int temp_y = parts.getY();
            ItemStack stack = parts.getItemStack();
            ItemStack stack_copy = null;
            int count = 0;
            for (final ItemStack research_stack : stack_list) {
                if (research_stack.isItemEqual(stack) && research_stack.getCount() > stack.getCount()) {
                    stack_copy = research_stack;
                    count = stack.getCount();
                    break;
                }
            }
            if (stack_copy == null) {
                continue;
            }

            if (x > temp_x - 5 && x < temp_x + 5) {
                if (y > temp_y - 5 && y < temp_y + 5) {
                    stack_copy.setCount(stack_copy.getCount() - count);
                    part.add(parts);
                    ResearchSystem.instance.getResearchFromItem(research).setPartsResearch(part);
                    final NBTTagCompound nbt = ModUtils.nbt(research);
                    final NBTTagCompound tag = nbt.getCompoundTag("research_parts");
                    int[] list_id = tag.getIntArray("list_parts");
                    int[] new_list_id = new int[list_id.length + 1];
                    System.arraycopy(list_id, 0, new_list_id, 0, list_id.length);
                    new_list_id[list_id.length] = part_copy.indexOf(parts);
                    tag.setIntArray("list_parts", new_list_id);
                    final NBTTagCompound nbt_player = player.getEntityData();
                    if (part.size() == original.getPartsResearch().size()) {
                        nbt.setBoolean("iu.research.full", true);
                        if (!nbt_player.getBoolean("iu.research." + original.getName())) {
                            final BaseLevelSystem level = ResearchSystem.instance.getLevel(player);
                            level.addLevel(EnumLeveling.PRACTICE, original.pointsPractise());
                            nbt_player.setBoolean("iu.research." + original.getName(), true);
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }


}
