package com.denfop.api.windsystem;

import java.util.Collections;
import java.util.List;

public enum EnumWindSide {
    W(EnumHorizonSide.W),
    S(EnumHorizonSide.S),
    N(EnumHorizonSide.N),
    E(EnumHorizonSide.E),
    NW(EnumHorizonSide.NW),
    NE(EnumHorizonSide.EN),
    SW(EnumHorizonSide.SW),
    SE(EnumHorizonSide.ES);
    private final List<EnumHorizonSide> list;


    EnumWindSide(EnumHorizonSide list) {
        this.list = Collections.singletonList(list);
    }

    public static EnumWindSide getValue(int index) {
        EnumWindSide[] enumHorizonSides = values();
        return enumHorizonSides[index];
    }

    public List<EnumHorizonSide> getList() {
        return list;
    }
}
