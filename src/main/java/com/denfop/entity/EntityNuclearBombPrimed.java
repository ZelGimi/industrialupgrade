package com.denfop.entity;


import com.denfop.IUItem;
import com.denfop.api.item.armor.HazmatLike;
import com.denfop.api.pollution.radiation.EnumLevelRadiation;
import com.denfop.network.packet.PacketUpdateRadiationValue;
import com.denfop.potion.IUPotion;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class EntityNuclearBombPrimed extends Entity {
    private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(EntityNuclearBombPrimed.class, EntityDataSerializers.INT);

    private LivingEntity owner;

    public EntityNuclearBombPrimed(EntityType<?> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
    }

    public EntityNuclearBombPrimed(Level level, double x, double y, double z, LivingEntity igniter) {
        this(IUItem.entity_nuclear_bomb.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = igniter;
        this.setFuse(80);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(FUSE, 80);
    }

    public int getFuse() {
        return this.entityData.get(FUSE);
    }

    public void setFuse(int ticks) {
        this.entityData.set(FUSE, ticks);
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.x, motion.y - 0.04, motion.z);
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.onGround()) {
            Vec3 damp = this.getDeltaMovement().multiply(0.7, -0.5, 0.7);
            this.setDeltaMovement(damp);
        }

        int fuse = this.getFuse() - 1;
        this.setFuse(fuse);

        if (fuse <= 0) {
            this.discard();
            explode();
        } else {
            this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE,
                    this.getX(), this.getY() + 0.5, this.getZ(), 0, 0, 0);
        }
    }

    private void explode() {
        level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 20.0F, Level.ExplosionInteraction.TNT);
        BlockPos explosionPos = new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ());

        if (!this.level().isClientSide) {
            if (this.level().dimension() == Level.OVERWORLD) {
                int radiationValue = 5000;
                new PacketUpdateRadiationValue(this.level().getChunk(explosionPos).getPos(), radiationValue);
                spreadRadiation(explosionPos, radiationValue);
                affectNearbyPlayers(explosionPos, 30.0D);
            }
        }
    }

    private void spreadRadiation(BlockPos explosionPos, int initialRadiation) {
        int radius = 3;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos chunkCenter = explosionPos.offset(x * 16, 0, z * 16);
                int radiationLevel = (int) (initialRadiation / (1 + Math.sqrt(x * x + z * z)));
                new PacketUpdateRadiationValue(this.level().getChunk(chunkCenter).getPos(), radiationLevel);
            }
        }
    }

    private void affectNearbyPlayers(BlockPos explosionPos, double radius) {
        List<Player> players = this.level().getEntitiesOfClass(
                Player.class,
                this.getBoundingBox().inflate(radius)
        );
        for (Player player : players) {
            if (!hasRadiationSuit(player)) {
                player.addEffect(new MobEffectInstance(IUPotion.radiation, 200, 0));
            }
        }
    }

    private boolean hasRadiationSuit(Player player) {

        return HazmatLike.hasCompleteHazmat(player, EnumLevelRadiation.LOW);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setFuse(tag.getShort("Fuse"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putShort("Fuse", (short) this.getFuse());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public LivingEntity getOwner() {
        return this.owner;
    }
}
