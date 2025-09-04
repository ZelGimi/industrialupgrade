package com.denfop.componets;

import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.blockentity.base.BlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.*;

public abstract class EnergyNetDelegate implements EnergyTile {
    public final BufferEnergy buffer;
    public final BlockPos worldPosition;
    private final boolean clientSide;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public double tick;
    public boolean limit;
    public double limit_amount = 0;
    protected double pastEnergy;
    protected double perenergy;
    protected double pastEnergy1;
    protected double perenergy1;
    Map<Direction, EnergyTile> energyConductorMap = new HashMap<>();
    List<InfoTile<EnergyTile>> validReceivers = new LinkedList<>();
    private long id;

    public EnergyNetDelegate(Energy block) {
        this.worldPosition = block.parent.pos;
        this.clientSide = block.parent.getLevel().isClientSide;
        sinkDirections = block.sinkDirections;
        sourceDirections = block.sourceDirections;
        this.buffer = block.buffer;
    }

    public EnergyNetDelegate(BlockEntityBase block, Set<Direction> sourceDirection, BufferEnergy bufferEnergy) {
        this.worldPosition = block.pos;
        this.clientSide = block.getLevel().isClientSide;
        sinkDirections = new HashSet<>();
        sourceDirections = sourceDirection;
        this.buffer = bufferEnergy;
    }

    public double getSourceEnergy() {
        if (!limit) {
            return Math.min(this.buffer.storage, EnergyNetGlobal.instance.getPowerFromTier(buffer.sourceTier));
        } else {

            return Math.min(this.buffer.storage, this.limit_amount);
        }
    }

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

    public Map<Direction, EnergyTile> getConductors() {
        return energyConductorMap;
    }

    public void RemoveTile(EnergyTile tile, final Direction facing1) {
        if (!clientSide) {
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
    }

    public List<InfoTile<EnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public void AddTile(EnergyTile tile, final Direction facing1) {
        if (!this.clientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
        }
    }

}
