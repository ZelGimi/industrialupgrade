package com.denfop.tiles.mechanism;

import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerOilRefiner;
import com.denfop.gui.GUIOilRefiner;
import com.denfop.tiles.base.TileEntityBaseLiquedMachine;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityOilRefiner extends TileEntityBaseLiquedMachine {

    public TileEntityOilRefiner() {
        super("", 24000, 14, 2, 3, new boolean[]{false, true, true}, new boolean[]{true, false, false},
                new Fluid[]{FluidName.fluidneft.getInstance(), FluidName.fluidbenz.getInstance(),
                        FluidName.fluiddizel.getInstance()}
        );

    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @Override
    public void onNetworkUpdate(String field) {

    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public ContainerBase<TileEntityOilRefiner> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerOilRefiner(entityPlayer, this);

    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank(0).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(0).getFluidAmount() * i / this.getFluidTank(0).getCapacity();
    }

    public double gaugeLiquidScaled(double i) {
        return this.getFluidTank(0).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(0).getFluidAmount() * i / this.getFluidTank(0).getCapacity();
    }

    public double gaugeLiquidScaled1(double i) {
        return this.getFluidTank(1).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(1).getFluidAmount() * i / this.getFluidTank(1).getCapacity();
    }

    public double gaugeLiquidScaled2(double i) {
        return this.getFluidTank(2).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(2).getFluidAmount() * i / this.getFluidTank(2).getCapacity();
    }

    public int gaugeLiquidScaled1(int i) {
        return this.getFluidTank(1).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(1).getFluidAmount() * i / this.getFluidTank(1).getCapacity();
    }

    public int gaugeLiquidScaled2(int i) {
        return this.getFluidTank(2).getFluidAmount() <= 0 ? 0 :
                this.getFluidTank(2).getFluidAmount() * i / this.getFluidTank(2).getCapacity();
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIOilRefiner(new ContainerOilRefiner(entityPlayer, this));
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        IBlockState state = this.getBlockState();
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 2);


        boolean drain = false;
        boolean drain1 = false;
        if (getWorld().provider.getWorldTime() % 200 == 0) {
            initiate(2);
        }
        if (this.getFluidTank(0).getFluidAmount() >= 5 && this.energy.getEnergy() >= 25) {

            if (this.fluidTank[1].getFluidAmount() + 3 <= this.fluidTank[1].getCapacity()) {
                fill(new FluidStack(FluidName.fluidbenz.getInstance(), 3), true);
                drain = true;

            }
            if (this.fluidTank[2].getFluidAmount() + 2 <= this.fluidTank[2].getCapacity()) {
                fill(new FluidStack(FluidName.fluiddizel.getInstance(), 2), true);
                drain1 = true;
            }
            if (drain ||drain1 ) {
                int drains = 0;
                drains = drain ? drains+3 : drains;
                drains = drain1 ? drains+2 : drains;

                this.getFluidTank(0).drain(drains, true);
                initiate(0);
                this.useEnergy(25);

                IC2.network.get(true).updateTileEntityField(this, "fluidTank");

                setActive(true);
            } else {
                initiate(2);
                setActive(false);
            }
        }


    }

    public String getStartSoundFile() {
        return "Machines/oilrefiner.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

}
