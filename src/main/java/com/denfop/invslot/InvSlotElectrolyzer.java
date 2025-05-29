package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class InvSlotElectrolyzer extends InvSlot implements ITypeSlot {

    private final int type;
    private int stackSizeLimit;

    public InvSlotElectrolyzer(TileEntityInventory base1, int type) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.type = type;
        this.stackSizeLimit = 1;
    }


    @Override
    public EnumTypeSlot getTypeSlot() {
        if (type == 1) {
            return EnumTypeSlot.CATHODE;
        } else {
            return EnumTypeSlot.ANODE;
        }
    }

    public boolean accepts(ItemStack itemStack, final int index) {
        if (type == 0) {
            Item item = itemStack.getItem();
            return item.equals(IUItem.anode.getItem()) || item.equals(IUItem.adv_anode.getItem());
        }
        if (type == 1) {
            Item item = itemStack.getItem();
            return item.equals(IUItem.cathode.getItem()) || item.equals(IUItem.adv_cathode.getItem());
        }
        return false;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public void consume(int amount) {
        this.contents.get(0).shrink(amount);
    }


}
