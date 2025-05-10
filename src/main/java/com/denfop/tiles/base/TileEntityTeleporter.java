package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.utils.ModUtils;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TileEntityTeleporter extends TileElectricMachine {

    private BlockPos target;
    private int targetCheckTicker;
    private int cooldown;

    public TileEntityTeleporter(BlockPos pos, BlockState state) {
        super(500000, 14, 0,BlockBaseMachine3.teleporter_iu,pos,state);
        this.targetCheckTicker = IUCore.random.nextInt(1024);
        this.cooldown = 0;
    }

    private static int getStackCost(ItemStack stack) {
        return ModUtils.isEmpty(stack) ? 0 : 100 * ModUtils.getSize(stack) / stack.getMaxStackSize();
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.teleporter_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        if (nbt.contains("targetX")) {
            this.target = new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ"));
        }

    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        if (this.target != null) {
            nbt.putInt("targetX", this.target.getX());
            nbt.putInt("targetY", this.target.getY());
            nbt.putInt("targetZ", this.target.getZ());
        }

        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        boolean coolingDown = this.cooldown > 0;
        if (coolingDown) {
            --this.cooldown;
            new PacketUpdateFieldTile(this, "cooldown", cooldown);
        }

        Level level = this.level;
        if (level.hasNeighborSignal(this.worldPosition) && this.target != null) {
            this.setActive(true);

            List<Entity> entitiesNearby;
            if (coolingDown) {
                entitiesNearby = Collections.emptyList();
            } else {
                AABB aabb = new AABB(
                        this.worldPosition.getX() - 1,
                        this.worldPosition.getY(),
                        this.worldPosition.getZ() - 1,
                        this.worldPosition.getX() + 2,
                        this.worldPosition.getY() + 3,
                        this.worldPosition.getZ() + 2
                );
                entitiesNearby = level.getEntitiesOfClass(Entity.class, aabb);
            }

            if (!entitiesNearby.isEmpty() && this.verifyTarget()) {
                double minDistanceSquared = Double.MAX_VALUE;
                Entity closestEntity = null;

                for (Entity entity : entitiesNearby) {
                    if (entity.getVehicle() == null) {
                        double distSquared = entity.distanceToSqr(Vec3.atCenterOf(this.worldPosition));
                        if (distSquared < minDistanceSquared) {
                            minDistanceSquared = distSquared;
                            closestEntity = entity;
                        }
                    }
                }

                if (closestEntity != null) {
                    level.playSound(null, this.worldPosition, getSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    this.teleport(closestEntity, Math.sqrt(Vec3.atCenterOf(this.worldPosition).distanceToSqr(Vec3.atCenterOf(this.target))));
                }
            } else if (++this.targetCheckTicker % 1024 == 0) {
                this.verifyTarget();
            }
        } else {
            this.setActive(false);
        }


    }

    private boolean verifyTarget() {
        if (this.getWorld().getBlockEntity(this.target) instanceof TileEntityTeleporter) {
            return true;
        } else {
            this.target = null;
            this.setActive(false);
            return false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            if (this.cooldown > 0) {
                this.spawnGreenParticles(2, this.pos);
            } else {
                this.spawnBlueParticles(2, this.pos);
            }
        }

    }


    public void teleport(Entity user, double distance) {
        int weight = this.getWeightOf(user);
        if (weight != 0) {
            int energyCost = (int) (weight * Math.pow(distance + 10.0, 0.7) * 5.0);
            if (energyCost <= this.getAvailableEnergy()) {
                this.consumeEnergy(energyCost);

                double targetX = this.target.getX() + 0.5;
                double targetY = this.target.getY() + 1.5;
                double targetZ = this.target.getZ() + 0.5;

                if (user instanceof ServerPlayer serverPlayer) {
                    serverPlayer.teleportTo(targetX, targetY, targetZ);
                } else {
                    user.teleportTo(targetX, targetY, targetZ);
                    user.setYRot(user.getYRot());
                    user.setXRot(user.getXRot());
                }

                BlockEntity be = this.level.getBlockEntity(this.target);
                if (be instanceof TileEntityTeleporter teleporter) {
                    teleporter.onTeleportTo();
                }
            }
        }
    }


    public void spawnBlueParticles(int n, BlockPos pos) {
        this.spawnParticles(n, pos, 0, 1);
    }

    public void spawnGreenParticles(int n, BlockPos pos) {
        this.spawnParticles(n, pos, 1, 0);
    }

    private void spawnParticles(int n, BlockPos pos, float green, float blue) {
        if (this.level == null || this.level.isClientSide) return;

        RandomSource rnd = this.level.random;

        for (int i = 0; i < n; ++i) {
            float x = pos.getX() + rnd.nextFloat();
            float y1 = pos.getY() + 1 + rnd.nextFloat();
            float y2 = pos.getY() + 2 + rnd.nextFloat();
            float z = pos.getZ() + rnd.nextFloat();


            DustParticleOptions particle = new DustParticleOptions(new Vector3f(1.0f, green, blue), 1.0f);

            level.addParticle(particle, x, y1, z, 0.0, 0.0, 0.0);
            level.addParticle(particle, x, y2, z, 0.0, 0.0, 0.0);
        }
    }


    public void consumeEnergy(int energy) {
        this.energy.useEnergy(energy);

    }

    public int getAvailableEnergy() {


        return (int) this.energy.getEnergy();
    }

    public int getWeightOf(Entity user) {
        boolean teleporterUseInventoryWeight = true;
        int weight = 0;

        if (user instanceof ItemEntity itemEntity) {
            ItemStack is = itemEntity.getItem();
            weight += 100 * ModUtils.getSize(is) / is.getMaxStackSize();
        } else if (!(user instanceof Animal) && !(user instanceof AbstractMinecart) && !(user instanceof Boat)) {
            if (user instanceof Player player) {
                weight += 1000;
                if (teleporterUseInventoryWeight) {
                    for (ItemStack stack : player.getInventory().items) {
                        weight += getStackCost(stack);
                    }
                }
            } else if (user instanceof Ghast) {
                weight += 2500;
            } else if (user instanceof WitherBoss) {
                weight += 5000;
            } else if (user instanceof EnderDragon) {
                weight += 10000;
            } else if (user instanceof PathfinderMob) {
                weight += 500;
            }
        } else {
            weight += 100;
        }

        if (user instanceof LivingEntity living) {
            for (ItemStack stack : living.getArmorSlots()) {
                weight += getStackCost(stack);
            }

            if (user instanceof Player) {
                ItemStack stack = living.getMainHandItem();
                weight -= getStackCost(stack);
            }
        }

        for (Entity passenger : user.getPassengers()) {
            weight += this.getWeightOf(passenger);
        }

        return weight;
    }


    private void onTeleportTo() {
        this.cooldown = 20;
    }


    public boolean hasTarget() {
        return this.target != null;
    }

    public BlockPos getTarget() {
        return this.target;
    }

    public void setTarget(BlockPos pos) {
        this.target = pos;
        new PacketUpdateFieldTile(this, "target", target);
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("cooldown")) {
            try {
                this.cooldown = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("target")) {
            try {
                this.target = (BlockPos) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            target = (BlockPos) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
