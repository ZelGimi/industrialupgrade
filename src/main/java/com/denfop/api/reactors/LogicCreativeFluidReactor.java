package com.denfop.api.reactors;

public class LogicCreativeFluidReactor extends LogicCreativeReactor {


    public double temp_heat = 0;

    public LogicCreativeFluidReactor(final CreativeReactor advReactor) {
        super(advReactor);

    }

    @Override
    public void onTick() {

        if (!this.rodsList.isEmpty()) {
            super.onTick();

            if (temp_heat < getMaxHeat()) {
                temp_heat += rand.nextInt(Math.max((int) (getMaxHeat() - temp_heat), 4) / 4);
                if (temp_heat > getMaxHeat()) {
                    temp_heat = getMaxHeat();
                }
            }

            if (temp_heat > this.getMaxHeat()) {
                temp_heat -= rand.nextInt((int) Math.max(temp_heat - this.getMaxHeat(), 1));
                if (temp_heat < 0) {
                    temp_heat = 0;
                }
            } else {
                if (temp_heat > reactor.getStableMaxHeat() && temp_heat > this.getMaxHeat()) {
                    temp_heat -= rand.nextInt((int) Math.max(((temp_heat - reactor.getStableMaxHeat()) * 0.25), 1));
                    if (temp_heat < 0) {
                        temp_heat = 0;
                    }
                }
            }
            reactor.setOutput(generation * (1 + 0.02 * (5 - 1)));
            if ((int) (temp_heat * 0.2) > 0) {
                temp_heat -= rand.nextInt((int) (temp_heat * 0.2));
                if (temp_heat < 0) {
                    temp_heat = 0;
                }
            }
            if (temp_heat < 0) {
                temp_heat = 0;
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
        reactor.setHeat(temp_heat);
    }

    public double getTemp_heat() {
        return temp_heat;
    }

    public void setTemp_heat(final double temp_heat) {
        this.temp_heat = temp_heat;
    }

}
