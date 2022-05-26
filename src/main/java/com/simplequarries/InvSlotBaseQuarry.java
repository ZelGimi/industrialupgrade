package com.simplequarries;

import com.denfop.items.modules.EnumQuarryModules;
import com.denfop.items.modules.EnumQuarryType;
import com.denfop.items.modules.QuarryModule;
import com.denfop.utils.ModUtils;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotBaseQuarry extends InvSlot {


    public final TileEntityBaseQuarry tile;
    public int stackSizeLimit;

    public InvSlotBaseQuarry(TileEntityBaseQuarry base1, int oldStartIndex1) {
        super(base1,  "input", Access.I, oldStartIndex1, InvSlot.InvSide.TOP);
        this.tile= base1;
        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack) {

            if(itemStack.getItem() instanceof QuarryModule && itemStack.getItemDamage() > 11) {
                for(int i = 0; i < this.size();i++){
                    if(!(this.get(i).isEmpty() || (this.get(i).getItem()instanceof QuarryModule && !(itemStack.getItemDamage() > 11) ))) {
                       return false;
                    }
                }
                (( TileEntityBaseQuarry) this.base).list= ModUtils.getListFromModule(itemStack);
                return true;
            }
            if(itemStack.getItem() instanceof QuarryModule && (EnumQuarryModules.getFromID(itemStack.getItemDamage()).type != EnumQuarryType.WHITELIST && EnumQuarryModules.getFromID(
                    itemStack.getItemDamage()).type != EnumQuarryType.BLACKLIST)) {
                if(EnumQuarryModules.getFromID(
                        itemStack.getItemDamage()).type == EnumQuarryType.DEPTH)
                    tile.blockpos = null;
                return itemStack.getItem() instanceof QuarryModule && (EnumQuarryModules.getFromID(itemStack.getItemDamage()).type != EnumQuarryType.WHITELIST && EnumQuarryModules.getFromID(
                        itemStack.getItemDamage()).type != EnumQuarryType.BLACKLIST);
            }
        return false;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
