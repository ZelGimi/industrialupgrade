package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityEnergyBarrier extends TileEntityInventory {

    private final Energy energy;

    public TileEntityEnergyBarrier() {
        this.energy = this.addComponent(Energy.asBasicSink(this, 1000, 14));
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();


        if (this.energy.getEnergy() > 10) {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(
                    EntityLivingBase.class,
                    new AxisAlignedBB(this.pos.getX(), this.pos.getY(), this.pos.getZ(), (this.pos.getX() + 1),
                            (this.pos.getY() + 1),
                            (this.pos.getZ() + 1)
                    ).expand(0.1D, 0.1D, 0.1D)
            );
            if (!targets.isEmpty()) {
                for (EntityLivingBase e : targets) {
                    e.setPositionAndUpdate(
                            e.posX - Math.sin((e.rotationYaw + 180.0F) * 3.1415927F / 180.0F) * 0.2F,
                            this.pos.getY() + 1,
                            e.posZ + (Math.cos((e.rotationYaw + 180.0F) * 3.1415927F / 180.0F) * 0.2F)
                    );
                }
                this.energy.useEnergy(10);
            }
        }


    }


    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.energy_barrier;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
