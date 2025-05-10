package com.denfop.api.reactors;

import com.denfop.blocks.FluidName;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class LogicFluidReactor extends LogicReactor {

    private final IFluidReactor reactorFluid;
    public double temp_heat = 0;

    public LogicFluidReactor(final IFluidReactor advReactor) {
        super(advReactor);
        this.reactorFluid = advReactor;
    }

    @Override
    public void onTick() {

        if (!this.rodsList.isEmpty() && reactorFluid.getInputTank().getFluidAmount() > 0) {
            super.onTick();
            if (!this.reactorFluid.isFull()) {
                return;
            }
            if (temp_heat < getMaxHeat()) {
                temp_heat += rand.nextInt(Math.max((int) (getMaxHeat() - temp_heat), 4) / 4);
                if (temp_heat > getMaxHeat()) {
                    temp_heat = getMaxHeat();
                }
            }

            if (reactorFluid.getInputTank().getFluidAmount() >= 5 * this.reactorFluid.getPressure()) {
                reactorFluid.getInputTank().drain(5 * this.reactorFluid.getPressure(), IFluidHandler.FluidAction.EXECUTE);
                if (reactorFluid.getOutputTank().getFluidAmount() + 2 * this.reactorFluid.getPressure() <= reactorFluid
                        .getOutputTank()
                        .getCapacity()) {
                    reactorFluid.getOutputTank().fill(new FluidStack(
                            FluidName.fluidsteam.getInstance().get(),
                            2 * this.reactorFluid.getPressure()
                    ), IFluidHandler.FluidAction.EXECUTE);
                    if (temp_heat > this.getMaxHeat()) {
                        temp_heat -= rand.nextInt((int) Math.max(temp_heat - this.getMaxHeat(), 1));
                        if (temp_heat < 0) {
                            temp_heat = 0;
                        }
                    } else {
                        if (temp_heat > reactorFluid.getStableMaxHeat() && temp_heat > this.getMaxHeat()) {
                            temp_heat -= rand.nextInt((int) Math.max(((temp_heat - reactorFluid.getStableMaxHeat()) * 0.25), 1));
                            if (temp_heat < 0) {
                                temp_heat = 0;
                            }
                        }
                    }
                    reactorFluid.setOutput(generation * (1 + 0.02 * (this.reactorFluid.getPressure() - 1)));
                } else {
                    temp_heat += rand.nextInt(100);
                }
            } else {

                temp_heat += rand.nextInt(150);
            }
            if (this.reactorFluid.getCoolantTank().getFluidAmount() >= 5) {
                if (this.reactorFluid.getHotCoolantTank().getFluidAmount() + 2 <= this.reactorFluid
                        .getHotCoolantTank()
                        .getCapacity()) {
                    reactorFluid.getCoolantTank().drain(5, IFluidHandler.FluidAction.EXECUTE);
                    reactorFluid.getHotCoolantTank().fill(new FluidStack(
                            FluidName.fluidhot_coolant.getInstance().get(),
                            2
                    ), IFluidHandler.FluidAction.EXECUTE);
                    if ((int) (temp_heat * 0.2) > 0) {
                        temp_heat -= rand.nextInt((int) (temp_heat * 0.2));
                        if (temp_heat < 0) {
                            temp_heat = 0;
                        }
                    }
                    if (temp_heat < 0) {
                        temp_heat = 0;
                    }
                }
            }
        } else {
            if (this.getTemp_heat() > 0 && this.rodsList.isEmpty()) {
                temp_heat -= rand.nextInt((int) (this.getTemp_heat() + 1));
                if (temp_heat < 0) {
                    temp_heat = 0;
                }
                this.reactor.setOutput(0);
            } else if (!this.rodsList.isEmpty()) {
                temp_heat += rand.nextInt(200);
                this.reactor.setOutput(0);
            }
            this.reactor.setOutput(0);
        }
        reactorFluid.setHeat(temp_heat);
    }

    public double getTemp_heat() {
        return temp_heat;
    }

    public void setTemp_heat(final double temp_heat) {
        this.temp_heat = temp_heat;
    }

}
