package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IAdvEnergySource;
import com.denfop.api.energy.IAdvEnergyTile;
import com.denfop.audio.AudioPosition;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.componets.ComparatorEmitter;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TileEntityTeleporter extends TileEntityInventory implements INetworkTileEntityEventListener {

    protected final ComparatorEmitter comparator;
    private BlockPos target;
    private AudioSource audioSource = null;
    private int targetCheckTicker;
    private int cooldown;

    public TileEntityTeleporter() {
        this.targetCheckTicker = IC2.random.nextInt(1024);
        this.cooldown = 0;
        this.comparator = this.addComponent(new ComparatorEmitter(this));
    }

    private static int getStackCost(ItemStack stack) {
        return StackUtil.isEmpty(stack) ? 0 : 100 * StackUtil.getSize(stack) / stack.getMaxStackSize();
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

    protected void onUnloaded() {
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }

        super.onUnloaded();
    }

    protected void onLoaded() {
        super.onLoaded();
        this.updateComparatorLevel();
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean coolingDown = this.cooldown > 0;
        if (coolingDown) {
            --this.cooldown;
            IUCore.network.get(true).updateTileEntityField(this, "cooldown");
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
            this.updateComparatorLevel();
            this.setActive(false);
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    protected void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            if (this.cooldown > 0) {
                this.spawnGreenParticles(2, this.pos);
            } else {
                this.spawnBlueParticles(2, this.pos);
            }
        }

    }

    private void updateComparatorLevel() {
        int targetLevel = this.target != null ? 15 : 0;
        this.comparator.setLevel(targetLevel);
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

                ((TileEntityTeleporter) te).onTeleportTo(this, user);
                IUCore.network.get(true).initiateTileEntityEvent(this, 0, true);
                if (user instanceof EntityPlayer && distance >= 1000.0) {
                    IC2.achievements.issueAchievement((EntityPlayer) user, "teleportFarAway");
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
        World world = this.getWorld();
        List<IAdvEnergySource> energySources = new LinkedList<>();
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
            IAdvEnergyTile energyTile = EnergyNetGlobal.instance.getTile(world, this.pos.offset(dir));
            if (energyTile instanceof IAdvEnergySource) {
                IAdvEnergySource energySource = (IAdvEnergySource) energyTile;
                if (energySource.getOfferedEnergy() > 0) {
                    energySources.add(energySource);
                }
            }
        }

        while (energy > 0) {
            int drain = (energy + energySources.size() - 1) / energySources.size();
            Iterator<IAdvEnergySource> it = energySources.iterator();

            while (it.hasNext()) {
                IAdvEnergySource energySource = it.next();
                if (drain > energy) {
                    drain = energy;
                }

                if (energySource.getOfferedEnergy() <= drain) {
                    energy -= energySource.getOfferedEnergy();
                    energySource.drawEnergy(energySource.getOfferedEnergy());
                    it.remove();
                } else {
                    energy -= drain;
                    energySource.drawEnergy(drain);
                }
            }
        }

    }

    public int getAvailableEnergy() {
        World world = this.getWorld();
        int energy = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
            IAdvEnergyTile energyTile = EnergyNetGlobal.instance.getTile(world, this.pos.offset(dir));
            if (energyTile instanceof IAdvEnergySource) {
                IAdvEnergySource energySource = (IAdvEnergySource) energyTile;
                energy += energySource.getOfferedEnergy();
            }
        }

        return energy;
    }

    public int getWeightOf(Entity user) {
        boolean teleporterUseInventoryWeight = ConfigUtil.getBool(MainConfig.get(), "balance/teleporterUseInventoryWeight");
        int weight = 0;
        ItemStack stack1;
        Iterator<ItemStack> var7;
        if (user instanceof EntityItem) {
            ItemStack is = ((EntityItem) user).getItem();
            weight += 100 * StackUtil.getSize(is) / is.getMaxStackSize();
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

        if (teleporterUseInventoryWeight && user instanceof EntityLivingBase) {
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

    private void onTeleportTo(TileEntityTeleporter from, Entity entity) {
        this.cooldown = 20;
    }

    protected boolean canEntityDestroy(Entity entity) {
        return !(entity instanceof EntityDragon) && !(entity instanceof EntityWither);
    }

    public boolean hasTarget() {
        return this.target != null;
    }

    public BlockPos getTarget() {
        return this.target;
    }

    public void setTarget(BlockPos pos) {
        this.target = pos;
        this.updateComparatorLevel();
        (IUCore.network.get(true)).updateTileEntityField(this, "target");
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("target");
        return ret;
    }

    public void onNetworkUpdate(String field) {
        if (field.equals("active")) {
            if (this.audioSource == null) {
                this.audioSource = IUCore.audioManager.createSource(this, PositionSpec.Center, "Machines/Teleporter" +
                        "/TeleChargedLoop.ogg", true, false, IC2.audioManager.getDefaultVolume());
            }

            if (this.getActive()) {
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
            } else if (this.audioSource != null) {
                this.audioSource.stop();
            }
        }

        super.onNetworkUpdate(field);
    }

    public void onNetworkEvent(int event) {
        if (event == 0) {
            IC2.audioManager.playOnce(this, "Machines/Teleporter/TeleUse.ogg");
            IC2.audioManager.playOnce(new AudioPosition(this.getWorld(), this.pos), "Machines/Teleporter/TeleUse.ogg");
            this.spawnBlueParticles(20, this.pos);
            this.spawnBlueParticles(20, this.target);
        } else {
            IC2.platform.displayError(
                    "An unknown event type was received over multiplayer.\nThis could happen due to corrupted data or a bug.\n\n(Technical information: event ID " + event + ", tile entity below)\nT: " + this + " (" + this.pos + ")"
            );
        }

    }

}
