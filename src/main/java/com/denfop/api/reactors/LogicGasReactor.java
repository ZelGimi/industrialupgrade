package com.denfop.api.reactors;

import com.denfop.blocks.FluidName;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class LogicGasReactor extends LogicReactor {

    private final IGasReactor gasReactor;
    public double temp_heat = 0;

    public LogicGasReactor(final IGasReactor advReactor) {
        super(advReactor);
        this.gasReactor = advReactor;
    }

    public void setTemp_heat(final double temp_heat) {
        this.temp_heat = temp_heat;
    }

    public double getTemp_heat() {
        return temp_heat;
    }

    @Override
    public void onTick() {
        final double log = (Math.log(this.gasReactor.getTemperatureRefrigerator()) / Math.log(4));
        double temp = this.generation * 1.1;
        if (this.rodsList.isEmpty()) {

            if (this.temp_heat >= 1) {
                temp_heat -= rand.nextInt((int) this.temp_heat);
            }
        } else if (log > 0 && this.gasReactor.getHeliumTank().getFluidAmount() > 5 * log) {
            super.onTick();
            this.gasReactor.getHeliumTank().drain((int) (5 * log), true);
            if (temp_heat < getMaxHeat()) {
                temp_heat += rand.nextInt(Math.max((int) (getMaxHeat() - temp_heat), 4));
                if (temp_heat > getMaxHeat()) {
                    temp_heat = getMaxHeat();
                }
            }
            if (rand.nextInt(100) >= 20) {
                this.gasReactor.addHeliumToRegenerate((int) (5 * log));
            } else {
                this.gasReactor.addHeliumToRegenerate((int) (4 * log));
            }
            for (int j = 0; j < this.gasReactor.getLengthCompressors(); j++) {
                final int energy = this.gasReactor.getEnergyCompressor(j);
                if (temp >= energy) {
                    temp -= energy;
                } else {
                    continue;
                }
                int col = this.gasReactor.getHeliumToRegenerate();
                if (col > 0 && this.gasReactor
                        .getHeliumTank()
                        .getFluidAmount() + col * this.gasReactor.getPressure(j) <= this.gasReactor.getCapacityHelium()) {
                    this.gasReactor.getHeliumTank().fill(new FluidStack(
                            FluidName.fluidHelium.getInstance(),
                            col * this.gasReactor.getPressure(j)
                    ), true);
                    this.gasReactor.addHeliumToRegenerate(-col * this.gasReactor.getPressure(j));
                }
            }
            if (this.gasReactor.hasPump()) {
                for (int j = 0; j < this.gasReactor.getLengthPump(); j++) {
                    final int energy = this.gasReactor.getEnergyPump(j);
                    if (energy <= 0) {
                        continue;
                    }
                    if (temp >= energy) {
                        temp -= energy;
                    } else {
                        continue;
                    }
                    final int power = this.gasReactor.getPowerPump(j);
                    if (this.gasReactor
                            .getHydrogenTank(j)
                            .getFluidAmount() > 2 * power && this.gasReactor
                            .getOxygenTank(j)
                            .getFluidAmount() > power) {
                        if (this.gasReactor
                                .getWaterTank(j)
                                .getFluidAmount() + 2 * power < this.gasReactor
                                .getWaterTank(j)
                                .getCapacity()) {
                            this.gasReactor.getWaterTank(j).fill(new FluidStack(
                                    FluidRegistry.WATER,
                                    2 * power
                            ), true);
                            this.gasReactor
                                    .getHydrogenTank(j).drain(2 * power, true);
                            this.gasReactor
                                    .getOxygenTank(j).drain(power, true);
                            this.gasReactor.damagePump(j);
                        }
                    }
                }
                if (this.gasReactor.hasFan()) {
                    for (int j = 0; j < this.gasReactor.getLengthFan(); j++) {
                        final int energy = this.gasReactor.getEnergyFan(j);
                        if (energy <= 0) {
                            continue;
                        }
                        if (temp >= energy) {
                            temp -= energy;
                        } else {
                            continue;
                        }
                        int power = this.gasReactor.getPowerFan(j);
                        int col = 0;
                        for (int jj = 0; jj < this.gasReactor.getLengthPump() && col < power; jj++) {

                            if (this.gasReactor.getWaterTank(jj).getFluidAmount() > 0) {
                                int temp1 = Math.min(this.gasReactor.getWaterTank(jj).getFluidAmount(), power - col);
                                this.gasReactor.getWaterTank(jj).drain(temp1, true);
                                col += temp1;
                            }
                        }
                        if (col > 0) {
                            temp_heat -= rand.nextInt(30 * col);
                            temp_heat = Math.max(1, temp_heat);
                            this.gasReactor.damageFan(j);
                        }

                    }
                }
            }
        } else {
            if (!this.rodsList.isEmpty()) {
                temp_heat += rand.nextInt(200);
            }
        }

        this.gasReactor.setHeat(temp_heat);
        if (temp < 0) {
            temp = 0;
        }
        this.gasReactor.setOutput(temp);
    }

}
