package com.denfop.invslot;


import com.denfop.items.ItemSolidMatter;
import com.denfop.tiles.base.TileEntityBaseWorldCollector;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotWorldCollector extends InvSlot {

    private final TileEntityBaseWorldCollector tile;

    public InvSlotWorldCollector(TileEntityBaseWorldCollector base1, String string) {
        super(base1, string, InvSlot.Access.I, 1, InvSide.ANY);
        this.tile = base1;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.getmatter();
        TileEntityBaseWorldCollector tile = (TileEntityBaseWorldCollector) base;
        if (tile.getRecipeOutput() != null) {
            tile.getrequiredmatter(tile.getRecipeOutput().getRecipe().getOutput());
        }
    }

    public void getmatter() {

        for (int i = 0; i < this.size(); i++) {
            if (!get(i).isEmpty()) {
                while (!this.get(i).isEmpty() && this.tile.matter_energy + 200 <= this.tile.max_matter_energy) {
                    this.tile.matter_energy += 200;
                    this.consume(i, 1);
                }

            }
        }


    }

    public boolean accepts(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemSolidMatter && itemStack.getItemDamage() == this.tile.enumTypeCollector.getMeta();
    }

    public void consume(int content, int amount) {
        consume(content, amount, false);
    }

    public void consume(int content, int amount, boolean simulate) {

        ItemStack stack = get(content);
        if (!stack.isEmpty() && amount > 0) {
            int currentAmount = Math.min(amount, stack.getCount());
            if (!simulate) {
                stack.shrink(currentAmount);
            }
        }

    }

}
