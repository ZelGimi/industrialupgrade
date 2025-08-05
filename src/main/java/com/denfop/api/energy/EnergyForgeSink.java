package com.denfop.api.energy;

import com.denfop.componets.Energy;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EnergyForgeSink   extends EnergyForge implements IEnergySink{
    private double perenergy;
    private double pastEnergy;
    private double tick;

    public EnergyForgeSink(BlockEntity blockEntity){
        super(blockEntity);
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter var1, Direction var2) {
        IEnergyStorage energyStorage = storages.get(var2);
        return energyStorage != null && energyStorage.canReceive();
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
    public double getDemandedEnergy() {
        return 0;
    }

    @Override
    public double getDemandedEnergy(Direction direction) {
        IEnergyStorage energyStorage = storages.get(direction);
        if (energyStorage == null)
            return  0;
        return energyStorage.receiveEnergy(Integer.MAX_VALUE,true) / 4D;
    }
    @Override
    public int getSinkTier(Direction direction) {
        IEnergyStorage energyStorage = storages.get(direction);
        return EnergyNetGlobal.instance.getTierFromPower(energyStorage.receiveEnergy(Integer.MAX_VALUE,true) / 4D);
    }
    @Override
    public int getSinkTier() {
        return 1;
    }
    @Override
    public void receiveEnergy(Direction direction,double var2) {
        IEnergyStorage energyStorage = storages.get(direction);
        energyStorage.receiveEnergy((int) (var2 * 4),false);
    }
    @Override
    public void receiveEnergy(double var2) {

    }
    List<Integer> energyTicks = new LinkedList<>();
    @Override
    public List<Integer> getEnergyTickList() {
        return energyTicks;
    }
}
