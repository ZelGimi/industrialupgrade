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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TileEntityChargepadExperience extends TileEntityInventory {

    private final ComponentBaseEnergy energy;
    private EntityPlayer player;

    public TileEntityChargepadExperience() {
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.EXPERIENCE, this, 2000000000, 14));
        this.energy.setDirections(ModUtils.allFacings, ModUtils.allFacings);
    }

    public void onEntityCollision(Entity entity) {
        super.onEntityCollision(entity);
        if (!this.getWorld().isRemote && entity instanceof EntityPlayer) {
            if (this.canEntityDestroy(entity)) {
                this.updatePlayer((EntityPlayer) entity);
            }

        }

    }

    protected void getItems(EntityPlayer player) {
        if (!this.canEntityDestroy(player)) {
            IUCore.proxy.messagePlayer(player, Localization.translate("iu.error"));
            return;
        }
        chargeitems(player);


    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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

    protected void updatePlayer(EntityPlayer entity) {
        this.player = entity;

    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D));
    }

    protected void chargeitems(EntityPlayer player) {
        final double value = Math.min(this.energy.getEnergy(), 1);
        this.energy.storage -= (value - ExperienceUtils.addPlayerXP1(player, (int) value));
    }

    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            World world = this.getWorld();
            Random rnd = world.rand;
            final int n = 4;
            final int green = 1;
            int blue = 0;
            for (int i = 0; i < n; ++i) {
                world.spawnParticle(
                        EnumParticleTypes.REDSTONE,
                        (float) pos.getX() + rnd.nextFloat(),
                        (float) (pos.getY() + 1) + rnd.nextFloat(),
                        (float) pos.getZ() + rnd.nextFloat(),
                        -1,
                        green,
                        blue
                );
                world.spawnParticle(
                        EnumParticleTypes.REDSTONE,
                        (float) pos.getX() + rnd.nextFloat(),
                        (float) (pos.getY() + 2) + rnd.nextFloat(),
                        (float) pos.getZ() + rnd.nextFloat(),
                        -1,
                        green,
                        blue
                );
            }
        }

    }

}
