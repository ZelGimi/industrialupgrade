package com.denfop.componets;

import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergyEmitter;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.IMultiDual;
import com.denfop.api.sytem.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnergyNetDelegateMultiDual extends EnergyNetDelegate implements IMultiDual {


    int hashCodeSource;

    List<Integer> energyTicks = new ArrayList<>();


    EnergyNetDelegateMultiDual(Energy block) {
        super(block);
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction dir) {
        for (Direction facing1 : this.sinkDirections) {
            if (facing1.ordinal() == dir.ordinal()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }


    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public void AddTile(final IEnergyTile tile, final Direction dir) {
        super.AddTile(tile, dir);
    }

    @Override
    public void RemoveTile(final IEnergyTile tile, final Direction dir) {
        super.RemoveTile(tile, dir);
    }

    @Override
    public Map<Direction, IEnergyTile> getTiles() {
        return this.energyConductorMap;
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction dir) {
        for (Direction facing1 : this.sourceDirections) {
            if (facing1.ordinal() == dir.ordinal()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull BlockPos getPos() {
        return this.worldPosition;
    }

    public double getDemandedEnergy() {
        return !this.receivingDisabled && this.buffer.storage < buffer.capacity
                ? buffer.capacity - this.buffer.storage
                : 0.0D;
    }

    public double canExtractEnergy() {

        return !this.sendingSidabled
                ? this.getSourceEnergy()
                : 0.0D;
    }

    public int getSinkTier() {
        return buffer.sinkTier;
    }

    public int getSourceTier() {
        return buffer.sourceTier;
    }

    public void receiveEnergy(double amount) {
        this.buffer.storage = this.buffer.storage + amount;
    }

    public void extractEnergy(double amount) {
        assert amount <= this.buffer.storage;

        this.buffer.storage = this.buffer.storage - amount;
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
    public boolean isSource() {
        return !this.sendingSidabled;
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
        return !this.receivingDisabled;
    }


    @Override
    public double getPerEnergy1() {
        return this.perenergy1;
    }

    @Override
    public double getPastEnergy1() {
        return this.pastEnergy1;
    }

    @Override
    public void setPastEnergy1(final double pastEnergy) {
        this.pastEnergy1 = pastEnergy;
    }

    @Override
    public void addPerEnergy1(final double setEnergy) {
        this.perenergy1 += setEnergy;
    }


}
