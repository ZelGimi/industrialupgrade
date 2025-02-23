package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.Vector3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityMobMagnet extends TileEntityInventory {

    private final Energy energy;

    public TileEntityMobMagnet() {
        this.energy = this.addComponent(Energy.asBasicSink(this, 10000, 14));

        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.world.provider.getWorldTime() % 4 == 0) {
            if (this.energy.getEnergy() > 20) {
                List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(
                        EntityLivingBase.class,
                        new AxisAlignedBB(this.pos.getX() - 7, this.pos.getY() - 4, this.pos.getZ() - 7, (this.pos.getX() + 7),
                                (this.pos.getY() + 4),
                                (this.pos.getZ() + 7)
                        )
                );
                for (EntityLivingBase entity : entities) {
                    if (entity instanceof EntityPlayer) {
                        continue;
                    }
                    if (this.energy.getEnergy() < 20) {
                        break;
                    }
                    this.energy.useEnergy(20);
                    double x2 = entity.posX;
                    double y2 = entity.posY;
                    double z2 = entity.posZ;
                    double x1 = this.pos.getX() + 0.5D;
                    double y1 = this.pos.getY() + 0.5D;
                    double z1 = this.pos.getZ() + 0.5D;
                    final boolean blue = true;
                    float distanceSqrd = blue
                            ? (float) ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2))
                            : 1.1F;
                    if (distanceSqrd > 1.0F) {
                        final Vector3 originalPosVector = new Vector3(x1, y1, z1);
                        Vector3 entityVector = Vector3.fromEntityCenter(entity);
                        Vector3 finalVector = originalPosVector.copy().subtract(entityVector);
                        if (finalVector.mag() > 1.0D) {
                            finalVector.normalize();
                        }
                        entity.motionX = finalVector.x * 1 * 0.25F;
                        entity.motionY = finalVector.y * 1 * 0.25F;
                        entity.motionZ = finalVector.z * 1 * 0.25F;
                    }
                }
            }
        }


    }


    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.mob_magnet;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
