package com.denfop.tiles.gaswell;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerGasWellTank;
import com.denfop.container.ContainerGeothermalExchanger;
import com.denfop.gui.GuiGasWellTank;
import com.denfop.gui.GuiGeothermalExchanger;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGasWellTank extends TileEntityMultiBlockElement implements ITank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntityGasWellTank() {
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTankExtract("fluids", 10000);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerGasWellTank getGuiContainer(final EntityPlayer var1) {
        return new ContainerGasWellTank(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGasWellTank(this.getGuiContainer(var1));
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
        return IUItem.gas_well;
    }

}
