package com.denfop.componets.pressure;

import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.api.otherenergies.pressure.IPressureAcceptor;
import com.denfop.api.otherenergies.pressure.IPressureEmitter;
import com.denfop.api.otherenergies.pressure.IPressureTile;
import com.denfop.componets.BufferEnergy;
import com.denfop.componets.PressureComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EnergyNetDelegate implements IPressureTile {
    public final BufferEnergy buffer;
    public final BlockPos worldPosition;
    private final boolean clientSide;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public double tick;
    public Map<Direction, IPressureTile> energyConductorMap = new HashMap<>();
    protected double pastEnergy;
    protected double perenergy;
    List<InfoTile<IPressureTile>> validReceivers = new LinkedList<>();
    private long id;
    private int hashCodeSource;

    public EnergyNetDelegate(PressureComponent block) {
        this.worldPosition = block.getParent().pos;
        this.clientSide = block.getParent().getLevel().isClientSide;
        sinkDirections = block.sinkDirections;
        sourceDirections = block.sourceDirections;
        this.buffer = block.buffer;
    }

    public long getIdNetwork() {
        return this.id;
    }

    public boolean acceptsPressureFrom(IPressureEmitter emitter, Direction dir) {
        return this.sinkDirections.contains(dir);
    }

    public boolean emitsPressureTo(IPressureAcceptor receiver, Direction dir) {
        return this.sourceDirections.contains(dir);
    }

    @Override
    public @NotNull BlockPos getPos() {
        return worldPosition;
    }

    @Override
    public BlockEntity getTile() {
        return null;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Map<Direction, IPressureTile> getTiles() {
        return energyConductorMap;
    }

    public void RemoveTile(IPressureTile tile, final Direction facing1) {
        if (!clientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IPressureTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IPressureTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public List<InfoTile<IPressureTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(int hashCode) {
        hashCodeSource = hashCode;
    }

    public void AddTile(IPressureTile tile, final Direction facing1) {
        if (!clientSide) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));

        }
    }

    public Set<Direction> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<Direction> getSinkDirs() {
        return Collections.unmodifiableSet(this.sinkDirections);
    }

}

