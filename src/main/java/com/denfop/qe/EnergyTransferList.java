package com.denfop.qe;



import com.denfop.api.qe.*;

import java.util.HashMap;
import java.util.Map;

public class EnergyTransferList {

    public static Map<String, Double> values;
    public static Map<String, Double> acceptingOverride;

    static {
        EnergyTransferList.values = new HashMap<>();
        EnergyTransferList.acceptingOverride = new HashMap<>();
    }

    public EnergyTransferList() {
        init();
    }

    public static double getMaxEnergy(final IQESource par1, double currentMax) {

        if (!EnergyTransferList.values.containsKey(par1.getClass().getSimpleName())) {
            EnergyTransferList.values.put(par1.getClass().getSimpleName(), currentMax);
        }
        if (EnergyTransferList.values.containsKey(par1.getClass().getSimpleName())) {
            final double newValue = EnergyTransferList.values.get(par1.getClass().getSimpleName());
            if (newValue < currentMax) {
                EnergyTransferList.values.put(par1.getClass().getSimpleName(), currentMax);
            }
            currentMax = EnergyTransferList.values.get(par1.getClass().getSimpleName());
        }
        return currentMax;
    }

    public static void initIEnergySource(final IQESource par1) {
        if (!EnergyTransferList.values.containsKey(par1.getClass().getSimpleName())) {
            EnergyTransferList.values.put(par1.getClass().getSimpleName(), par1.getOfferedQE());
        }
    }

    public static boolean hasOverrideInput(final IQESink par1) {
        if (par1 == null) {
            return false;
        }
        final Class clz = par1.getClass();
        return EnergyTransferList.acceptingOverride.containsKey(clz.getSimpleName());
    }

    public static double getOverrideInput(final IQESink par1) {
        if (!hasOverrideInput(par1)) {
            return 0;
        }
        final Class clz = par1.getClass();
        return EnergyTransferList.acceptingOverride.get(clz.getSimpleName());
    }

    public static void init() {
        final Map<String, Double> list = new HashMap<>();
        EnergyTransferList.values.putAll(list);
    }
}
