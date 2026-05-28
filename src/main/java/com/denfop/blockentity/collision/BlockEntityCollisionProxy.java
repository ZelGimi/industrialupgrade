package com.denfop.blockentity.collision;

import com.denfop.blockentity.base.BlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.denfop.IUItem.COLLISION_PROXY_STATE;

public class BlockEntityCollisionProxy extends BlockEntity {

    private BlockPos masterPos;

    public BlockEntityCollisionProxy(BlockPos pos, BlockState state) {
        super(COLLISION_PROXY_STATE.get(), pos, state);
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
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider p_338445_) {
        super.loadAdditional(tag, p_338445_);
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider p_323635_) {
        super.saveAdditional(tag, p_323635_);
        if (this.masterPos != null) {
            tag.putInt("masterX", this.masterPos.getX());
            tag.putInt("masterY", this.masterPos.getY());
            tag.putInt("masterZ", this.masterPos.getZ());
        }
    }


    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        try {
            return ((BlockEntityBase) level.getBlockEntity(masterPos)).getCapability(cap, side);
        } catch (Exception e) {
            return null;
        }
    }

}