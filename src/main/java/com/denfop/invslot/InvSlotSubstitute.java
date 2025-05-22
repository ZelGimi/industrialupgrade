package com.denfop.invslot;

import com.denfop.tiles.mechanism.energy.TileEnergySubstitute;
import net.minecraft.item.ItemStack;

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
        return stack.getItem() instanceof com.denfop.items.transport.ItemCable;
    }

    @Override
    public void onChanged() {
        super.onChanged();
        try {
            if (!this.tile.getWorld().isRemote) {
                this.tile.getCableItemList().clear();
                this.tile.max_value = 0;
                List<CableItem> cableItemList = new ArrayList<>();
                for (ItemStack stack : this) {
                    if (stack.isEmpty()) {
                        continue;
                    }
                    if (stack.getItem() instanceof com.denfop.items.transport.ItemCable) {
                        boolean need = false;
                        for (CableItem cableItem : cableItemList) {
                            if (cableItem.equals(new CableItem(com.denfop.items.transport.ItemCable.getCableType(stack).capacity,
                                    stack.getCount(), stack
                            ))) {
                                cableItem.addCount(stack.getCount());
                                need = true;
                                break;
                            }
                        }
                        if (!need) {
                            cableItemList.add(new CableItem(com.denfop.items.transport.ItemCable.getCableType(stack).capacity,
                                    stack.getCount(), stack
                            ));
                        }


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
