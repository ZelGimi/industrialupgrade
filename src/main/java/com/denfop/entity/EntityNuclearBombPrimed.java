package com.denfop.entity;

import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.network.packet.PacketUpdateRadiationValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class EntityNuclearBombPrimed extends Entity {

    private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(
            EntityNuclearBombPrimed.class,
            DataSerializers.VARINT
    );
    private static final Random RANDOM = new Random();
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityNuclearBombPrimed(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityNuclearBombPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
        this(worldIn);
        this.setPosition(x, y, z);
        float f = (float) (Math.random() * (Math.PI * 2D));
        this.motionX = -Math.sin(f) * 0.02F;
        this.motionY = 0.20000000298023224D;
        this.motionZ = -Math.cos(f) * 0.02F;
        this.setFuse(80);
        this.tntPlacedBy = igniter;
    }

    protected void entityInit() {
        this.dataManager.register(FUSE, 80);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity()) {
            this.motionY -= 0.03999999910593033D;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        --this.fuse;

        if (this.fuse <= 0) {
            this.setDead();
            if (!this.world.isRemote) {
                this.explode();
            } else {
                BlockPos explosionPos = new BlockPos(posX, posY, posZ);
                final int explosionRadius = 10;
                createRadiationParticles(explosionPos, (int) explosionRadius);
                createExplosionFlash(explosionPos, (int) explosionRadius);
            }
        } else {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode() {
        float explosionRadius = 20.0F;
        boolean causeFire = true;
        this.world.createExplosion(this, this.posX, this.posY + (this.height / 16.0F), this.posZ, explosionRadius, causeFire);

        this.world.playSound(
                null,
                this.posX,
                this.posY,
                this.posZ,
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                net.minecraft.util.SoundCategory.BLOCKS,
                4.0F,
                1.0F
        );

        BlockPos explosionPos = new BlockPos(posX, posY, posZ);


        if (!this.world.isRemote) {
            if (this.world.provider.getDimension() == 0) {
                int radiationValue = 5000;
                new PacketUpdateRadiationValue(this.world.getChunkFromBlockCoords(explosionPos).getPos(), radiationValue);
                spreadRadiation(explosionPos, radiationValue);
                affectNearbyPlayers(explosionPos, 30.0D);
            }
        }
    }


    private void createExplosionFlash(BlockPos center, int radius) {
        for (int i = 0; i < radius * 20; i++) {
            double x = center.getX() + RANDOM.nextDouble() * radius * 2 - radius;
            double y = center.getY() + RANDOM.nextDouble() * radius / 2;
            double z = center.getZ() + RANDOM.nextDouble() * radius * 2 - radius;

            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB_AMBIENT, x, y, z, 1.0D, 0.0D, 0.0D);
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, x, y, z, 1.0D, 0.0D, 0.0D);
        }
    }

    private void createRadiationParticles(BlockPos center, int radius) {
        for (int i = 0; i < radius * 50; i++) {
            double x = center.getX() + RANDOM.nextDouble() * radius * 2 - radius;
            double y = center.getY() + RANDOM.nextDouble() * radius;
            double z = center.getZ() + RANDOM.nextDouble() * radius * 2 - radius;

            this.world.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, 1.0D, 0.0D, 0.0D);
        }
    }

    private void spreadRadiation(BlockPos explosionPos, int initialRadiation) {
        int radius = 3;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos chunkCenter = explosionPos.add(x * 16, 0, z * 16);
                int radiationLevel = (int) (initialRadiation / (1 + Math.sqrt(x * x + z * z)));
                new PacketUpdateRadiationValue(this.world.getChunkFromBlockCoords(chunkCenter).getPos(), radiationLevel);
            }
        }
    }

    private void affectNearbyPlayers(BlockPos explosionPos, double radius) {
        List<EntityPlayer> players = this.world.getEntitiesWithinAABB(
                EntityPlayer.class,
                this.getEntityBoundingBox().grow(radius)
        );
        for (EntityPlayer player : players) {
            if (!hasRadiationSuit(player)) {
                player.addPotionEffect(new PotionEffect(IUPotion.radiation, 200, 0));
            }
        }
    }

    private boolean hasRadiationSuit(EntityPlayer player) {

        return IHazmatLike.hasCompleteHazmat(player, EnumLevelRadiation.LOW);
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("Fuse", (short) this.getFuse());
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.setFuse(compound.getShort("Fuse"));
    }

    @Nullable
    public EntityLivingBase getTntPlacedBy() {
        return this.tntPlacedBy;
    }

    public float getEyeHeight() {
        return 0.0F;
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        if (FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }
    }

    public int getFuseDataManager() {
        return this.dataManager.get(FUSE);
    }

    public int getFuse() {
        return this.fuse;
    }

    public void setFuse(int fuseIn) {
        this.dataManager.set(FUSE, fuseIn);
        this.fuse = fuseIn;
    }

}
