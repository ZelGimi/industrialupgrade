package com.denfop.invslot;


import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.ItemSolidMatter;
import com.denfop.tiles.base.TileEntityCombinerSolidMatter;
import com.denfop.tiles.solidmatter.EnumSolidMatter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotSolidMatter extends InvSlot implements ITypeSlot {

    private final TileEntityCombinerSolidMatter tile;
    private int stackSizeLimit;

    public InvSlotSolidMatter(TileEntityCombinerSolidMatter base1) {
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
        this.tile.energy.useEnergy(this.tile.energy.getEnergy());
        this.tile.energy.setCapacity(0);
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                this.tile.solid[i] = ItemSolidMatter.getsolidmatter(this.get(i).getItemDamage());
                this.tile.solid_col[i] = this.get(i).getCount();
                this.tile.energy.addCapacity(Config.SolidMatterStorage * this.get(i).getCount());
            }
        }
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.tile.solid = new EnumSolidMatter[9];
        this.tile.solid_col = new int[9];
        this.tile.energy.useEnergy(this.tile.energy.getEnergy());
        this.tile.energy.setCapacity(0);
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                this.tile.solid[i] = ItemSolidMatter.getsolidmatter(this.get(i).getItemDamage());
                this.tile.solid_col[i] = this.get(i).getCount();
                this.tile.energy.addCapacity(Config.SolidMatterStorage * this.get(i).getCount());
            }
        }
    }

    public boolean accepts(ItemStack itemStack, final int index) {
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
                maxEnergy += (Config.SolidMatterStorage * this.get(i).getCount());
            }

        }
        return maxEnergy;
    }

}
