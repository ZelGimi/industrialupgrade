package com.denfop.invslot;

import com.denfop.tiles.mechanism.energy.TileEntityEnergySubstitute;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.wiring.CableType;
import ic2.core.item.block.ItemCable;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class InvSlotSubstitute extends InvSlot {

    private final TileEntityEnergySubstitute tile;

    public InvSlotSubstitute(TileEntityEnergySubstitute tileEntityEnergySubstitute) {
        super(tileEntityEnergySubstitute, "slot", Access.I, 16);
        this.tile = tileEntityEnergySubstitute;
    }

    private static CableType getCableType(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        int type = nbt.getByte("type") & 255;
        return type < CableType.values.length ? CableType.values[type] : CableType.copper;
    }

    @Override
    public boolean accepts(final ItemStack stack) {
        return stack.getItem() instanceof ItemCable || stack.getItem() instanceof com.denfop.items.transport.ItemCable;
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
                    if (stack.getItem() instanceof ItemCable) {
                        boolean need = false;
                        for (CableItem cableItem : cableItemList) {
                            if (cableItem.equals(new CableItem(getCableType(stack).capacity, stack.getCount(), stack))) {
                                cableItem.addCount(stack.getCount());
                                need = true;
                                break;
                            }
                        }
                        if (!need) {
                            cableItemList.add(new CableItem(getCableType(stack).capacity, stack.getCount(), stack));
                        }

                    } else if (stack.getItem() instanceof com.denfop.items.transport.ItemCable) {
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
