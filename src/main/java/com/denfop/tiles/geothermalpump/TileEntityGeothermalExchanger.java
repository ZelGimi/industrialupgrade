package com.denfop.tiles.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerGeothermalExchanger;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGeothermalExchanger;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityGeothermalExchanger extends TileEntityMultiBlockElement implements IExchanger {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntityGeothermalExchanger(BlockPos pos, BlockState state) {
        super(BlockGeothermalPump.geothermal_exchanger,pos,state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidhot_coolant.getInstance().get()));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerGeothermalExchanger getGuiContainer(final Player var1) {
        return new ContainerGeothermalExchanger(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiGeothermalExchanger((ContainerGeothermalExchanger) menu);
    }

    @Override
    public Fluids.InternalFluidTank getFluidTank() {
        return this.fluidTank;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGeothermalPump.geothermal_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump.getBlock(getTeBlock());
    }

}
