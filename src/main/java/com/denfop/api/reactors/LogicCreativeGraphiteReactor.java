package com.denfop.api.reactors;

import java.util.HashMap;
import java.util.Map;

public class LogicCreativeGraphiteReactor extends LogicCreativeReactor {

    public double temp_heat = 0;
    public int[] coolant = new int[]{2, 3, 4, 4};
    Map<Integer, Double> mapGraphiteConsume;

    public LogicCreativeGraphiteReactor(final CreativeReactor advReactor) {
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
            mapGraphiteConsume.put(i, col);
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
            super.onTick();
            if (temp_heat < getMaxHeat() && getMaxHeat() != 0) {
                double temp = getMaxHeat() - temp_heat;
                temp_heat += rand.nextInt(Math.max((int) temp, 1));
            }

            temp_heat = this.workCoolant(temp_heat);
            for (int i = 0; i < reactor.getWidth(); i++) {
                if (this.mapGraphiteConsume.get(i) == 0) {
                    continue;
                }
                double col = this.mapGraphiteConsume.get(i);
                if (temp_heat > getMaxHeat()) {
                    temp_heat -= rand.nextInt((int) (temp_heat - getMaxHeat()));
                    if (temp_heat < 0) {
                        temp_heat = 0;
                    }
                }
                temp_heat -= rand.nextInt((int) (10 * col));
                if (temp_heat < 0) {
                    temp_heat = 0;
                }

            }
            if (temp_heat < 0) {
                temp_heat = 0;
            }
            reactor.setHeat(temp_heat);
        }
    }

    private double workCoolant(double heat) {
        for (int i = 0; i < coolant[this.reactor.getBlockLevel() - 1]; i++) {
            heat = work(heat);
        }
        return heat;
    }

    private double work(double heat) {
        return Math.max(1, heat - rand.nextInt(40 * (this.reactor.getBlockLevel())));
    }

    public double getTemp_heat() {
        return temp_heat;
    }

    public void setTemp_heat(final double temp_heat) {
        this.temp_heat = temp_heat;
    }

}
