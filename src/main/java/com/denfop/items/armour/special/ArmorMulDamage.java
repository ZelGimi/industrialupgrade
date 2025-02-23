package com.denfop.items.armour.special;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArmorMulDamage {

    private final List<Double> coefficientsList;

    public ArmorMulDamage(double... coefficients) {
        this.coefficientsList = Arrays.stream(coefficients)
                .boxed()
                .collect(Collectors.toList());
        while (this.coefficientsList.size() < 4) {
            this.coefficientsList.add(1.0);
        }
    }

    public double getHeadMul() {
        return this.coefficientsList.get(0);
    }

    public double getChestMul() {
        return this.coefficientsList.get(1);
    }

    public double getLeggingsMul() {
        return this.coefficientsList.get(2);
    }

    public double getBootsMul() {
        return this.coefficientsList.get(3);
    }

}
