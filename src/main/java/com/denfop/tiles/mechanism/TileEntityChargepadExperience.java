package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;

public class TileEntityChargepadExperience extends TileEntityInventory {

    private final ComponentBaseEnergy energy;
    private Player player;

    public TileEntityChargepadExperience(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.exp_chargepad,pos,state);
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.EXPERIENCE, this, 2000000000, 14));
        this.energy.setDirections(ModUtils.allFacings, ModUtils.allFacings);
    }

    public void onEntityCollision(Entity entity) {
        super.onEntityCollision(entity);
        if (!this.getWorld().isClientSide && entity instanceof Player) {
            if (this.canEntityDestroy(entity)) {
                this.updatePlayer((Player) entity);
            }

        }

    }

    protected void getItems(Player player) {
        if (!this.canEntityDestroy(player)) {
            IUCore.proxy.messagePlayer(player, Localization.translate("iu.error"));
            return;
        }
        chargeitems(player);


    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.exp_chargepad;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.player != null && this.energy.getEnergy() >= 1.0D) {
            if (!getActive()) {
                setActive(true);
            }
            getItems(this.player);
            this.player = null;
        } else if (getActive()) {
            setActive(false);
        }
        updatePlayer(null);
    }

    protected void updatePlayer(Player entity) {
        this.player = entity;

    }

    public List<AABB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AABB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D));
    }

    protected void chargeitems(Player player) {
        final double value = Math.min(this.energy.getEnergy(), 1);
        this.energy.storage -= (value - ExperienceUtils.addPlayerXP1(player, (int) value));
    }

    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            RandomSource rnd = this.level.random;
            final int n = 4;
            float red = 0.0F;
            float green = 1.0F;
            float blue = 0.0F;
            float size = 1.0F;

            DustParticleOptions redstoneGreen = new DustParticleOptions(new Vector3f(red, green, blue), size);

            for (int i = 0; i < n; ++i) {
                this.level.addParticle(
                        redstoneGreen,
                        this.worldPosition.getX() + rnd.nextFloat(),
                        this.worldPosition.getY() + 1 + rnd.nextFloat(),
                        this.worldPosition.getZ() + rnd.nextFloat(),
                        0.0D, 0.0D, 0.0D
                );

                this.level.addParticle(
                        redstoneGreen,
                        this.worldPosition.getX() + rnd.nextFloat(),
                        this.worldPosition.getY() + 2 + rnd.nextFloat(),
                        this.worldPosition.getZ() + rnd.nextFloat(),
                        0.0D, 0.0D, 0.0D
                );
            }
        }


    }

}
