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

    private boolean add(List<ItemStack> stacks, boolean simulate) {
        if (stacks != null && !stacks.isEmpty()) {

            for (ItemStack stack : stacks) {
                for (int i = 0; i < this.size(); i++) {
                    if (this.get(i).isEmpty()) {
                        if (!simulate) {
                            this.put(i, stack.copy());

                        }
                        return true;
                    } else {
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    return true;
                                } else {
                                    if (stack.getTagCompound() != null &&
                                            stack.getTagCompound().equals(this.get(i).getTagCompound())) {
                                        if (!simulate) {
                                            this.get(i).grow(stack.getCount());

                                        }
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
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
                for (ItemStack stack : this.gets()) {
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
