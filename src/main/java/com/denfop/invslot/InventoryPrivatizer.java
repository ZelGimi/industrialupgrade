package com.denfop.invslot;


import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.TilePrivatizer;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryPrivatizer extends Inventory implements ITypeSlot {

    private final int type;
    private final TilePrivatizer tile;
    private int stackSizeLimit;

    public InventoryPrivatizer(TileEntityInventory base1, int type, int count) {
        super(base1, TypeItemSlot.INPUT_OUTPUT, count);
        this.stackSizeLimit = 1;
        this.type = type;
        this.tile = (TilePrivatizer) base1;
    }

    @Override
    public EnumTypeSlot getTypeSlot(final int slotid) {
        if (type == 1) {
            return EnumTypeSlot.PRIVATE;
        }
        return EnumTypeSlot.QUARRY1;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (type == 0) {
            this.update();
        }
    }

    public void update() {
        if (type == 0) {
            this.tile.listItems.clear();
            for (int i = 0; i < this.size(); i++) {
                if (!this.get(i).isEmpty()) {
                    NBTTagCompound nbt1 = ModUtils.nbt(this.get(i));
                    String name = nbt1.getString("name");
                    if (!this.tile.listItems.contains(name)) {
                        this.tile.listItems.add(name);
                    }
                }

            }
        }
    }

    public boolean isItemValidForSlot(final int index, ItemStack itemStack) {
        if (type == 0) {
            return itemStack.getItem() instanceof ItemEntityModule && itemStack.getItemDamage() == 0;
        } else {
            return itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 0;
        }
    }

    public int getInventoryStackLimit() {
        return this.stackSizeLimit;
    }

    public void setInventoryStackLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
