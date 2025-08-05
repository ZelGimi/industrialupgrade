package com.denfop.componets;

import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergySource;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.sytem.InfoTile;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnergyNetDelegateSource extends EnergyNetDelegate implements IEnergySource {


    int hashCodeSource;
    public   double maxOutput = -1;

    public EnergyNetDelegateSource(Energy block) {
        super(block);

    }
    public EnergyNetDelegateSource(TileEntityBlock block, Set<Direction> sourceDirection, BufferEnergy bufferEnergy) {
        super(block,sourceDirection,bufferEnergy);

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

    @Override
    public @NotNull BlockPos getPos() {
        return this.worldPosition;
    }

    public int getSourceTier() {
        return buffer.sourceTier;
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction dir) {
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
