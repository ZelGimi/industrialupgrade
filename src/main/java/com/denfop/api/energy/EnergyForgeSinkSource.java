package com.denfop.api.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EnergyForgeSinkSource extends EnergyForge implements IDual {
    List<Integer> energyTicks = new LinkedList<>();
    private double perenergy;
    private double pastEnergy;
    private double tick;
    private double perenergy1;
    private double pastEnergy1;

    public EnergyForgeSinkSource(BlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter var1, Direction var2) {
        IEnergyStorage energyStorage = storages.get(var2.getOpposite());
        boolean can = energyStorage != null && energyStorage.canReceive();
        return can;
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
            return 0;
        return energyStorage.receiveEnergy(Integer.MAX_VALUE, true) / 4D;
    }

    @Override
    public int getSinkTier(Direction direction) {
        IEnergyStorage energyStorage = storages.get(direction);
        return EnergyNetGlobal.instance.getTierFromPower(energyStorage.receiveEnergy(Integer.MAX_VALUE, true) / 4D);
    }

    @Override
    public int getSinkTier() {
        return 1;
    }

    @Override
    public void receiveEnergy(Direction direction, double var2) {
        IEnergyStorage energyStorage = storages.get(direction);
        energyStorage.receiveEnergy((int) (var2 * 4), false);
    }

    @Override
    public void receiveEnergy(double var2) {

    }

    @Override
    public List<Integer> getEnergyTickList() {
        return energyTicks;
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

    @Override
    public boolean isSource() {
        return true;
    }

    @Override
    public double canExtractEnergy(Direction direction) {
        IEnergyStorage energy = storages.get(direction);
        if (energy == null)
            return 0;
        return energy.extractEnergy(Integer.MAX_VALUE, true) / 4D;
    }

    @Override
    public double canExtractEnergy() {
        int amount = 0;
        for (Map.Entry<Direction, IEnergyStorage> storage : storages.entrySet()) {
            if (storage.getValue() != null)
                if (storage.getValue().canExtract())
                    amount += storage.getValue().extractEnergy(Integer.MAX_VALUE, true) / 4;
        }
        return amount;
    }

    @Override
    public int getSourceTier(Direction direction) {
        IEnergyStorage energyStorage = storages.get(direction);
        return EnergyNetGlobal.instance.getTierFromPower(energyStorage.extractEnergy(Integer.MAX_VALUE, true) / 4D);

    }

    @Override
    public void extractEnergy(Direction direction, double var1) {
        storages.get(direction).extractEnergy((int) (var1 * 4), false);
    }

    @Override
    public void extractEnergy(double var1) {

    }

    @Override
    public int getSourceTier() {
        return 1;
    }


    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor var1, Direction var2) {
        IEnergyStorage energyStorage = storages.get(var2);
        return energyStorage != null && energyStorage.canExtract();
    }
}
