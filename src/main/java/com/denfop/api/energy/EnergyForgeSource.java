package com.denfop.api.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Map;

public class EnergyForgeSource extends EnergyForge implements IEnergySource {
    private double perenergy;
    private double pastEnergy;
    private double tick;

    public EnergyForgeSource(BlockEntity blockEntity) {
        super(blockEntity);
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
