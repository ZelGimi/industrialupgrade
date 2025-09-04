package com.denfop.api.otherenergies.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnergyBase {

    public static GlobalNet QE;
    public static GlobalNet EXP;
    public static List<IGlobalNet> listGlobal = new ArrayList<>();
    public static Map<EnergyType, IGlobalNet> globalNetMap = new HashMap<>();
    public static IGlobalNet SE;
    public static EnergyHandler handler;
    public static GlobalNet rad;
    public static GlobalNet positrons;
    public static GlobalNet steam;
    public static GlobalNet ampere;
    public static GlobalNet biofuel;
    public static GlobalNet NE;

    public static void init() {
        handler = new EnergyHandler();
        SE = new GlobalNet(EnergyType.SOLARIUM);
        NE = new GlobalNet(EnergyType.NIGHT);
        QE = new GlobalNet(EnergyType.QUANTUM);
        EXP = new GlobalNet(EnergyType.EXPERIENCE);
        rad = new GlobalNet(EnergyType.RADIATION);
        positrons = new GlobalNet(EnergyType.POSITRONS);
        steam = new GlobalNet(EnergyType.STEAM);
        ampere = new GlobalNet(EnergyType.AMPERE);
        biofuel = new GlobalNet(EnergyType.BIOFUEL);

    }

    public static IGlobalNet getGlobal(EnergyType energyType) {
        return globalNetMap.get(energyType);
    }

}
