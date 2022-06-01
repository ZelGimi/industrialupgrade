package com.denfop.invslot;


import com.denfop.items.ItemSolidMatter;
import com.denfop.tiles.base.TileEntityConverterSolidMatter;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

public class InvSlotConverterSolidMatter extends InvSlot {

    public InvSlotConverterSolidMatter(TileEntityInventory base1, String string) {
        super(base1, string, InvSlot.Access.I, 7, InvSlot.InvSide.TOP);

    }

    public static boolean isStackEqual(ItemStack stack1, ItemStack stack2) {
        return stack1 == null && stack2 == null || stack1 != null && stack2 != null && stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() && !stack1.isItemStackDamageable() || stack1.getItemDamage() == stack2.getItemDamage());
    }

    public static boolean isStackEqualStrict(ItemStack stack1, ItemStack stack2) {
        return isStackEqual(stack1, stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.getmatter();
    }

    public void getmatter() {

        for (int i = 0; i < this.size(); i++) {
            if (!get(i).isEmpty()) {
                TileEntityConverterSolidMatter tile = (TileEntityConverterSolidMatter) base;
                int meta = get(i).getItemDamage();
                while (!this.get(i).isEmpty() && tile.quantitysolid[meta % tile.quantitysolid.length] <= 4800) {
                    tile.quantitysolid[meta % tile.quantitysolid.length] += 200;
                    this.consume(i, 1);

                }

            }
        }


    }

    public boolean accepts(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemSolidMatter;
    }

    public void consume(int content, int amount) {
        consume(content, amount, false, false);
    }

    public void consume(int content, int amount, boolean simulate, boolean consumeContainers) {
        ItemStack ret = ItemStack.EMPTY;

        ItemStack stack = get(content);
        if (!stack.isEmpty() && stack.getCount() >= 1 &&

                accepts(stack) && (ret.isEmpty() ||
                isStackEqualStrict(stack, ret)) && (stack.getCount() == 1 || consumeContainers ||
                !stack.getItem().hasContainerItem(stack))) {
            int currentAmount = Math.min(amount, stack.getCount());
            if (!simulate) {
                if (stack.getCount() == currentAmount) {
                    if (!consumeContainers && stack.getItem().hasContainerItem(stack)) {
                        put(content, stack.getItem().getContainerItem(stack));
                    } else {
                        put(content, ItemStack.EMPTY);
                    }
                } else {
                    stack.setCount(stack.getCount() - currentAmount);
                }
            }
            if (ret.isEmpty()) {
                StackUtil.copyWithSize(stack, currentAmount);
            } else {
                ret.setCount(ret.getCount() + currentAmount);
            }

        }

    }

}
