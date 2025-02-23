package com.denfop.tiles.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerGeothermalgenerator;
import com.denfop.gui.GuiGeothermalGenerator;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGeothermalGenerator extends TileEntityMultiBlockElement implements IGenerator {

    private final ComponentBaseEnergy energy;
    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntityGeothermalGenerator() {
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidneft.getInstance()));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    public Fluids.InternalFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    public ContainerGeothermalgenerator getGuiContainer(final EntityPlayer var1) {
        return new ContainerGeothermalgenerator(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGeothermalGenerator(this.getGuiContainer(var1));
    }

    @Override
    public ComponentBaseEnergy getEnergy() {
        return energy;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGeothermalPump.geothermal_generator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump;
    }

}
