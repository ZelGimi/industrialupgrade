package com.denfop.componets.energy;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.sytem.InfoTile;
import com.denfop.componets.BufferEnergy;
import com.denfop.componets.Energy;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public abstract class EnergyNetDelegate  implements IEnergyTile {
    public final BufferEnergy buffer;
    public final BlockPos worldPosition;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
    private final boolean clientSide;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public Map<Direction, IEnergyTile> energyConductorMap = new HashMap<>();
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
    public double tick;
    protected double pastEnergy;
    protected double perenergy;
    protected double pastEnergy1;
    protected double perenergy1;
    public boolean limit;
    public double limit_amount = 0;

    public EnergyNetDelegate(Energy block) {
        this.worldPosition = block.getParent().pos;
        this.clientSide = block.getParent().getLevel().isClientSide;
        sinkDirections = block.sinkDirections;
        sourceDirections = block.sourceDirections;
        this.buffer = block.buffer;
    }

    public EnergyNetDelegate(TileEntityBlock block, Set<Direction> sourceDirection, BufferEnergy bufferEnergy) {
        this.worldPosition = block.pos;
        this.clientSide = block.getLevel().isClientSide;
        sinkDirections = new HashSet<>();
        sourceDirections = sourceDirection;
        this.buffer =bufferEnergy;
    }

    public double getSourceEnergy() {
        if (!limit) {
            return Math.min(this.buffer.storage, EnergyNetGlobal.instance.getPowerFromTier(buffer.sourceTier));
        } else {

            return Math.min(this.buffer.storage, this.limit_amount);
        }
    }

    private long id;

    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setSinkDirections(Set<Direction> sinkDirections) {
        this.sinkDirections = sinkDirections;
    }

    public void setSourceDirections(Set<Direction> sourceDirections) {
        this.sourceDirections = sourceDirections;
    }

    public Map<Direction, IEnergyTile> getConductors() {
        return energyConductorMap;
    }

    public void RemoveTile(IEnergyTile tile, final Direction facing1) {
        if (!clientSide) {
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
    }

    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public void AddTile(IEnergyTile tile, final Direction facing1) {
        if (!this.clientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
        }
    }

}
