package com.powerutils;

import com.denfop.api.energy.EnergyTick;
import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergyEmitter;
import com.denfop.api.energy.IEnergySink;
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

public class SinkEnergy implements IEnergySink {

    private final TileEntity parent;
    private final IEnergyStorage energy;
    private double perenergy;
    private double pastEnergy;
    private double tick;
    private long id;

    public SinkEnergy(TileEntity tileentity, IEnergyStorage energy) {
        this.parent = tileentity;
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
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing dir) {

        return true;
    }
    public double getDemandedEnergy() {
        return energy.receiveEnergy(Integer.MAX_VALUE,true) / 4D;
    }

    @Override
    public int getSinkTier() {
        return 14;
    }

    public void receiveEnergy(double amount) {
        energy.receiveEnergy((int) amount * 4,false);
    }

    List<Integer> energyTicks = new LinkedList<>();

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

  
    public long getIdNetwork() {
        return id;
    }


    public void setId(final long id) {
        this.id = id;
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
