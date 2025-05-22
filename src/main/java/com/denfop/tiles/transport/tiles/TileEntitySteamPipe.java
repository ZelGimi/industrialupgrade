package com.denfop.tiles.transport.tiles;


import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyEvent;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.sytem.EnumTypeEvent;
import com.denfop.api.sytem.IAcceptor;
import com.denfop.api.sytem.IConductor;
import com.denfop.api.sytem.IEmitter;
import com.denfop.api.sytem.ITile;
import com.denfop.api.sytem.InfoCable;
import com.denfop.api.sytem.InfoTile;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamPipe;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.transport.types.ICableItem;
import com.denfop.tiles.transport.types.SteamType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class TileEntitySteamPipe extends TileEntityMultiCable implements IConductor {


    public boolean addedToEnergyNet;
    protected SteamType cableType;
    Map<EnumFacing, ITile> energyConductorMap = new HashMap<>();
    List<InfoTile<ITile>> validReceivers = new LinkedList<>();
    int hashCodeSource;
    boolean updateConnect = false;
    private boolean needUpdate;
    private long id;
    private InfoCable cable;

    public TileEntitySteamPipe(SteamType cableType) {
        super(cableType);
        this.cableType = cableType;
    }


    public TileEntitySteamPipe() {
        super(SteamType.spipe);
        this.cableType = SteamType.spipe;
        this.connectivity = 0;
        this.addedToEnergyNet = false;

    }

    public static TileEntitySteamPipe delegate(SteamType cableType) {
        return new TileEntitySteamPipe(cableType);
    }

    public long getIdNetwork() {
        return this.id;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public List<InfoTile<ITile>> getValidReceivers(EnergyType type) {
        return validReceivers;
    }

    public Map<EnumFacing, ITile> getTiles(EnergyType type) {
        return energyConductorMap;
    }

    @Override
    public InfoCable getCable(EnergyType type) {
        return cable;
    }

    @Override
    public void setCable(EnergyType type, final InfoCable cable) {
        this.cable = cable;
    }

    @Override
    public void RemoveTile(EnergyType type, ITile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<ITile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<ITile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
            updateConnect = true;
        }
    }

    @Override
    public void AddTile(EnergyType type, ITile tile, final EnumFacing facing1) {
        if (!this.getWorld().isRemote) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
            updateConnect = true;
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockSteamPipe.spipe;
    }

    public BlockTileEntity getBlock() {
        return IUItem.steamPipeBlock;
    }

    public ICableItem getCableItem() {
        return cableType;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cableType = SteamType.values[nbt.getByte("cableType") & 255];
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        super.updateTileServer(var1, var2);
        MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.STEAM, this));
        this.needUpdate = true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.STEAM, this));
            this.needUpdate = false;
            this.updateConnectivity();
        }
        if (updateConnect) {
            updateConnect = false;
            this.updateConnectivity();
        }
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote && !addedToEnergyNet) {
            this.energyConductorMap.clear();
            this.validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.STEAM, this));
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    public void onUnloaded() {
        if (IUCore.proxy.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.STEAM, this));
            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
    }


    public ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return new ItemStack(IUItem.steamPipe, 1, 0);
    }


    public void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (!this.getWorld().isRemote) {
            this.updateConnectivity();
        }

    }


    public void updateConnectivity() {
        byte newConnectivity = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;
        final Map<EnumFacing, ITile> map = this.energyConductorMap;
        for (EnumFacing dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            final ITile tile = map.get(dir);
            if (dir != null && !getBlackList().contains(dir)) {
                if (tile instanceof IAcceptor && ((IAcceptor) tile).acceptsFrom(
                        this,
                        dir.getOpposite()
                ) || tile instanceof IEmitter && ((IEmitter) tile).emitsTo(
                        this,
                        dir.getOpposite()
                )) {
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

    public boolean acceptsFrom(IEmitter emitter, EnumFacing direction) {
        return (!getBlackList().contains(direction));
    }

    public boolean emitsTo(IAcceptor receiver, EnumFacing direction) {
        return (!getBlackList().contains(direction));
    }

    public boolean canInteractWith() {

        return true;
    }

    public double getConductorBreakdownEnergy(EnergyType type) {
        return this.cableType.capacity + 1;
    }


    public void removeConductor() {
        this.getWorld().setBlockToAir(this.pos);
        world.playSound(
                null,
                this.pos,
                SoundEvents.ENTITY_GENERIC_BURN,
                SoundCategory.BLOCKS,
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
        );
    }


    @Override
    public EnergyType getEnergyType() {
        return EnergyType.STEAM;
    }

    @Override
    public boolean hasEnergies() {
        return false;
    }

    @Override
    public List<EnergyType> getEnergies() {
        return null;
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
            cableType = SteamType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public TileEntity getTile() {
        return this;
    }


}
