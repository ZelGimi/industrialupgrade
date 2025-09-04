package com.denfop.blockentity.reactors.water.inputfluid;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.water.IInput;
import com.denfop.componets.Fluids;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityInputFluid extends BlockEntityMultiBlockElement implements IInput {

    public List<Fluids> internalFluidTankList = new ArrayList<>();

    public BlockEntityInputFluid(MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null) {
            for (Fluids fluids : internalFluidTankList) {
                if (ModUtils.interactWithFluidHandler(player, hand,
                        fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(() -> (T) new FluidHandlerReactor(this.internalFluidTankList)).cast();
        return super.getCapability(cap, facing);
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
