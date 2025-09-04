package com.denfop.inventory;


import com.denfop.IUItem;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.base.BlockEntityCombinerSolidMatter;
import com.denfop.blockentity.solidmatter.EnumSolidMatter;
import com.denfop.items.ItemSolidMatter;
import net.minecraft.world.item.ItemStack;

public class InventorySolidMatter extends Inventory implements ITypeSlot {

    private final BlockEntityCombinerSolidMatter tile;
    private int stackSizeLimit;

    public InventorySolidMatter(BlockEntityCombinerSolidMatter base1) {
        super(base1, TypeItemSlot.INPUT, 9);
        this.tile = base1;
        this.stackSizeLimit = 64;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.BLOCKS;
    }

    public void update() {
        this.tile.solid = new EnumSolidMatter[9];
        this.tile.solid_col = new int[9];
        double prev = this.tile.energy.getEnergy();
        this.tile.energy.useEnergy(this.tile.energy.getEnergy());
        this.tile.energy.setCapacity(0);
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                this.tile.solid[i] = ItemSolidMatter.getsolidmatter(IUItem.solidmatter.getMetaFromItemStack(this.get(i)));
                this.tile.solid_col[i] = this.get(i).getCount();
                this.tile.energy.addCapacity(5E7D * this.get(i).getCount());
            }
        }
        this.tile.energy.addEnergy(prev);
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.tile.solid = new EnumSolidMatter[9];
        this.tile.solid_col = new int[9];
        double prev = this.tile.energy.getEnergy();
        this.tile.energy.useEnergy(this.tile.energy.getEnergy());
        this.tile.energy.buffer.capacity = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                this.tile.solid[i] = ItemSolidMatter.getsolidmatter(IUItem.solidmatter.getMetaFromItemStack(this.get(i)));
                this.tile.solid_col[i] = this.get(i).getCount();
                this.tile.energy.addCapacity(5E7D * this.get(i).getCount());
            }
        }
        this.tile.energy.addEnergy(prev);
        return content;
    }

    public boolean canPlaceItem(final int index, ItemStack itemStack) {

        return IUItem.solidmatter.contains(itemStack);

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
                maxEnergy += (5E7D * this.get(i).getCount());
            }

        }
        return maxEnergy;
    }

}
