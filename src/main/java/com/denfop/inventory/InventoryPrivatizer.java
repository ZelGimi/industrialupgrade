package com.denfop.inventory;


import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.mechanism.BlockEntityPrivatizer;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemEntityModule;
import net.minecraft.world.item.ItemStack;

public class InventoryPrivatizer extends Inventory implements ITypeSlot {

    private final int type;
    private final BlockEntityPrivatizer tile;
    private int stackSizeLimit;

    public InventoryPrivatizer(BlockEntityInventory base1, int type, int count) {
        super(base1, TypeItemSlot.INPUT_OUTPUT, count);
        this.stackSizeLimit = 1;
        this.type = type;
        this.tile = (BlockEntityPrivatizer) base1;
    }

    @Override
    public EnumTypeSlot getTypeSlot(final int slotid) {
        if (type == 1) {
            return EnumTypeSlot.PRIVATE;
        }
        return EnumTypeSlot.QUARRY1;
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        if (type == 0) {
            this.update();
        }
        return content;
    }

    public void update() {
        if (type == 0) {
            this.tile.listItems.clear();
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).isEmpty()) {
                    String name = this.get(i).getOrDefault(DataComponentsInit.NAME, "");
                    if (!name.isEmpty() && !this.tile.listItems.contains(name)) {
                        this.tile.listItems.add(name);
                    }
                }

            }
        }
    }

    public boolean canPlaceItem(final int index, ItemStack itemStack) {
        if (type == 0) {
            return itemStack.getItem() instanceof ItemEntityModule && ((ItemEntityModule<?>) itemStack.getItem()).getElement().getId() == 0;
        } else {
            return itemStack.getItem() instanceof ItemAdditionModule && ((ItemAdditionModule<?>) itemStack.getItem()).getElement().getId() == 0;
        }
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
