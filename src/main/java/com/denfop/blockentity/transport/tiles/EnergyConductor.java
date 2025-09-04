package com.denfop.blockentity.transport.tiles;

import com.denfop.api.energy.InfoCable;
import com.denfop.api.energy.interfaces.EnergyAcceptor;
import com.denfop.api.energy.interfaces.EnergyEmitter;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.energy.networking.ConductorInfo;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.blockentity.transport.types.ICableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.*;

public class EnergyConductor implements com.denfop.api.energy.interfaces.EnergyConductor {
    public final ConductorInfo conductor;
    public int hashCodeSource;
    public boolean needUpdate;
    public InfoCable cable;
    public long id;
    public List<Direction> blackList = new LinkedList<>();
    public Map<Direction, EnergyTile> energyConductorMap = new HashMap<>();
    public List<InfoTile<EnergyTile>> validReceivers = new LinkedList<>();
    protected ICableItem cableType;
    BlockPos pos;

    public EnergyConductor(BlockEntityMultiCable tileEntityCable) {
        this.cableType = tileEntityCable.getCableItem();
        this.pos = tileEntityCable.getPos();
        this.conductor = new ConductorInfo(tileEntityCable.pos, this);

    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    public List<Direction> getBlackList() {
        return blackList;
    }

    public Map<Direction, EnergyTile> getTiles() {
        return energyConductorMap;
    }

    @Override
    public List<InfoTile<EnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public boolean acceptsEnergyFrom(EnergyEmitter emitter, Direction direction) {
        return !getBlackList().contains(direction);
    }

    public boolean emitsEnergyTo(EnergyAcceptor receiver, Direction direction) {
        return !getBlackList().contains(direction);
    }

    @Override
    public void RemoveTile(EnergyTile tile, final Direction facing1) {
        this.energyConductorMap.remove(facing1);
        final Iterator<InfoTile<EnergyTile>> iter = validReceivers.iterator();
        while (iter.hasNext()) {
            InfoTile<EnergyTile> tileInfoTile = iter.next();
            if (tileInfoTile.tileEntity == tile) {
                iter.remove();
                break;
            }
        }
    }

    @Override
    public void AddTile(EnergyTile tile, final Direction facing1) {
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
