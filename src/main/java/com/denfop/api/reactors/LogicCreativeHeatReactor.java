package com.denfop.api.reactors;

import java.util.HashMap;
import java.util.Map;

public class LogicCreativeHeatReactor extends LogicCreativeReactor {

    public double temp_heat = 0;
    int[] pump = new int[]{1, 2, 4, 8};
    int[] power_pump = new int[]{1, 2, 3, 4};
    private HashMap<Integer, Double> mapGraphiteConsume;

    public LogicCreativeHeatReactor(final CreativeReactor advReactor) {
        super(advReactor);
    }

    @Override
    public void calculateComponent() {
        super.calculateComponent();
        mapGraphiteConsume = new HashMap<>();
        for (int i = 0; i < this.x; i++) {
            double col = 0;
            for (LogicCreativeComponent component : this.rodsList) {
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
            for (Map.Entry<Integer, Double> ignored : this.mapGraphiteConsume.entrySet()) {
                temp_heat -= rand.nextInt(100);
                if (temp_heat < 0) {
                    temp_heat = 0;
                }
            }
            if (temp_heat < 0) {
                temp_heat = 0;
            }
            int temp = (int) (this.generation * 1.05);
            super.onTick();
            if (temp_heat < 0) {
                temp_heat = 0;
            }
            if (this.temp_heat >= 0 && this.temp_heat < this.getMaxHeat() && this.getMaxHeat() > 1) {

                temp_heat += rand.nextInt((int) ((this.getMaxHeat() - this.temp_heat)));
            }
            for (int j = 0; j < pump[this.reactor.getLevel() - 1]; j++) {

                final int power = power_pump[this.reactor.getLevel() - 1];
                int col = rand.nextInt(10);
                if (power > col) {
                    this.temp_heat -= rand.nextInt(20 * power);
                    if (temp_heat < 0) {
                        temp_heat = 0;
                    }
                } else {
                    this.temp_heat -= rand.nextInt(10 * power);
                    if (temp_heat < 0) {
                        temp_heat = 0;
                    }
                }
                if (rand.nextInt(80) > 15 * power) {

                    this.temp_heat -= rand.nextInt(power * 20);
                    if (temp_heat < 0) {
                        temp_heat = 0;
                    }
                }
            }


            reactor.setOutput(temp);


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
