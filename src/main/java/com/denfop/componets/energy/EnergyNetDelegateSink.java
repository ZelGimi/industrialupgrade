package com.denfop.componets.energy;

import com.denfop.api.energy.interfaces.EnergyEmitter;
import com.denfop.api.energy.interfaces.EnergySink;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.componets.Energy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EnergyNetDelegateSink extends EnergyNetDelegate implements EnergySink {

    int hashCodeSource;
    List<Integer> energyTicks = new LinkedList<>();


    public EnergyNetDelegateSink(Energy block) {
        super(block);
    }

    public int getSinkTier() {
        return buffer.sinkTier;
    }

    public boolean acceptsEnergyFrom(EnergyEmitter emitter, Direction dir) {
        for (Direction facing1 : this.sinkDirections) {
            if (facing1.ordinal() == dir.ordinal()) {
                return true;
            }
        }
        return false;
    }


    public List<InfoTile<EnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    @Override
    public void AddTile(final EnergyTile tile, final Direction dir) {
        super.AddTile(tile, dir);
    }

    @Override
    public void RemoveTile(final EnergyTile tile, final Direction dir) {
        super.RemoveTile(tile, dir);
    }

    @Override
    public Map<Direction, EnergyTile> getTiles() {
        return this.energyConductorMap;
    }

    public double getDemandedEnergy() {
        return !this.receivingDisabled ? this.buffer.capacity - this.buffer.storage : 0;
    }

    public void receiveEnergy(double amount) {
        this.buffer.storage += amount;
    }

    @Override
    public List<Integer> getEnergyTickList() {
        return energyTicks;
    }

    @Override
    public double getPerEnergy() {
        return this.perenergy;
    }

    @Override
    public double getPastEnergy() {
        return this.pastEnergy;
    }

    @Override
    public void setPastEnergy(final double pastEnergy) {
        this.pastEnergy = pastEnergy;
    }

    @Override
    public void addPerEnergy(final double setEnergy) {
        this.perenergy += setEnergy;
    }

    @Override
    public void addTick(final double tick) {
        this.tick = tick;
    }

    @Override
    public double getTick() {
        return this.tick;
    }

    @Override
    public boolean isSink() {
        return true;
    }

    @Override
    public @NotNull BlockPos getPos() {
        return this.worldPosition;
    }

}
