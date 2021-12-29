package com.denfop.invslot;


import com.denfop.items.modules.QuarryModule;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class InvSlotModule extends InvSlot {

    private final int type;
    private int stackSizeLimit;

    public InvSlotModule(TileEntityInventory base1, String name, int type, int count) {
        super(base1, name, InvSlot.Access.IO, count, InvSlot.InvSide.TOP);
        this.stackSizeLimit = 1;
        this.type = type;
    }

    public boolean accepts(ItemStack itemStack) {
        if (type == 0) {
            if (OreDictionary.getOreIDs(itemStack).length > 0) {
                int id = OreDictionary.getOreIDs(itemStack)[0];
                String ore = OreDictionary.getOreName(id);

                return ore.startsWith("ore") || ore.startsWith("gem") || ore.startsWith("ingot") || ore.startsWith("dust") || ore.startsWith(
                        "shard");
            } else {
                return false;
            }
        } else {
            return itemStack.getItem() instanceof QuarryModule && itemStack.getItemDamage() >= 12;
        }
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
