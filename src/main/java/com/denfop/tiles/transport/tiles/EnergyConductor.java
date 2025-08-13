package com.denfop.tiles.transport.tiles;

import com.denfop.api.energy.*;
import com.denfop.api.sytem.InfoTile;
import com.denfop.invslot.CableItem;
import com.denfop.tiles.transport.types.CableType;
import com.denfop.tiles.transport.types.ICableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.*;

public class EnergyConductor implements IEnergyConductor {
    public final ConductorInfo conductor;
    public int hashCodeSource;
    public boolean needUpdate;
    public InfoCable cable;
    public long id;
    BlockPos pos;
    public List<Direction> blackList = new LinkedList<>();

    protected ICableItem cableType;

    public EnergyConductor(TileEntityMultiCable tileEntityCable) {
        this.cableType =  tileEntityCable.getCableItem();
        this.pos = tileEntityCable.getPos();
        this.conductor = new ConductorInfo(tileEntityCable.pos, this);

    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    public Map<Direction, IEnergyTile> energyConductorMap = new HashMap<>();
    public List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();

    public List<Direction> getBlackList() {
        return blackList;
    }

    public Map<Direction, IEnergyTile> getTiles() {
        return energyConductorMap;
    }

    @Override
    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction direction) {
        return !getBlackList().contains(direction);
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction direction) {
        return !getBlackList().contains(direction);
    }

    @Override
    public void RemoveTile(IEnergyTile tile, final Direction facing1) {
        this.energyConductorMap.remove(facing1);
        final Iterator<InfoTile<IEnergyTile>> iter = validReceivers.iterator();
        while (iter.hasNext()) {
            InfoTile<IEnergyTile> tileInfoTile = iter.next();
            if (tileInfoTile.tileEntity == tile) {
                iter.remove();
                break;
            }
        }
    }

    @Override
    public void AddTile(IEnergyTile tile, final Direction facing1) {
        if (!this.energyConductorMap.containsKey(facing1)) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
        }
    }

    public double getConductionLoss() {
        return this.cableType.getLoss();
    }

    public double getConductorBreakdownEnergy() {
        return this.cableType.getCapacity() + 1;
    }

    @Override
    public void removeConductor() {

    }

    @Override
    public InfoCable getCable() {
        return cable;
    }

    @Override
    public void setCable(InfoCable cable) {
        this.cable = cable;
    }

    @Override
    public ConductorInfo getInfo() {
        return conductor;
    }

    public long getIdNetwork() {
        return this.id;
    }

    @Override
    public int getHashCodeSource() {
        return this.hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        this.hashCodeSource = hashCode;
    }

    public void setId(final long id) {
        this.id = id;
    }
}
