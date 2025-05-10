package com.denfop.items.energy.instruments;

public enum EnumVarietyInstruments {
    CHAINSAW(1, 100, 30000, 12, 5, 0, 0, 10,
            0, 0, 0
    ),
    SIMPLE(1, 100, 30000, 8, 5, 0, 0, 6,
            0, 0, 0
    ),
    DIAMOND(1, 100, 30000, 16, 8, 0, 0, 20,
            30, 0, 0
    ),
    VAJRA(4, 15000, 50000000, 5000, 4000, 3000, 2500,
            160,
            (int) (80 * 1.25),
            (int) (500 * 1.25),
            (int) (700 * 1.25)
    ),
    NANO(2, 1000, 1000000, 30, 15, 60, 0, 400,
            550, 300, 0
    ),
    QUANTUM(
            3,
            2500,
            10000000,
            40,
            20,
            60,
            0,
            350,
            450,
            300,
            0
    ),
    SPECTRAL(4, 5000, 50000000, 60, 30, 60, 0,
            250,
            370, 300, 0
    ),
    PERFECT_DRILL(5, 15000, 15000000, 80, 60,
            60, 60, 160, 80,
            500,
            700
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
