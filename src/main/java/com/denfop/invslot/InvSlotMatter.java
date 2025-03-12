package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.tiles.base.TileCombinerMatter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotMatter extends InvSlot implements ITypeSlot {

    private final TileCombinerMatter tile;
    private int stackSizeLimit;

    public InvSlotMatter(TileCombinerMatter base1) {
        super(base1, TypeItemSlot.INPUT, 9);
        this.stackSizeLimit = 4;
        this.tile = base1;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.BLOCKS;
    }

    public void update() {
        this.tile.energy.setCapacity(this.getMaxEnergy(this));
        this.tile.fluidTank.setCapacity(this.getFluidTank(this));
        this.tile.energycost = this.getcostEnergy(this);
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.tile.energy.setCapacity(this.getMaxEnergy(this));
        this.tile.fluidTank.setCapacity(this.getFluidTank(this));
        this.tile.energycost = this.getcostEnergy(this);
    }

    public boolean accepts(ItemStack itemStack, final int index) {
        return (itemStack
                .getItem()
                .equals(Item.getItemFromBlock(IUItem.machines)) && itemStack.getItemDamage() <= 3) || (itemStack.isItemEqual(
                new ItemStack(IUItem.simplemachine, 1, 6)));
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public double getMattercostenergy(ItemStack stack) {
        int count = stack.getItemDamage();
        if (stack.getItem().equals(Item.getItemFromBlock(IUItem.machines))) {
            switch (count) {
                case 1:
                    return 900000;
                case 2:
                    return 800000;
                case 3:
                    return 700000;
            }
        }
        return 1000000;
    }

    public double getMatterenergy(ItemStack stack) {
        int count = stack.getItemDamage();
        if (stack.getItem().equals(Item.getItemFromBlock(IUItem.machines))) {
            switch (count) {
                case 1:
                    return 8000000;
                case 2:
                    return 64000000;
                case 3:
                    return 256000000;
            }
        }
        return 5000000;
    }

    public double getMaxEnergy(InvSlotMatter inputSlot) {
        double maxEnergy = 0;
        for (int i = 0; i < 9; i++) {
            if (!inputSlot.get(i).isEmpty()) {
                maxEnergy += (getMatterenergy(inputSlot.get(i)) * inputSlot.get(i).getCount());
            }

        }
        return maxEnergy;
    }

    public double getcostEnergy(InvSlotMatter inputSlot) {
        double cost = 0;
        int k = 0;
        for (int i = 0; i < 9; i++) {
            if (!inputSlot.get(i).isEmpty()) {
                cost += (getMattercostenergy(inputSlot.get(i)) * inputSlot.get(i).getCount());
                k += (inputSlot.get(i).getCount());

            }

        }
        return cost / k;
    }

    public int getFluidTank(InvSlotMatter inputSlot) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (!inputSlot.get(i).isEmpty()) {
                count += (getMattertank(inputSlot.get(i)) * inputSlot.get(i).getCount());
            }
        }
        return 1000 * count;
    }

    private int getMattertank(ItemStack itemStack) {
        int count = itemStack.getItemDamage();
        if (itemStack.getItem().equals(Item.getItemFromBlock(IUItem.machines))) {
            switch (count) {
                case 1:
                    return 12;
                case 2:
                    return 14;
                case 3:
                    return 16;
            }
        }
        return 10;
    }

}
