package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.tiles.mechanism.energy.TileEnergySubstitute;
import com.denfop.tiles.transport.tiles.TileEntityCable;
import com.denfop.tiles.transport.types.CableType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvSlotSubstitute extends InvSlot {

    private final TileEnergySubstitute tile;

    public InvSlotSubstitute(TileEnergySubstitute tileEntityEnergySubstitute) {
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
    public boolean accepts(final ItemStack stack, final int index) {
        if (!(stack.getItem() instanceof ItemBlockTileEntity<?>))
            return false;
        return IUItem.cable.contains(stack);
    }

    @Override
    public void onChanged() {
        super.onChanged();
        try {
            if (!this.tile.getWorld().isClientSide) {
                this.tile.getCableItemList().clear();
                this.tile.max_value = 0;
                List<CableItem> cableItemList = new ArrayList<>();
                for (ItemStack stack : this) {
                    if (stack.isEmpty()) {
                        continue;
                    }
                    ItemBlockTileEntity<BlockCable> cable1 = IUItem.cable.getItem(stack);
                    boolean need = false;
                    for (CableItem cableItem : cableItemList) {
                        if (cableItem.equals(new CableItem(((CableType) ((TileEntityCable) cable1.getElement().getDummyTe()).cableItem).capacity,
                                stack.getCount(), stack
                        ))) {
                            cableItem.addCount(stack.getCount());
                            need = true;
                            break;
                        }
                    }
                    if (!need) {
                        cableItemList.add(new CableItem(((CableType) ((TileEntityCable) cable1.getElement().getDummyTe()).cableItem).capacity,
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
