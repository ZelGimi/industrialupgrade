package com.denfop.componets.cold;

import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.api.otherenergies.cool.ICoolAcceptor;
import com.denfop.api.otherenergies.cool.ICoolEmitter;
import com.denfop.api.otherenergies.cool.ICoolTile;
import com.denfop.componets.BufferEnergy;
import com.denfop.componets.CoolComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public class EnergyNetDelegate implements ICoolTile {
    public final BufferEnergy buffer;
    public final BlockPos worldPosition;
    private final boolean clientSide;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public double tick;
    public Map<Direction, ICoolTile> energyConductorMap = new HashMap<>();
    public int hashCodeSource;
    protected double pastEnergy;
    protected double perenergy;
    List<InfoTile<ICoolTile>> validReceivers = new LinkedList<>();
    private long id;

    public EnergyNetDelegate(CoolComponent block) {
        this.worldPosition = block.getParent().pos;
        this.clientSide = block.getParent().getLevel().isClientSide;
        sinkDirections = block.sinkDirections;
        sourceDirections = block.sourceDirections;
        this.buffer = block.buffer;

    }

    public double getSourceEnergy() {
        return this.buffer.storage;
    }

    @Override
    public BlockPos getPos() {
        return worldPosition;
    }

    @Override
    public BlockEntity getTile() {
        return null;
    }

    public boolean acceptsCoolFrom(ICoolEmitter var1, Direction var2) {
        for (Direction facing1 : this.sinkDirections) {
            if (facing1.ordinal() == var2.ordinal()) {
                return true;
            }
        }
        return false;
    }

    public boolean emitsCoolTo(ICoolAcceptor receiver, Direction dir) {
        return this.sourceDirections.contains(dir);
    }

    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public void AddCoolTile(ICoolTile tile, Direction facing1) {
        if (!this.clientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
        }
    }

    @Override
    public void RemoveCoolTile(ICoolTile tile, Direction facing1) {
        if (!clientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<ICoolTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<ICoolTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    @Override
    public Map<Direction, ICoolTile> getCoolTiles() {
        return energyConductorMap;
    }


    public void setSinkDirections(Set<Direction> sinkDirections) {
        this.sinkDirections = sinkDirections;
    }

    public void setSourceDirections(Set<Direction> sourceDirections) {
        this.sourceDirections = sourceDirections;
    }


    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    public List<InfoTile<ICoolTile>> getCoolValidReceivers() {
        return validReceivers;
    }


}
