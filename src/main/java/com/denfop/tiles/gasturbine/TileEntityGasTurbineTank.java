package com.denfop.tiles.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.componets.Fluids;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class TileEntityGasTurbineTank extends TileEntityMultiBlockElement implements ITank {


    private final Fluids.InternalFluidTank tank;
    private final Fluids fluids;

    public TileEntityGasTurbineTank(BlockPos pos, BlockState state) {
        super(BlockGasTurbine.gas_turbine_tank, pos, state);
        fluids = this.addComponent(new Fluids(this));
        this.tank = fluids.addTank("tank", 20000, InvSlot.TypeItemSlot.INPUT);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasTurbine.gas_turbine_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine.getBlock(getTeBlock());
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        List<Fluid> fluidList = new ArrayList<>(TileEntityGasTurbineController.gasMapValue
                .keySet());
        this.tank.setAcceptedFluids(Fluids.fluidPredicate(fluidList));
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null && this.getMain() != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(Capabilities.FluidHandler.BLOCK, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }


    @Override
    public FluidTank getTank() {
        return tank;
    }

}
