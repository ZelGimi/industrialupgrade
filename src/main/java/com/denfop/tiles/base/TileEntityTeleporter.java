package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.utils.ModUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileEntityTeleporter extends TileElectricMachine {

    private BlockPos target;
    private int targetCheckTicker;
    private int cooldown;

    public TileEntityTeleporter() {
        super(500000, 14, 0);
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
        return IUItem.basemachine2;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("targetX")) {
            this.target = new BlockPos(nbt.getInteger("targetX"), nbt.getInteger("targetY"), nbt.getInteger("targetZ"));
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (this.target != null) {
            nbt.setInteger("targetX", this.target.getX());
            nbt.setInteger("targetY", this.target.getY());
            nbt.setInteger("targetZ", this.target.getZ());
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

        World world = this.getWorld();
        if (world.isBlockPowered(this.pos) && this.target != null) {
            this.setActive(true);
            List entitiesNearby;
            if (coolingDown) {
                entitiesNearby = Collections.emptyList();
            } else {
                entitiesNearby = world.getEntitiesWithinAABB(
                        Entity.class,
                        new AxisAlignedBB(
                                this.pos.getX() - 1,
                                this.pos.getY(),
                                this.pos.getZ() - 1,
                                this.pos.getX() + 2,
                                this.pos.getY() + 3,
                                this.pos.getZ() + 2
                        )
                );
            }

            if (!entitiesNearby.isEmpty() && this.verifyTarget()) {
                double minDistanceSquared = Double.MAX_VALUE;
                Entity closestEntity = null;

                for (final Object o : entitiesNearby) {
                    Entity entity = (Entity) o;
                    if (entity.getRidingEntity() == null) {
                        double distSquared = this.pos.distanceSqToCenter(entity.posX, entity.posY, entity.posZ);
                        if (distSquared < minDistanceSquared) {
                            minDistanceSquared = distSquared;
                            closestEntity = entity;
                        }
                    }
                }

                assert closestEntity != null;
                world.playSound(null, this.pos, EnumSound.teleporter.getSoundEvent(), SoundCategory.BLOCKS, 1, 1);
                this.teleport(closestEntity, Math.sqrt(this.pos.distanceSq(this.target)));
            } else if (++this.targetCheckTicker % 1024 == 0) {
                this.verifyTarget();
            }
        } else {
            this.setActive(false);
        }

    }

    private boolean verifyTarget() {
        if (this.getWorld().getTileEntity(this.target) instanceof TileEntityTeleporter) {
            return true;
        } else {
            this.target = null;
            this.setActive(false);
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
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
            int energyCost = (int) ((double) weight * Math.pow(distance + 10.0, 0.7) * 5.0);
            if (energyCost <= this.getAvailableEnergy()) {
                this.consumeEnergy(energyCost);
                if (user instanceof EntityPlayerMP) {
                    user.setPositionAndUpdate(
                            (double) this.target.getX() + 0.5,
                            (double) this.target.getY() + 1.5 + user.getYOffset(),
                            (double) this.target.getZ() + 0.5
                    );
                } else {
                    user.setPositionAndRotation(
                            (double) this.target.getX() + 0.5,
                            (double) this.target.getY() + 1.5 + user.getYOffset(),
                            (double) this.target.getZ() + 0.5,
                            user.rotationYaw,
                            user.rotationPitch
                    );
                }

                TileEntity te = this.getWorld().getTileEntity(this.target);

                assert te instanceof TileEntityTeleporter;

                ((TileEntityTeleporter) te).onTeleportTo();
            }
        }
    }

    public void spawnBlueParticles(int n, BlockPos pos) {
        this.spawnParticles(n, pos, 0, 1);
    }

    public void spawnGreenParticles(int n, BlockPos pos) {
        this.spawnParticles(n, pos, 1, 0);
    }

    private void spawnParticles(int n, BlockPos pos, int green, int blue) {
        World world = this.getWorld();
        Random rnd = world.rand;

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

    public void consumeEnergy(int energy) {
        this.energy.useEnergy(energy);

    }

    public int getAvailableEnergy() {


        return (int) this.energy.getEnergy();
    }

    public int getWeightOf(Entity user) {
        boolean teleporterUseInventoryWeight = true;
        int weight = 0;
        ItemStack stack1;
        Iterator<ItemStack> var7;
        if (user instanceof EntityItem) {
            ItemStack is = ((EntityItem) user).getItem();
            weight += 100 * ModUtils.getSize(is) / is.getMaxStackSize();
        } else if (!(user instanceof EntityAnimal) && !(user instanceof EntityMinecart) && !(user instanceof EntityBoat)) {
            if (user instanceof EntityPlayer) {
                weight += 1000;
                if (teleporterUseInventoryWeight) {
                    for (var7 = ((EntityPlayer) user).inventory.mainInventory.iterator(); var7.hasNext(); weight += getStackCost(
                            stack1)) {
                        stack1 = var7.next();
                    }
                }
            } else if (user instanceof EntityGhast) {
                weight += 2500;
            } else if (user instanceof EntityWither) {
                weight += 5000;
            } else if (user instanceof EntityDragon) {
                weight += 10000;
            } else if (user instanceof EntityCreature) {
                weight += 500;
            }
        } else {
            weight += 100;
        }

        if (user instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) user;

            ItemStack stack;
            for (Iterator<ItemStack> var9 = living
                    .getEquipmentAndArmor()
                    .iterator(); var9.hasNext(); weight += getStackCost(stack)) {
                stack = var9.next();
            }

            if (user instanceof EntityPlayer) {
                stack = living.getHeldItemMainhand();
                weight -= getStackCost(stack);
            }
        }
        Iterator<Entity> var10;
        Entity passenger;
        for (var10 = user.getPassengers().iterator(); var10.hasNext(); weight += this.getWeightOf(passenger)) {
            passenger = var10.next();
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
