package com.powerutils;

import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergySource;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.sytem.InfoTile;
import com.denfop.componets.Energy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SourceEnergy implements IEnergySource {

    private final IEnergyStorage energy;
    private final TileEntity parent;
    private double perenergy;
    private double pastEnergy;
    private long id;

    public SourceEnergy(TileEntity parent, IEnergyStorage energy) {
        this.parent = parent;
        this.energy = energy;
    }
    public Map<EnumFacing, IEnergyTile> getConductors() {
        return energyConductorMap;
    }
    int hashCodeSource;
    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    private int hashCode;
    boolean hasHashCode = false;

    @Override
    public int hashCode() {
        if (!hasHashCode) {
            hasHashCode = true;
            this.hashCode = this.parent.hashCode();
            return hashCode;
        } else {
            return hashCode;
        }
    }
    @Override
    public @NotNull BlockPos getBlockPos() {
        return this.parent.getPos();
    }

    public int getSourceTier() {
        return 14;
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing dir) {
        return true;
    }
    public long getIdNetwork() {
        return id;
    }


    public void setId(final long id) {
        this.id = id;
    }

    public double canExtractEnergy() {

        return energy.extractEnergy(Integer.MAX_VALUE,true) / 4;
    }


    public void extractEnergy(double amount) {
        energy.extractEnergy((int) (amount * 4),false);
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

    @Override
    public TileEntity getTileEntity() {
        return parent;
    }
    Map<EnumFacing, IEnergyTile> energyConductorMap = new HashMap<>();

    public void RemoveTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.parent.getWorld().isRemote) {
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

    @Override
    public Map<EnumFacing, IEnergyTile> getTiles() {
        return energyConductorMap;
    }

    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();


    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public void AddTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.parent.getWorld().isRemote) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));

        }
    }
}
