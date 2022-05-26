package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.items.modules.QuarryModule;
import com.denfop.tiles.base.TileEntityBaseQuantumQuarry;
import com.denfop.utils.ModUtils;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotQuantumQuarry extends InvSlot {

    public final int type;
    public int stackSizeLimit;

    public InvSlotQuantumQuarry(TileEntityInventory base1, int oldStartIndex1, String name, int type) {
        super(base1, name, Access.I, oldStartIndex1, InvSlot.InvSide.TOP);

        this.stackSizeLimit = 1;
        this.type = type;
    }

    public boolean accepts(ItemStack itemStack) {
        if (type == 0) {

            return itemStack.getItem() instanceof QuarryModule && (EnumQuarryModules.getFromID(itemStack.getItemDamage()).type != EnumQuarryType.WHITELIST && EnumQuarryModules.getFromID(
                    itemStack.getItemDamage()).type != EnumQuarryType.BLACKLIST);
        } else if (type == 1) {
            if (itemStack.getItem() instanceof QuarryModule && itemStack.getItemDamage() > 11) {
                ((TileEntityBaseQuantumQuarry) this.base).list = ModUtils.getListFromModule(itemStack);
                return true;
            }

        }
        return itemStack.getItem().equals(IUItem.analyzermodule);
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
