package com.denfop.api.windsystem;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.windsystem.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.windsystem.upgrade.RotorUpgradeSystem;
import com.denfop.invslot.InvSlot;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.mechanism.TileEntityRotorModifier;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class InvSlotUpgrade extends InvSlot implements ITypeSlot {

    private final IWindUpgradeBlock tile;

    public InvSlotUpgrade(IWindUpgradeBlock base1) {
        super((IAdvInventory) base1, TypeItemSlot.INPUT, 4);
        setStackSizeLimit(1);
        this.tile = base1;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.ROTOR_UPGRADE;
    }

    public boolean accepts(ItemStack stack, final int index) {
        if (this.tile.getRotor() == null) {
            return false;
        }
        if (!(stack.getItem() instanceof com.denfop.items.ItemRotorsUpgrade)) {
            return false;
        }

        EnumInfoRotorUpgradeModules enumInfoRotorUpgradeModules = EnumInfoRotorUpgradeModules.getFromID(stack.getItemDamage());
        int col = 0;
        for (ItemStack stack1 : this.contents) {
            if (stack1.isEmpty()) {
                continue;
            }
            if (stack1.isItemEqual(stack)) {
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
            put(i, ItemStack.EMPTY);
        }
    }

    public void update(ItemStack stack) {
        Map<Integer, ItemStack> map = RotorUpgradeSystem.instance.getList(stack);
        for (Map.Entry<Integer, ItemStack> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }


    public void put(int index, ItemStack content) {
        super.put(index, content);
        ((IUpdatableTileEvent) this.base).updateTileServer(null, 0);
    }

}
