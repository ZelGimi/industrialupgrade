package com.denfop.items.energy.instruments;

import com.denfop.Config;

public enum EnumVarietyInstruments {
    NANO(2, Config.nano_transfer, Config.nano_maxEnergy, 30, 15, Config.ultraLowPower, 0, Config.nano_energyPerOperation,
            Config.nano_energyPerbigHolePowerOperation, Config.energyPerbigHolePowerOperation, 0
    ),
    QUANTUM(
            3,
            Config.quantum_transfer,
            Config.quantum_maxEnergy,
            40,
            20,
            Config.ultraLowPower,
            0,
            Config.quantum_energyPerOperation,
            Config.quantum_energyPerbigHolePowerOperation,
            Config.energyPerbigHolePowerOperation,
            0
    ),
    SPECTRAL(4, Config.spectral_transfer, Config.spectral_maxEnergy, 60, 30, Config.ultraLowPower, 0,
            Config.spectral_energyPerOperation,
            Config.spectral_energyPerbigHolePowerOperation, Config.energyPerbigHolePowerOperation, 0
    ),
    PERFECT_DRILL(5, Config.ultdrilltransferLimit, Config.ultdrillmaxCharge, Config.effPower, Config.bigHolePower,
            Config.ultraLowPower, Config.ultraLowPower1, Config.energyPerOperation, Config.energyPerLowOperation,
            Config.energyPerultraLowPowerOperation,
            Config.energyPerultraLowPowerOperation1
    );

    private final int tier;
    private final int energy_transfer;
    private final int capacity;
    private final int normal_power;
    private final int big_holes;
    private final int mega_holes;
    private final int ultra_power;
    private final int energyPerOperation;
    private final int energyPerBigOperation;
    private final int energyPerbigHolePowerOperation;
    private final int energyPerultraLowPowerOperation;

    EnumVarietyInstruments(
            int tier, int energy_transfer, int capacity, int normal_power, int big_holes, int mega_holes,
            int ultra_power, int energyPerOperation, int energyPerBigOperation,
            int energyPerbigHolePowerOperation, int energyPerultraLowPowerOperation
    ) {
        this.tier = tier;
        this.energy_transfer = energy_transfer;
        this.capacity = capacity;
        this.normal_power = normal_power;
        this.big_holes = big_holes;
        this.mega_holes = mega_holes;
        this.ultra_power = ultra_power;
        this.energyPerOperation = energyPerOperation;
        this.energyPerBigOperation = energyPerBigOperation;
        this.energyPerbigHolePowerOperation = energyPerbigHolePowerOperation;
        this.energyPerultraLowPowerOperation = energyPerultraLowPowerOperation;

    }

    public int getBig_holes() {
        return big_holes;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getEnergy_transfer() {
        return energy_transfer;
    }

    public int getEnergyPerbigHolePowerOperation() {
        return energyPerbigHolePowerOperation;
    }

    public int getEnergyPerBigOperation() {
        return energyPerBigOperation;
    }

    public int getEnergyPerOperation() {
        return energyPerOperation;
    }

    public int getEnergyPerultraLowPowerOperation() {
        return energyPerultraLowPowerOperation;
    }

    public int getMega_holes() {
        return mega_holes;
    }

    public int getNormal_power() {
        return normal_power;
    }

    public int getTier() {
        return tier;
    }

    public int getUltra_power() {
        return ultra_power;
    }
}
