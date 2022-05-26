package aroma1997.uncomplication.enet;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergySourceInfo;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;

import java.util.HashMap;
import java.util.Map;

public class EnergyTransferList {
    public static Map<String, Double> values;
    public static Map<String, Double> acceptingOverride;

    public EnergyTransferList() {
        init();
    }

    public static double getMaxEnergy(final IEnergySource par1, double currentMax) {
        if (par1 instanceof IEnergySourceInfo) {
            final IEnergySourceInfo info = (IEnergySourceInfo) par1;
            return info.getMaxEnergyAmount();
        }
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

    public static void initIEnergySource(final IEnergySource par1) {
        if (!EnergyTransferList.values.containsKey(par1.getClass().getSimpleName())) {
            EnergyTransferList.values.put(par1.getClass().getSimpleName(), par1.getOfferedEnergy());
        }
    }

    public static boolean hasOverrideInput(final IEnergySink par1) {
        if (par1 == null) {
            return false;
        }
        final Class clz = par1.getClass();
        return EnergyTransferList.acceptingOverride.containsKey(clz.getSimpleName());
    }

    public static double getOverrideInput(final IEnergySink par1) {
        if (!hasOverrideInput(par1)) {
            return 0;
        }
        final Class clz = par1.getClass();
        return EnergyTransferList.acceptingOverride.get(clz.getSimpleName());
    }

    public static void init() {
        final Map<String, Double> list = new HashMap<>();
        list.put("TileEntityEnergyOMat", 32.0);
        list.put("TileEntityNuclearReactorElectric", (double) Math.round(1512.0f * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/nuclear")));
        list.put("TileEntityReactorChamberElectric", (double) Math.round(240.0f * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/nuclear")));
        list.put("TileEntityWindGenerator", 10.0);
        list.put("TileEntityGenerator", (double) Math.round(10.0f * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/generator")));
        list.put("TileEntityGeoGenerator", (double) Math.round(20.0f * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/geothermal")));
        list.put("TileEntitySolarGenerator", 1.0);
        list.put("TileEntityWaterGenerator", 2.0);
        list.put("TileEntityElectricBatBox", 32.0);
        list.put("TileEntityStirlingGenerator", 128.0);
        list.put("TileEntityKineticGenerator", 512.0);
        list.put("TileEntityRTGenerator", 32.0);
        list.put("TileEntitySemifluidGenerator", 32.0);
        list.put("TileEntityChargepadBatBox", 32.0);
        list.put("TileEntityChargepadCESU", 128.0);
        list.put("TileEntityChargepadMFE", 512.0);
        list.put("TileEntityChargepadMFSU", 2048.0);
        list.put("TileEntityTransformerMV", 512.0);
        list.put("TileEntityTransformerLV", 128.0);
        list.put("TileEntityTransformerHV", 2048.0);
        list.put("TileEntityTransformerEV", 8192.0);
        list.put("TileEntityElectricMFSU", 2048.0);
        list.put("TileEntityElectricMFE", 512.0);
        list.put("TileEntityElectricCESU", 128.0);
        EnergyTransferList.values.putAll(list);
    }

    static {
        EnergyTransferList.values = new HashMap<>();
        EnergyTransferList.acceptingOverride = new HashMap<>();
    }
}
