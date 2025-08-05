package com.denfop.api.reactors;

import com.denfop.blocks.FluidName;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.HashMap;
import java.util.Map;

public class LogicHeatReactor extends LogicReactor {

    private final IHeatReactor heatReactor;
    public double temp_heat = 0;
    private HashMap<Integer, Double> mapGraphiteConsume;

    public LogicHeatReactor(final IHeatReactor advReactor) {
        super(advReactor);
        this.heatReactor = advReactor;
    }

    @Override
    public void calculateComponent() {
        super.calculateComponent();
        mapGraphiteConsume = new HashMap<>();
        for (int i = 0; i < this.x; i++) {
            double col = 0;
            for (LogicComponent component : this.rodsList) {
                if (component.getX() == i) {
                    col += (component.heat * 0.83) / 160;
                }
            }
            if (col != 0) {
                mapGraphiteConsume.put(i, col);
            }
        }
    }

    @Override
    public void onTick() {
        if (this.rodsList.isEmpty()) {

            if (this.temp_heat >= 1) {
                temp_heat -= rand.nextInt((int) this.temp_heat);
            }
            this.reactor.setOutput(0);
        } else {
            boolean can = true;
            for (Map.Entry<Integer, Double> entry : this.mapGraphiteConsume.entrySet()) {
                int[] lengthGraphite = this.heatReactor.getLengthGraphiteIndex(entry.getKey());
                final double col = entry.getValue();
                double col1 = col;

                for (int i : lengthGraphite) {
                    if (col1 <= 0) {
                        break;
                    }
                    if (heatReactor.getFuelGraphite(i) > col1) {
                        heatReactor.consumeFuelGraphite(i, col1);
                        final int level = this.heatReactor.getLevelGraphite(i);
                        col1 = 0;
                        if (this.heatReactor.getHeliumTank().getFluidAmount() >= col * level) {
                            this.heatReactor.addHeliumToRegenerate((col * level) / 2D);
                            this.heatReactor.getHeliumTank().drain((int) (col * level), IFluidHandler.FluidAction.EXECUTE);
                            temp_heat -= rand.nextInt(100);
                            if (temp_heat < 0) {
                                temp_heat = 0;
                            }
                        } else {
                            temp_heat += rand.nextInt(200);
                        }
                    } else if (!heatReactor.getGraphite(i).isEmpty()) {
                        heatReactor.consumeGraphite(i);
                        final double fuel = heatReactor.getFuelGraphite(i);
                        if (fuel < col1) {
                            col1 -= fuel;
                        } else {
                            heatReactor.consumeFuelGraphite(i, col1);
                            final int level = this.heatReactor.getLevelGraphite(i);
                            col1 = 0;
                            if (this.heatReactor.getHeliumTank().getFluidAmount() >= col * level) {
                                this.heatReactor.addHeliumToRegenerate((col * level) / 2D);
                                this.heatReactor.getHeliumTank().drain((int) (col * level), IFluidHandler.FluidAction.EXECUTE);
                                temp_heat -= rand.nextInt(100);
                                if (temp_heat < 0) {
                                    temp_heat = 0;
                                }
                            } else {
                                temp_heat += rand.nextInt(200);
                            }
                        }
                    } else {
                        temp_heat += rand.nextInt(100);
                    }
                }
                if (col1 > 0) {
                    temp_heat += rand.nextInt((int) (50 * col));
                    can = false;
                    break;
                }
            }
            if (temp_heat < 0) {
                temp_heat = 0;
            }
            int temp = (int) (this.generation * 1.05);
            if (can) {
                super.onTick();
                if (!this.heatReactor.isFull()) {
                    return;
                }
                if (temp_heat < 0) {
                    temp_heat = 0;
                }
                if (this.temp_heat >= 0 && this.temp_heat < this.getMaxHeat() && this.getMaxHeat() > 1) {

                    temp_heat += rand.nextInt((int) ((this.getMaxHeat() - this.temp_heat)));
                }
                for (int j = 0; j < this.heatReactor.getLengthSimplePump(); j++) {
                    double col = Math.floor(this.heatReactor.getHeliumToRegenerate());
                    col = Math.min(col, this.heatReactor
                            .getHeliumTank().getFluidAmount());
                    if (col < 1) {
                        break;
                    }
                    final int energy = this.heatReactor.getEnergySimplePump(j);
                    if (energy <= 0) {
                        continue;
                    }
                    if (temp >= energy) {
                        temp -= energy;
                    } else {
                        continue;
                    }
                    final int power = this.heatReactor.getPowerSimplePump(j);

                    if (power > col) {
                        this.heatReactor.addHeliumToRegenerate(-col);
                        this.heatReactor
                                .getHeliumTank().fill(
                                        new FluidStack(
                                                FluidName.fluidHelium.getInstance().get(),
                                                (int) col
                                        ),
                                        IFluidHandler.FluidAction.EXECUTE
                                );
                        this.temp_heat -= this.rand.nextInt(20 * power);
                        if (temp_heat < 0) {
                            temp_heat = 0;
                        }
                    } else {
                        this.heatReactor.addHeliumToRegenerate(-power);
                        this.heatReactor
                                .getHeliumTank().fill(
                                        new FluidStack(
                                                FluidName.fluidHelium.getInstance().get(),
                                                power
                                        ),
                                        IFluidHandler.FluidAction.EXECUTE
                                );
                        this.temp_heat -= this.rand.nextInt(10 * power);
                        if (temp_heat < 0) {
                            temp_heat = 0;
                        }
                    }
                }
                for (int j = 0; j < this.heatReactor.getLengthPump(); j++) {
                    final int energy = this.heatReactor.getEnergyPump(j);
                    if (energy <= 0) {
                        continue;
                    }
                    if (temp >= energy) {
                        temp -= energy;
                    } else {
                        continue;
                    }
                    final int power = this.heatReactor.getPowerPump(j);
                    if (this.heatReactor
                            .getWaterTank()
                            .getFluidAmount() > 15 * power) {
                        if (this.heatReactor
                                .getHydrogenTank()
                                .getFluidAmount() + 10 * power < this.heatReactor
                                .getHydrogenTank()
                                .getCapacity() && this.heatReactor
                                .getOxygenTank()
                                .getFluidAmount() + 5 * power < this.heatReactor
                                .getOxygenTank()
                                .getCapacity()) {
                            this.heatReactor
                                    .getWaterTank().drain(15 * power, IFluidHandler.FluidAction.EXECUTE);
                            this.heatReactor
                                    .getHydrogenTank().fill(
                                            new FluidStack(
                                                    FluidName.fluidhyd.getInstance().get(),
                                                    10 * power
                                            ),
                                            IFluidHandler.FluidAction.EXECUTE
                                    );
                            this.heatReactor
                                    .getOxygenTank().fill(new FluidStack(
                                            FluidName.fluidoxy.getInstance().get(),
                                            5 * power
                                    ), IFluidHandler.FluidAction.EXECUTE);
                            this.heatReactor.damagePump(j);
                            this.temp_heat -= rand.nextInt(power * 20);
                            if (temp_heat < 0) {
                                temp_heat = 0;
                            }
                        }
                    }
                }

                heatReactor.setOutput(temp);
            } else {
                temp_heat += rand.nextInt(200);
                this.reactor.setOutput(0);
            }


        }
        heatReactor.setHeat(temp_heat);
    }

    public double getTemp_heat() {
        return temp_heat;
    }

    public void setTemp_heat(final double temp_heat) {
        this.temp_heat = temp_heat;
    }

}
