package com.denfop.invslot;


import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.TilePrivatizer;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class InvSlotPrivatizer extends InvSlot implements ITypeSlot {

    private final int type;
    private final TilePrivatizer tile;
    private int stackSizeLimit;

    public InvSlotPrivatizer(TileEntityInventory base1, int type, int count) {
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
                    CompoundTag nbt1 = ModUtils.nbt(this.get(i));
                    String name = nbt1.getString("name");
                    if (!this.tile.listItems.contains(name)) {
                        this.tile.listItems.add(name);
                    }
                }

            }
        }
    }

    public boolean accepts(ItemStack itemStack, final int index) {
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
