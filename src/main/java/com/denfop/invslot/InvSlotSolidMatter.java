package com.denfop.invslot;


import com.denfop.IUItem;
import com.denfop.items.ItemSolidMatter;
import com.denfop.tiles.base.TileEntityCombinerSolidMatter;
import com.denfop.tiles.solidmatter.EnumSolidMatter;
import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotSolidMatter extends InvSlot {

    private final TileEntityCombinerSolidMatter tile;
    private int stackSizeLimit;

    public InvSlotSolidMatter(TileEntityCombinerSolidMatter base1) {
        super(base1, "input5", InvSlot.Access.I, 9, InvSlot.InvSide.TOP);
        this.tile = base1;
        this.stackSizeLimit = 1;
    }

    public void update() {
        this.tile.energy.setCapacity(this.getMaxEnergy());
        for (int i = 0; i < this.size(); i++) {

            if (!StackUtil.isEmpty(this.get(i))) {
                EnumSolidMatter[] solid1 = this.tile.solid;
                this.tile.solid = new EnumSolidMatter[this.tile.solid.length + 1];
                this.tile.solid[this.tile.solid.length - 1] = ItemSolidMatter.getsolidmatter(this.get(i).getItemDamage());
                System.arraycopy(solid1, 0, this.tile.solid, 0, solid1.length);
            }
        }
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.tile.energy.setCapacity(this.getMaxEnergy());
        for (int i = 0; i < this.size(); i++) {

            if (!StackUtil.isEmpty(this.get(i))) {
                EnumSolidMatter[] solid1 = this.tile.solid;
                this.tile.solid = new EnumSolidMatter[this.tile.solid.length + 1];
                this.tile.solid[this.tile.solid.length - 1] = ItemSolidMatter.getsolidmatter(this.get(i).getItemDamage());
                System.arraycopy(solid1, 0, this.tile.solid, 0, solid1.length);
            }
        }
    }

    public boolean accepts(ItemStack itemStack) {
        return itemStack.getItem().equals(Item.getItemFromBlock(IUItem.solidmatter));
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
                maxEnergy += 1E5D;
            }

        }
        return maxEnergy;
    }

}
