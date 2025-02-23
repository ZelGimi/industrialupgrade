package com.denfop.tiles.transport.tiles;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.EnergyTick;
import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergyConductor;
import com.denfop.api.energy.IEnergyEmitter;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.InfoCable;
import com.denfop.api.energy.NodeStats;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketCableSound;
import com.denfop.tiles.transport.types.CableType;
import com.denfop.tiles.transport.types.ICableItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class TileEntityCable extends TileEntityMultiCable implements IEnergyConductor {


    public boolean addedToEnergyNet;
    public int type;
    protected CableType cableType;
    private boolean needUpdate;
    private ChunkPos chunkPos;

    public TileEntityCable(CableType cableType) {
        super(cableType);
        this.addedToEnergyNet = false;
        this.cableType = cableType;
        this.type = cableType.ordinal();
    }

    public TileEntityCable() {
        super(CableType.glass);
        this.addedToEnergyNet = false;
        this.cableType = CableType.glass;
        this.type = cableType.ordinal();
    }

    public static TileEntityCable delegate(CableType cableType) {
        return new TileEntityCable(cableType);
    }

    @Override
    public void onEntityCollision(final Entity entity) {
        super.onEntityCollision(entity);
        if (!this.getWorld().isRemote && entity instanceof EntityLivingBase) {
            if (cableType == CableType.tin || cableType == CableType.copper || cableType == CableType.gold || cableType == CableType.iron) {
                NodeStats stats = EnergyNetGlobal.instance.getNodeStats(this);
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    if (!IHazmatLike.hasCompleteHazmat(player)) {
                        if (stats.getEnergyIn() > 0) {
                            entity.attackEntityFrom(IUDamageSource.current, 0.25f)
                            ;
                        }
                    }
                } else if (stats.getEnergyIn() > 0) {
                    entity.attackEntityFrom(IUDamageSource.current, 0.25f)
                    ;
                }
            }

        }
    }
    boolean updateConnect = false;
    public IMultiTileBlock getTeBlock() {
        return BlockCable.cable_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.cableblock;
    }

    public ICableItem getCableItem() {
        return cableType;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cableType = CableType.values[nbt.getByte("cableType") & 255];
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        super.updateTileServer(var1, var2);
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
        this.needUpdate = true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this, this));
            this.needUpdate = false;
            this.updateConnectivity();
        }
        if (updateConnect){
            updateConnect = false;
            this.updateConnectivity();
        }
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote && !addedToEnergyNet) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this, this));
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    public void onUnloaded() {
        if (IUCore.proxy.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
            this.addedToEnergyNet = false;
            this.updateConnectivity();
        }


        super.onUnloaded();
    }

    public SoundType getBlockSound(Entity entity) {
        return SoundType.CLOTH;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
    }

    public ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return new ItemStack(IUItem.cable, 1, this.cableType.ordinal());
    }

    @Override
    public ItemStack getItem(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.cable, 1, this.cableType.ordinal());
    }

    public void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);


    }
    @Override
    public InfoCable getCable() {
        return cable;
    }

    private InfoCable cable;
    @Override
    public void setCable(final InfoCable cable) {
        this.cable=cable;
    }


    public void updateConnectivity() {
        byte newConnectivity = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            IEnergyTile tile = energyConductorMap.get(dir);
            if (!this.getBlackList().contains(dir)) {
                if ((tile instanceof IEnergyAcceptor && ((IEnergyAcceptor) tile).acceptsEnergyFrom(
                        this,
                        dir.getOpposite()
                ) || tile instanceof IEnergyEmitter && ((IEnergyEmitter) tile).emitsEnergyTo(
                        this,
                        dir.getOpposite()
                ))) {
                    newConnectivity = (byte) (newConnectivity + 1);
                }
            }


        }
        setConnectivity(newConnectivity);
        this.cableItem = cableType;

    }

    public boolean wrenchCanRemove(EntityPlayer player) {
        return false;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing direction) {
        return !getBlackList().contains(direction);
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        return !getBlackList().contains(direction);
    }


    public double getConductionLoss() {
        return this.cableType.loss;
    }

    public double getConductorBreakdownEnergy() {
        return this.cableType.capacity + 1;
    }




    public void removeConductor() {
        this.getWorld().setBlockToAir(this.pos);
        new PacketCableSound(this.getWorld(), this.pos,
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
        );
    }




    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, cableType);
            EncoderHandler.encode(packet, connectivity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            cableType = CableType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Map<EnumFacing, IEnergyTile> getTiles() {
        return energyConductorMap;
    }

    Map<EnumFacing, IEnergyTile> energyConductorMap = new HashMap<>();
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();

    @Override
    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }
    public long getIdNetwork() {
        return this.id;
    }
    int hashCodeSource;
    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }


    public void setId(final long id) {
        this.id = id;
    }
    private long id;

    @Override
    public void RemoveTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IEnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()){
                InfoTile<IEnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
            updateConnect = true;
        }
    }

    @Override
    public void AddTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
            if(!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
            updateConnect = true;
        }
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

}
