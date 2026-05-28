package com.denfop.blockentity.collision;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.denfop.IUItem.COLLISION_PROXY_STATE;

public class BlockEntityCollisionProxy extends BlockEntity {

    private BlockPos masterPos;

    public BlockEntityCollisionProxy(BlockPos pos, BlockState state) {
        super(COLLISION_PROXY_STATE.get(), pos, state);
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        try {
            return level.getBlockEntity(masterPos).getCapability(cap);
        } catch (Exception e) {
            return LazyOptional.empty();
        }

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        try {
            return level.getBlockEntity(masterPos).getCapability(cap, side);
        } catch (Exception e) {
            return LazyOptional.empty();
        }
    }

    @Nullable
    public BlockPos getMasterPos() {
        return masterPos;
    }

    public void setMasterPos(@Nullable BlockPos masterPos) {
        this.masterPos = masterPos;
        this.setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("masterX") && tag.contains("masterY") && tag.contains("masterZ")) {
            this.masterPos = new BlockPos(
                    tag.getInt("masterX"),
                    tag.getInt("masterY"),
                    tag.getInt("masterZ")
            );
        } else {
            this.masterPos = null;
        }
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.masterPos != null) {
            tag.putInt("masterX", this.masterPos.getX());
            tag.putInt("masterY", this.masterPos.getY());
            tag.putInt("masterZ", this.masterPos.getZ());
        }
    }
}