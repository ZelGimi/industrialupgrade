package com.denfop.invslot;


import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntityCombinerSEGenerators;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotCombinerSEG extends InvSlot {

    private final TileEntityCombinerSEGenerators tile;
    private int stackSizeLimit;

    public InvSlotCombinerSEG(TileEntityCombinerSEGenerators base1) {
        super(base1, TypeItemSlot.INPUT, 9);
        this.tile = base1;
        this.stackSizeLimit = 4;
    }

    public void update() {
        this.tile.sunenergy.setCapacity(this.getMaxEnergy());
        this.tile.count = 0;
        this.tile.coef = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                this.tile.count += Math.min(this.get(i).getCount(), 4);
                Item item = this.get(i).getItem();
                if (Item.getItemFromBlock(IUItem.adv_se_generator).equals(item)) {
                    this.tile.coef += 2 * Math.min(this.get(i).getCount(), 4);
                } else if (Item.getItemFromBlock(IUItem.blockSE).equals(item)) {
                    this.tile.coef += Math.min(this.get(i).getCount(), 4);
                } else if (Item.getItemFromBlock(IUItem.imp_se_generator).equals(item)) {
                    this.tile.coef += 4 * Math.min(this.get(i).getCount(), 4);
                }
            }
        }

    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.tile.sunenergy.setCapacity(this.getMaxEnergy());
        this.tile.count = 0;
        this.tile.coef = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                this.tile.count += Math.min(this.get(i).getCount(), 4);
                Item item = this.get(i).getItem();
                if (Item.getItemFromBlock(IUItem.adv_se_generator).equals(item)) {
                    this.tile.coef += 2 * Math.min(this.get(i).getCount(), 4);
                } else if (Item.getItemFromBlock(IUItem.blockSE).equals(item)) {
                    this.tile.coef += Math.min(this.get(i).getCount(), 4);
                } else if (Item.getItemFromBlock(IUItem.imp_se_generator).equals(item)) {
                    this.tile.coef += 4 * Math.min(this.get(i).getCount(), 4);
                }
            }
        }
    }


    public boolean accepts(ItemStack itemStack, final int index) {
        return itemStack.getItem().equals(Item.getItemFromBlock(IUItem.adv_se_generator))
                || itemStack.getItem().equals(Item.getItemFromBlock(IUItem.blockSE))
                || itemStack.getItem().equals(Item.getItemFromBlock(IUItem.imp_se_generator))
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
