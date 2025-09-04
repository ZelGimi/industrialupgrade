package com.denfop.inventory;

import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.energy.BlockEntityEnergySubstitute;
import com.denfop.blockentity.transport.tiles.BlockEntityCable;
import com.denfop.blockentity.transport.types.CableType;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.blocks.mechanism.BlockCableEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventorySubstitute extends Inventory {

    private final BlockEntityEnergySubstitute tile;

    public InventorySubstitute(BlockEntityEnergySubstitute tileEntityEnergySubstitute) {
        super(tileEntityEnergySubstitute, TypeItemSlot.INPUT, 16);
        this.tile = tileEntityEnergySubstitute;
    }


    public boolean add(List<ItemStack> stacks) {
        return this.add(stacks, false);
    }

    public boolean add(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), false);
        }
    }

    public boolean canAdd(List<ItemStack> stacks) {
        boolean can = true;
        for (ItemStack stack : stacks) {
            can = can && this.canAdd(stack);
        }
        return can;
    }

    public boolean canAdd(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), true);
        }
    }

    public boolean add(List<ItemStack> stacks, boolean simulate) {
        return super.add(stacks, simulate);
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBlockTileEntity<?>))
            return false;
        return IUItem.cable.contains(stack);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        try {
            if (!this.tile.getWorld().isClientSide) {
                this.tile.getCableItemList().clear();
                this.tile.max_value = 0;
                List<CableItem> cableItemList = new ArrayList<>();
                for (ItemStack stack : this) {
                    if (stack.isEmpty()) {
                        continue;
                    }
                    ItemBlockTileEntity<BlockCableEntity> cable1 = IUItem.cable.getItem(stack);
                    boolean need = false;
                    for (CableItem cableItem : cableItemList) {
                        if (cableItem.equals(new CableItem(((CableType) ((BlockEntityCable) cable1.getElement().getDummyTe()).cableItem).capacity,
                                stack.getCount(), stack
                        ))) {
                            cableItem.addCount(stack.getCount());
                            need = true;
                            break;
                        }
                    }
                    if (!need) {
                        cableItemList.add(new CableItem(((CableType) ((BlockEntityCable) cable1.getElement().getDummyTe()).cableItem).capacity,
                                stack.getCount(), stack
                        ));
                    }


                }

                this.tile.setCableItemList(cableItemList);
                cableItemList.forEach(cableItem -> this.tile.setMaxValue(
                        this.tile.max_value,
                        cableItem.getProvider(),
                        cableItem
                ));

            }
        } catch (Exception ignored) {
        }

    }

}
