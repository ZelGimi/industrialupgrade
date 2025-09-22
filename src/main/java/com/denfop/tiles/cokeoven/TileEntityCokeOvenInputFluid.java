package com.denfop.tiles.cokeoven;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockCokeOven;
import com.denfop.componets.Fluids;
import com.denfop.invslot.Inventory;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityCokeOvenInputFluid extends TileEntityMultiBlockElement implements IInputFluid {


    private final Fluids fluids;
    FluidTank tank;

    public TileEntityCokeOvenInputFluid() {
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTank("tank", 10000, Inventory.TypeItemSlot.INPUT,
                Fluids.fluidPredicate(FluidName.fluidsteam.getInstance())
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

    public IMultiTileBlock getTeBlock() {
        return BlockCokeOven.coke_oven_input_fluid;
    }

    public BlockTileEntity getBlock() {
        return IUItem.cokeoven;
    }


}
