package com.denfop.api.windsystem;

import com.denfop.IUItem;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.api.windsystem.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.windsystem.upgrade.RotorUpgradeSystem;
import com.denfop.inventory.Inventory;
import com.denfop.items.modules.ItemRotorsUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class InventoryUpgrade extends Inventory implements ITypeSlot {

    private final IWindUpgradeBlock tile;

    public InventoryUpgrade(IWindUpgradeBlock base1) {
        super((CustomWorldContainer) base1, TypeItemSlot.INPUT, 4);
        setStackSizeLimit(1);
        this.tile = base1;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.ROTOR_UPGRADE;
    }

    public boolean canPlaceItem(final int index, ItemStack stack) {
        if (this.tile.getRotor() == null) {
            return false;
        }
        if (!(stack.getItem() instanceof ItemRotorsUpgrade<?>)) {
            return false;
        }

        EnumInfoRotorUpgradeModules enumInfoRotorUpgradeModules = EnumInfoRotorUpgradeModules.getFromID(IUItem.rotors_upgrade.getMeta((ItemRotorsUpgrade) stack.getItem()));
        int col = 0;
        for (ItemStack stack1 : this.contents) {
            if (stack1.isEmpty()) {
                continue;
            }
            if (stack1.getItem() == stack.getItem()) {
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
