package com.denfop.componets.heat;

import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.heat.IHeatAcceptor;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatTile;
import com.denfop.api.sytem.InfoTile;
import com.denfop.componets.BufferEnergy;
import com.denfop.componets.HeatComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public class EnergyNetDelegate  implements IHeatTile {
    public final BufferEnergy buffer;
    public final BlockPos worldPosition;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
    private final boolean clientSide;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public double tick;
    public Map<Direction, IHeatTile> energyConductorMap = new HashMap<>();
    List<InfoTile<IHeatTile>> validReceivers = new LinkedList<>();
    protected double pastEnergy;
    protected double perenergy;
    private long id;
    public EnergyNetDelegate(HeatComponent block) {
        this.worldPosition = block.getParent().pos;
        this.clientSide = block.getParent().getLevel().isClientSide;
        sinkDirections = block.sinkDirections;
        sourceDirections = block.sourceDirections;
        this.buffer = block.buffer;
    }
    @Override
    public BlockPos getPos() {
        return worldPosition;
    }

    @Override
    public BlockEntity getTile() {
        return null;
    }

    public boolean acceptsHeatFrom(IHeatEmitter var1, Direction var2) {
        for (Direction facing1 : this.sinkDirections) {
            if (facing1.ordinal() == var2.ordinal()) {
                return true;
            }
        }
        return false;
    }
    public boolean emitHeatTo(IHeatAcceptor receiver, Direction dir) {
        return this.sourceDirections.contains(dir);
    }
    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public void AddHeatTile(IHeatTile tile, Direction facing1) {
        if (!this.clientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
        }
    }

    @Override
    public void RemoveHeatTile(IHeatTile tile, Direction facing1) {
        if (!clientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IHeatTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IHeatTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }
    @Override
    public Map<Direction, IHeatTile> getHeatTiles() {
        return energyConductorMap;
    }



    public void setSinkDirections(Set<Direction> sinkDirections) {
        this.sinkDirections = sinkDirections;
    }

    public void setSourceDirections(Set<Direction> sourceDirections) {
        this.sourceDirections = sourceDirections;
    }

    public int hashCodeSource;
    public double getSourceEnergy() {
        return this.buffer.storage;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }
    public List<InfoTile<IHeatTile>> getHeatValidReceivers() {
        return validReceivers;
    }
}
