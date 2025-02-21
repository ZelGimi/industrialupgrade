package com.denfop.tiles.reactors.graphite.tank;

import com.denfop.componets.Fluids;
import com.denfop.container.ContainerGraphiteTank;
import com.denfop.gui.GuiGraphiteMainTank;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.graphite.ITank;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMainTank extends TileEntityMultiBlockElement implements ITank {

    public final Fluids fluids;
    public final Fluids.InternalFluidTank tank;

    public TileEntityMainTank(int col) {
        this.fluids = this.addComponent(new Fluids(this));
        tank = this.fluids.addTank("fluidTank", col);
        tank.setCanAccept(false);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        tank.setCanAccept(this.getMain() != null && this.getMain().isFull());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGraphiteMainTank(getGuiContainer(var1));
    }

    @Override
    public ContainerGraphiteTank getGuiContainer(final EntityPlayer var1) {
        return new ContainerGraphiteTank(this, var1);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) && this.getMain() != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }


    public Fluids getFluids() {
        return fluids;
    }

    public Fluids.InternalFluidTank getTank() {
        return tank;
    }

    @Override
    public void setFluid(final Fluid fluid) {
        tank.setAcceptedFluids(Fluids.fluidPredicate(fluid));
    }

}
