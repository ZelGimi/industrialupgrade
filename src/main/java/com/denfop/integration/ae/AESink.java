package com.denfop.integration.ae;

import appeng.api.config.Actionable;
import appeng.tile.powersink.AEBasePoweredTile;
import com.denfop.api.energy.IEnergyEmitter;
import com.denfop.api.energy.IEnergySink;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.sytem.InfoTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AESink implements IEnergySink {

    private final AEBasePoweredTile entity;
    List<Integer> energyTicks = new ArrayList<>();
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
    Map<EnumFacing, IEnergyTile> energyConductorMap = new HashMap<>();
    boolean hasHashCode = false;
    int hashCodeSource;
    private double pastEnergy;
    private double perEnergy;
    private double tick;
    private int hashCode;
    private long id;

    public AESink(AEBasePoweredTile entity) {
        this.entity = entity;
    }

    public Map<EnumFacing, IEnergyTile> getTiles() {
        return energyConductorMap;
    }

    @Override
    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    @Override
    public void RemoveTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!entity.getWorld().isRemote) {
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
    public void AddTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!entity.getWorld().isRemote) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
        }
    }

    @Override
    public int hashCode() {
        if (!hasHashCode) {
            hasHashCode = true;
            this.hashCode = super.hashCode();
            return hashCode;
        } else {
            return hashCode;
        }
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
    public boolean acceptsEnergyFrom(final IEnergyEmitter var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public double getPerEnergy() {
        return this.perEnergy;
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
        this.perEnergy += setEnergy;
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
    public double getDemandedEnergy() {
        return (entity.getAEMaxPower() - entity.getAECurrentPower()) / 2D;
    }

    @Override
    public int getSinkTier() {
        return 14;
    }

    @Override
    public void receiveEnergy(final double var2) {
        entity.injectAEPower(var2 * 2D, Actionable.MODULATE);
    }

    @Override
    public List<Integer> getEnergyTickList() {
        return energyTicks;
    }

    @Override
    public TileEntity getTileEntity() {
        return entity;
    }

    @Override
    public BlockPos getBlockPos() {
        return entity.getPos();
    }

    @Override
    public long getIdNetwork() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

}
