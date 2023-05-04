package com.denfop.api.sytem;

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
    private static EnergyHandler handler;
    private static GlobalNet rad;

    public static void init() {
        handler = new EnergyHandler();
        SE = new GlobalNet(EnergyType.SOLARIUM);
        QE = new GlobalNet(EnergyType.QUANTUM);
        EXP = new GlobalNet(EnergyType.EXPERIENCE);
        rad = new GlobalNet(EnergyType.RADIATION);

    }

}
