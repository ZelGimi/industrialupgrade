package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TileEntityMobMagnet extends TileEntityInventory {

    private final Energy energy;

    public TileEntityMobMagnet(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.mob_magnet, pos, state);
        this.energy = this.addComponent(Energy.asBasicSink(this, 10000, 14));

        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.level.getGameTime() % 4 == 0) {
            if (this.energy.getEnergy() > 20) {
                AABB aabb = new AABB(
                        this.worldPosition.getX() - 7, this.worldPosition.getY() - 4, this.worldPosition.getZ() - 7,
                        this.worldPosition.getX() + 7, this.worldPosition.getY() + 4, this.worldPosition.getZ() + 7
                );

                List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, aabb);

                for (LivingEntity entity : entities) {
                    if (entity instanceof Player) continue;
                    if (this.energy.getEnergy() < 20) break;

                    this.energy.useEnergy(20);

                    double x2 = entity.getX();
                    double y2 = entity.getY();
                    double z2 = entity.getZ();

                    double x1 = this.worldPosition.getX() + 0.5D;
                    double y1 = this.worldPosition.getY() + 0.5D;
                    double z1 = this.worldPosition.getZ() + 0.5D;

                    float distanceSqrd = (float) ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
                    if (distanceSqrd > 1.0F) {
                        Vec3 originalPos = new Vec3(x1, y1, z1);
                        Vec3 entityPos = entity.position().add(0.0D, entity.getBbHeight() / 2.0D, 0.0D);
                        Vec3 motion = originalPos.subtract(entityPos).normalize().scale(0.25F);

                        entity.setDeltaMovement(motion);
                        entity.hurtMarked = true;
                    }
                }
            }

        }


    }


    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.mob_magnet;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
