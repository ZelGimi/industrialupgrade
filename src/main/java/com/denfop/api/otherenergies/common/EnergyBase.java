package com.denfop.api.otherenergies.common;

import com.denfop.api.otherenergies.common.interfaces.GlobalNet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnergyBase {

    public static com.denfop.api.otherenergies.common.networking.GlobalNet QE;
    public static com.denfop.api.otherenergies.common.networking.GlobalNet EXP;
    public static List<GlobalNet> listGlobal = new ArrayList<>();
    public static Map<EnergyType, GlobalNet> globalNetMap = new HashMap<>();
    public static GlobalNet SE;
    public static EnergyHandler handler;
    public static com.denfop.api.otherenergies.common.networking.GlobalNet rad;
    public static com.denfop.api.otherenergies.common.networking.GlobalNet positrons;
    public static com.denfop.api.otherenergies.common.networking.GlobalNet steam;
    public static com.denfop.api.otherenergies.common.networking.GlobalNet ampere;
    public static com.denfop.api.otherenergies.common.networking.GlobalNet biofuel;
    public static com.denfop.api.otherenergies.common.networking.GlobalNet NE;

    public static void init() {
        handler = new EnergyHandler();
        SE = new com.denfop.api.otherenergies.common.networking.GlobalNet(EnergyType.SOLARIUM);
        NE = new com.denfop.api.otherenergies.common.networking.GlobalNet(EnergyType.NIGHT);
        QE = new com.denfop.api.otherenergies.common.networking.GlobalNet(EnergyType.QUANTUM);
        EXP = new com.denfop.api.otherenergies.common.networking.GlobalNet(EnergyType.EXPERIENCE);
        rad = new com.denfop.api.otherenergies.common.networking.GlobalNet(EnergyType.RADIATION);
        positrons = new com.denfop.api.otherenergies.common.networking.GlobalNet(EnergyType.POSITRONS);
        steam = new com.denfop.api.otherenergies.common.networking.GlobalNet(EnergyType.STEAM);
        ampere = new com.denfop.api.otherenergies.common.networking.GlobalNet(EnergyType.AMPERE);
        biofuel = new com.denfop.api.otherenergies.common.networking.GlobalNet(EnergyType.BIOFUEL);

    }

    public static GlobalNet getGlobal(EnergyType energyType) {
        return globalNetMap.get(energyType);
    }

}
