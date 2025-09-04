package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.Energy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BlockEntityEnergyBarrier extends BlockEntityInventory {

    private final Energy energy;

    public BlockEntityEnergyBarrier(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.energy_barrier, pos, state);
        this.energy = this.addComponent(Energy.asBasicSink(this, 1000, 14));
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();


        if (this.energy.getEnergy() > 10) {
            AABB aabb = new AABB(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(),
                    this.worldPosition.getX() + 1, this.worldPosition.getY() + 1, this.worldPosition.getZ() + 1)
                    .inflate(0.1D, 0.1D, 0.1D);

            List<LivingEntity> targets = this.level.getEntitiesOfClass(LivingEntity.class, aabb);

            if (!targets.isEmpty()) {
                for (LivingEntity e : targets) {
                    double newX = e.getX() - Math.sin((e.getYRot() + 180.0F) * Math.PI / 180.0F) * 0.2F;
                    double newY = this.worldPosition.getY() + 1;
                    double newZ = e.getZ() + Math.cos((e.getYRot() + 180.0F) * Math.PI / 180.0F) * 0.2F;
                    e.teleportTo(newX, newY, newZ);
                }
                this.energy.useEnergy(10);
            }
        }


    }


    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.energy_barrier;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
