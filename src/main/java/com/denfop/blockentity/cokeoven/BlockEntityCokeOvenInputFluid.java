package com.denfop.blockentity.cokeoven;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockCokeOvenEntity;
import com.denfop.componets.Fluids;
import com.denfop.inventory.Inventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class BlockEntityCokeOvenInputFluid extends BlockEntityMultiBlockElement implements IInputFluid {


    private final Fluids fluids;
    FluidTank tank;

    public BlockEntityCokeOvenInputFluid(BlockPos pos, BlockState state) {
        super(BlockCokeOvenEntity.coke_oven_input_fluid, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTank("tank", 10000, Inventory.TypeItemSlot.INPUT,
                Fluids.fluidPredicate(FluidName.fluidsteam.getInstance().get())
        );

    }

    @Override
    public FluidTank getFluidTank() {
        return tank;
    }


    @Override
    public Fluids getFluid() {
        return fluids;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockCokeOvenEntity.coke_oven_input_fluid;
    }

    public BlockTileEntity getBlock() {
        return IUItem.cokeoven.getBlock(getTeBlock().getId());
    }


}
