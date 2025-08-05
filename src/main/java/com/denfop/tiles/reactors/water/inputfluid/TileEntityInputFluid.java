package com.denfop.tiles.reactors.water.inputfluid;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.Fluids;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.IInput;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TileEntityInputFluid extends TileEntityMultiBlockElement implements IInput {

    public List<Fluids> internalFluidTankList = new ArrayList<>();

    public TileEntityInputFluid(IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null) {
            for (Fluids fluids : internalFluidTankList) {
                if (ModUtils.interactWithFluidHandler(player, hand,
                        fluids.getCapability(Capabilities.FluidHandler.BLOCK, side)
                )) {
                    return true;
                }
            }
            return false;
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }

    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.FluidHandler.BLOCK) {
            return (T) new FluidHandlerReactor(this.internalFluidTankList);
        }
        return super.getCapability(cap, side);
    }


    @Override
    public void addFluids(final Fluids fluids) {
        internalFluidTankList.add(fluids);
    }

    @Override
    public void clearList() {
        internalFluidTankList.clear();
    }


}
