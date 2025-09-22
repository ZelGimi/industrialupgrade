package com.denfop.api.reactors;

public class LogicCreativeGasReactor extends LogicCreativeReactor {


    public double temp_heat = 0;
    int[] fan = new int[]{2, 2, 3, 4};
    int[] power_fan = new int[]{1, 2, 4, 6};

    public LogicCreativeGasReactor(final CreativeReactor advReactor) {
        super(advReactor);
    }

    public double getTemp_heat() {
        return temp_heat;
    }

    public void setTemp_heat(final double temp_heat) {
        this.temp_heat = temp_heat;
    }

    @Override
    public void onTick() {
        final double log = (Math.log(64) / Math.log(4));
        double temp = this.generation * 1.175;
        if (this.rodsList.isEmpty()) {

            if (this.temp_heat >= 1) {
                temp_heat -= rand.nextInt((int) this.temp_heat);
            }
            this.reactor.setOutput(0);
        } else if (log > 0) {
            super.onTick();

            if (temp_heat < getMaxHeat()) {
                temp_heat += rand.nextInt(Math.max((int) (getMaxHeat() - temp_heat), 4));
                if (temp_heat > getMaxHeat()) {
                    temp_heat = getMaxHeat();
                }
            }

            for (int j = 0; j < fan[this.reactor.getLevelReactor() - 1]; j++) {
                int power = power_fan[this.reactor.getLevelReactor() - 1];
                temp_heat -= rand.nextInt(30 * power);
                temp_heat = Math.max(1, temp_heat);

            }
            if (temp < 0) {
                temp = 0;
            }
            this.reactor.setOutput(temp);
        } else {
            if (!this.rodsList.isEmpty()) {
                temp_heat += rand.nextInt(200);
            }
            this.reactor.setOutput(0);
        }

        this.reactor.setHeat(temp_heat);


    }

}
