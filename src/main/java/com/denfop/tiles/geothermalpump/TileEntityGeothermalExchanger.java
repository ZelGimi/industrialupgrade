package com.denfop.tiles.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerGeothermalExchanger;
import com.denfop.gui.GuiGeothermalExchanger;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGeothermalExchanger extends TileEntityMultiBlockElement implements IExchanger {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntityGeothermalExchanger() {
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidhot_coolant.getInstance()));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerGeothermalExchanger getGuiContainer(final EntityPlayer var1) {
        return new ContainerGeothermalExchanger(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGeothermalExchanger(this.getGuiContainer(var1));
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
        return IUItem.geothermalpump;
    }

}
