package com.denfop.tiles.gaswell;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerGasWellTank;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGasWellTank;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityGasWellTank extends TileEntityMultiBlockElement implements ITank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntityGasWellTank(BlockPos pos, BlockState state) {
        super( BlockGasWell.gas_well_tank, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTankExtract("fluids", 10000);
    }


    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerGasWellTank getGuiContainer(final Player var1) {
        return new ContainerGasWellTank(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiGasWellTank((ContainerGasWellTank) menu);
    }

    @Override
    public Fluids.InternalFluidTank getTank() {
        return this.fluidTank;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasWell.gas_well_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well.getBlock(getTeBlock());
    }

}
