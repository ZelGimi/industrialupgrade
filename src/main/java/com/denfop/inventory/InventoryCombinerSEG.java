package com.denfop.inventory;


import com.denfop.IUItem;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.base.BlockEntityCombinerSEGenerators;
import net.minecraft.world.item.ItemStack;

public class InventoryCombinerSEG extends Inventory implements ITypeSlot {

    private final BlockEntityCombinerSEGenerators tile;
    private int stackSizeLimit;

    public InventoryCombinerSEG(BlockEntityCombinerSEGenerators base1) {
        super(base1, TypeItemSlot.INPUT, 9);
        this.tile = base1;
        this.stackSizeLimit = 4;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.BLOCKS;
    }

    public void update() {
        this.tile.sunenergy.setCapacity(this.getMaxEnergy());
        this.tile.count = 0;
        this.tile.coef = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                this.tile.count += Math.min(this.get(i).getCount(), 4);
                if (this.get(i).is(IUItem.adv_se_generator.getItem())) {
                    this.tile.coef += 2 * Math.min(this.get(i).getCount(), 4);
                } else if (this.get(i).is(IUItem.blockSE.getItem())) {
                    this.tile.coef += Math.min(this.get(i).getCount(), 4);
                } else if (this.get(i).is(IUItem.imp_se_generator.getItem())) {
                    this.tile.coef += 4 * Math.min(this.get(i).getCount(), 4);
                }
            }
        }

    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.tile.sunenergy.setCapacity(this.getMaxEnergy());
        this.tile.count = 0;
        this.tile.coef = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                this.tile.count += Math.min(this.get(i).getCount(), 4);
                if (this.get(i).is(IUItem.adv_se_generator.getItem())) {
                    this.tile.coef += 2 * Math.min(this.get(i).getCount(), 4);
                } else if (this.get(i).is(IUItem.blockSE.getItem())) {
                    this.tile.coef += Math.min(this.get(i).getCount(), 4);
                } else if (this.get(i).is(IUItem.imp_se_generator.getItem())) {
                    this.tile.coef += 4 * Math.min(this.get(i).getCount(), 4);
                }
            }
        }
        return content;
    }


    public boolean canPlaceItem(final int index, ItemStack itemStack) {
        return itemStack.is((IUItem.adv_se_generator.getItem()))
                || itemStack.is((IUItem.blockSE.getItem()))
                || itemStack.is((IUItem.imp_se_generator.getItem()))
                ;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public double getMaxEnergy() {
        double maxEnergy = 0;
        for (int i = 0; i < size(); i++) {
            if (!get(i).isEmpty()) {
                maxEnergy += 10000 * Math.min(get(i).getCount(), 4);
            }

        }
        return maxEnergy;
    }

}
