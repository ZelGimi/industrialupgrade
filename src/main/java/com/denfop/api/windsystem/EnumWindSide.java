package com.denfop.api.windsystem;


import net.minecraft.world.phys.Vec3;

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

    public Vec3 getDirectionVector() {
        switch (this) {
            case W:
                return new Vec3(-1, 0, 0);
            case E:
                return new Vec3(1, 0, 0);
            case N:
                return new Vec3(0, 0, -1);
            case S:
                return new Vec3(0, 0, 1);
            case NW:
                return new Vec3(-1, 0, -1).normalize();
            case NE:
                return new Vec3(1, 0, -1).normalize();
            case SW:
                return new Vec3(-1, 0, 1).normalize();
            case SE:
                return new Vec3(1, 0, 1).normalize();
            default:
                return new Vec3(0, 0, 0);
        }
    }
}
