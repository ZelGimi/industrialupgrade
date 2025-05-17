package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.water.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.water.upgrade.RotorUpgradeSystem;
import com.denfop.api.windsystem.IWindUpgradeBlock;
import com.denfop.items.modules.ItemWaterRotorsUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class InvSlotWaterUpgrade extends InvSlot implements ITypeSlot {

    private final IWindUpgradeBlock tile;

    public InvSlotWaterUpgrade(IWindUpgradeBlock base1) {
        super((IAdvInventory) base1, TypeItemSlot.INPUT, 4);
        setStackSizeLimit(1);
        this.tile = base1;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.QUARRY1;
    }

    public boolean accepts(ItemStack stack, final int index) {
        if (this.tile.getRotor() == null) {
            return false;
        }
        if (!(stack.getItem() instanceof ItemWaterRotorsUpgrade)) {
            return false;
        }

        EnumInfoRotorUpgradeModules enumInfoRotorUpgradeModules = EnumInfoRotorUpgradeModules.getFromID(((ItemWaterRotorsUpgrade<?>) stack.getItem()).getElement().getId());
        int col = 0;
        for (ItemStack stack1 : this.contents) {
            if (stack1.isEmpty()) {
                continue;
            }
            if (stack1.is(stack.getItem())) {
                col++;
            }
        }
        if (col == 0) {
            return true;
        }
        return (col < enumInfoRotorUpgradeModules.getMax());
    }

    public void update() {
        for (int i = 0; i < size(); i++) {
            put(i, ItemStack.EMPTY, false);
        }
    }

    public void update(ItemStack stack) {
        Map<Integer, ItemStack> map = RotorUpgradeSystem.instance.getList(stack);
        for (Map.Entry<Integer, ItemStack> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue(), false);
        }
    }

    public void put(int index, ItemStack content, boolean updates) {
        super.set(index, content);
    }
    @Override
    public ItemStack set(int i, ItemStack empty) {
        super.set(i, empty);
        ((IUpdatableTileEvent) this.base).updateTileServer(null, 0);
        return empty;

    }


}
