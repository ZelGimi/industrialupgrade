package com.denfop.componets.energy;

import com.denfop.api.energy.interfaces.EnergyAcceptor;
import com.denfop.api.energy.interfaces.EnergySource;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.componets.BufferEnergy;
import com.denfop.componets.Energy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnergyNetDelegateSource extends EnergyNetDelegate implements EnergySource {


    public double maxOutput = -1;
    int hashCodeSource;

    public EnergyNetDelegateSource(Energy block) {
        super(block);

    }

    public EnergyNetDelegateSource(BlockEntityBase block, Set<Direction> sourceDirection, BufferEnergy bufferEnergy) {
        super(block, sourceDirection, bufferEnergy);

    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    public List<InfoTile<EnergyTile>> getValidReceivers() {
        return validReceivers;
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

    @Override
    public @NotNull BlockPos getPos() {
        return this.worldPosition;
    }

    public int getSourceTier() {
        return buffer.sourceTier;
    }

    public boolean emitsEnergyTo(EnergyAcceptor receiver, Direction dir) {
        for (Direction facing1 : this.sourceDirections) {
            if (facing1.ordinal() == dir.ordinal()) {
                return true;
            }
        }
        return false;
    }


    public double canExtractEnergy() {

        return maxOutput == -1 ? !this.sendingSidabled ? this.getSourceEnergy() : 0.0D : maxOutput;
    }


    public void extractEnergy(double amount) {
        assert amount <= this.buffer.storage;

        this.buffer.storage = this.buffer.storage - amount;
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
        return true;
    }

}
