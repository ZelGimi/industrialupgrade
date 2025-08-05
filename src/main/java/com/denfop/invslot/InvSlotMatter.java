package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.blocks.mechanism.BlockSimpleMachine;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.tiles.base.TileCombinerMatter;
import net.minecraft.world.item.ItemStack;

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
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        this.tile.energy.setCapacity(this.getMaxEnergy(this));
        this.tile.fluidTank.setCapacity(this.getFluidTank(this));
        this.tile.energycost = this.getcostEnergy(this);
        return content;
    }

    public boolean accepts(ItemStack itemStack, final int index) {

        return (itemStack
                .getItem()
                instanceof ItemBlockTileEntity<?> && ((ItemBlockTileEntity<?>) itemStack.getItem()).getTeBlock(itemStack).getIDBlock() == BlockBaseMachine.adv_matter.getIDBlock() && ((ItemBlockTileEntity<?>) itemStack.getItem()).getTeBlock(itemStack).getId() <= 3)
                || (itemStack
                .getItem()
                instanceof ItemBlockTileEntity<?> && ((ItemBlockTileEntity<?>) itemStack.getItem()).getTeBlock(itemStack).getIDBlock() == BlockSimpleMachine.generator_matter.getIDBlock() && ((ItemBlockTileEntity<?>) itemStack.getItem()).getTeBlock(itemStack).getId() == 6)
                || (itemStack
                .getItem()
                instanceof ItemBlockTileEntity<?> && ((ItemBlockTileEntity<?>) itemStack.getItem()).getTeBlock(itemStack).getIDBlock() == BlockBaseMachine.imp_matter.getIDBlock())
                || (itemStack
                .getItem()
                instanceof ItemBlockTileEntity<?> && ((ItemBlockTileEntity<?>) itemStack.getItem()).getTeBlock(itemStack).getIDBlock() == BlockBaseMachine.per_matter.getIDBlock())
                || (itemStack
                .getItem()
                instanceof ItemBlockTileEntity<?> && ((ItemBlockTileEntity<?>) itemStack.getItem()).getTeBlock(itemStack).getIDBlock() == BlocksPhotonicMachine.photonic_gen_matter.getIDBlock() && ((ItemBlockTileEntity<?>) itemStack.getItem()).getTeBlock(itemStack).getId() ==12)

                ;

    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public double getMattercostenergy(ItemStack stack) {
        int count = ((ItemBlockTileEntity<?>) stack.getItem()).getTeBlock(stack).getId();
        switch (count) {
            case 1:
                return 900000;
            case 2:
                return 800000;
            case 3:
                return 700000;
            case 12:
                return 600000;
        }
        return 1000000;
    }

    public double getMatterenergy(ItemStack stack) {
        int count = ((ItemBlockTileEntity<?>) stack.getItem()).getTeBlock(stack).getId();
        switch (count) {
            case 1:
                return 8000000;
            case 2:
                return 64000000;
            case 3:
                return 256000000;
            case 12:
                return 512000000;
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

    private int getMattertank(ItemStack stack) {
        int count = ((ItemBlockTileEntity<?>) stack.getItem()).getTeBlock(stack).getId();
        switch (count) {
            case 1:
                return 12;
            case 2:
                return 14;
            case 3:
                return 16;
            case 12:
                return 16;
        }
        return 10;
    }

}
