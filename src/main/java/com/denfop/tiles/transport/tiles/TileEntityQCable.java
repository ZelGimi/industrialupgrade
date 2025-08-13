package com.denfop.tiles.transport.tiles;


import com.denfop.api.energy.ConductorInfo;
import com.denfop.api.sytem.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.transport.types.QEType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.*;


public class TileEntityQCable extends TileEntityMultiCable implements IConductor {


    public boolean addedToEnergyNet;
    protected QEType cableType;
    Map<Direction, ITile> energyConductorMap = new HashMap<>();
    List<InfoTile<ITile>> validReceivers = new LinkedList<>();
    int hashCodeSource;
    boolean updateConnect = false;
    private boolean needUpdate;
    private InfoCable cable;
    private long id;

    public TileEntityQCable(QEType cableType, IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(cableType, tileBlock, pos, state);
        this.cableType = cableType;
    }
    Map<EnergyType, ConductorInfo> conductorInfoMap = new HashMap<>();

    @Override
    public ConductorInfo getInfo(EnergyType energyType) {
        if (conductorInfoMap.isEmpty()) {
            if (getEnergies() == null || getEnergies().isEmpty()) {
                conductorInfoMap.put(getEnergyType(), new ConductorInfo(pos, this, getEnergyType()));
            } else {
                for (EnergyType e : getEnergies())
                    conductorInfoMap.put(e, new ConductorInfo(pos, this, e));
            }
        }
        return conductorInfoMap.get(energyType);
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
    public BlockPos getBlockPos() {
        return this.pos;
    }

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.cableType = QEType.values[nbt.getByte("cableType") & 255];
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        super.updateTileServer(var1, var2);
        MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.QUANTUM, this));
        this.needUpdate = true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.QUANTUM, this));
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
        if (!this.getWorld().isClientSide && !addedToEnergyNet) {
            this.energyConductorMap.clear();
            this.validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.LOAD, EnergyType.QUANTUM, this));
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    public void onUnloaded() {
        if (!this.getWorld().isClientSide && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.getWorld(), EnumTypeEvent.UNLOAD, EnergyType.QUANTUM, this));
            this.addedToEnergyNet = false;
        }


        super.onUnloaded();
    }

    public Map<Direction, ITile> getTiles(EnergyType type) {
        return energyConductorMap;
    }


    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (!this.getWorld().isClientSide) {
            this.updateConnectivity();
        }

    }

    public void updateConnectivity() {
        byte newConnectivity = 0;
        Direction[] var4 = Direction.values();
        final Map<Direction, ITile> map = this.energyConductorMap;
        for (Direction dir : var4) {
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

    public boolean wrenchCanRemove(Player player) {
        return false;
    }

    public boolean acceptsFrom(IEmitter emitter, Direction direction) {
        return (!getBlackList().contains(direction));
    }

    public boolean emitsTo(IAcceptor receiver, Direction direction) {
        return (!getBlackList().contains(direction));
    }

    public boolean canInteractWith() {

        return true;
    }

    public double getConductorBreakdownEnergy(EnergyType type) {
        return this.cableType.capacity + 1;
    }

    public Map<Direction, ITile> getTiles() {
        return energyConductorMap;
    }

    @Override
    public List<InfoTile<ITile>> getValidReceivers(EnergyType type) {
        return validReceivers;
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
    public void RemoveTile(EnergyType type, ITile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
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
    public void AddTile(EnergyType type, ITile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
            updateConnect = true;
        }
    }


    @Override
    public EnergyType getEnergyType() {
        return EnergyType.QUANTUM;
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
            cableType = QEType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
