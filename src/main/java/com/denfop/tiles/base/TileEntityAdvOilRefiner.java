package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerAdvOilRefiner;
import com.denfop.gui.GuiAdvOilRefiner;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAdvOilRefiner extends TileEntityBaseLiquedMachine {

    public TileEntityAdvOilRefiner() {
        super(
                24000, 14, 2, 3, new boolean[]{false, true, true}, new boolean[]{true, false, false},
                new Fluid[]{FluidName.fluidneft.getInstance(), FluidName.fluidpolyeth.getInstance(),
                        FluidName.fluidpolyprop.getInstance()}
        );

    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.oiladvrefiner, 1, 0);
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
    public ContainerBase<TileEntityAdvOilRefiner> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerAdvOilRefiner(entityPlayer, this);

    }

    public String getStartSoundFile() {
        return "Machines/oilrefiner.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
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


    public void updateEntityServer() {
        super.updateEntityServer();

        boolean drain = false;
        boolean drain1 = false;

        if (getWorld().provider.getWorldTime() % 200 == 0) {
            initiate(2);
        }

        if (this.getFluidTank(0).getFluidAmount() >= 10 && this.energy.getEnergy() >= 25) {
            int size = this.getFluidTank(0).getFluidAmount() / 10;
            size = Math.min(this.level + 1, size);
            int cap = this.fluidTank[1].getCapacity() - this.fluidTank[1].getFluidAmount();
            cap /= 5;
            cap = Math.min(cap, size);
            int cap1 = this.fluidTank[2].getCapacity() - this.fluidTank[2].getFluidAmount();
            cap1 /= 5;
            cap1 = Math.min(cap1, size);
            if (this.fluidTank[1].getCapacity() - this.fluidTank[1].getFluidAmount() >= 5) {
                fill(new FluidStack(FluidName.fluidpolyeth.getInstance(), cap * 5), true);
                drain = true;

            }
            if (this.fluidTank[2].getCapacity() - this.fluidTank[2].getFluidAmount() >= 5) {
                fill(new FluidStack(FluidName.fluidpolyprop.getInstance(), cap1 * 5), true);
                drain1 = true;
            }
            if (drain || drain1) {
                int drains = 0;
                drains = drain ? drains + 5 * cap : drains;
                drains = drain1 ? drains + 5 * cap1 : drains;

                this.getFluidTank(0).drain(drains, true);
                initiate(0);
                this.useEnergy(25);


                setActive(true);
            } else {
                initiate(2);
                setActive(false);
            }
            if (this.world.provider.getWorldTime() % 10 == 0) {
                IC2.network.get(true).updateTileEntityField(this, "fluidTank");
            }

        }


    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAdvOilRefiner(new ContainerAdvOilRefiner(entityPlayer, this));

    }

}
