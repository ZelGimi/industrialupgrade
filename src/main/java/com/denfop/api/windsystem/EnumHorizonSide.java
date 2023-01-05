package com.denfop.api.windsystem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum EnumHorizonSide {
    W(),
    E(),
    N(),
    S(),
    NW(N, W),
    SW(S, W),
    EN(N, E),
    ES(S, E),
    EW(E, W),
    SN(S, N),
    ;

    private List<EnumHorizonSide> enumHorizonSide;

    EnumHorizonSide(EnumHorizonSide... enumHorizonSide) {
        this.enumHorizonSide = Arrays.asList(enumHorizonSide);
        if (this.enumHorizonSide.isEmpty()) {
            this.enumHorizonSide = Collections.singletonList(this);
        }
    }


    public List<EnumHorizonSide> getEnumWindSide() {
        return enumHorizonSide;
    }
}
