package com.denfop.tiles.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.componets.Fluids;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class TileEntityGasTurbineTank extends TileEntityMultiBlockElement implements ITank {


    private final Fluids.InternalFluidTank tank;
    private final Fluids fluids;

    public TileEntityGasTurbineTank() {
        fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTank("tank", 20000, InvSlot.TypeItemSlot.INPUT);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasTurbine.gas_turbine_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        List<Fluid> fluidList = new ArrayList<>(TileEntityGasTurbineController.gasMapValue
                .keySet());
        this.tank.setAcceptedFluids(Fluids.fluidPredicate(fluidList));
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

    @Override
    public FluidTank getTank() {
        return tank;
    }

}
