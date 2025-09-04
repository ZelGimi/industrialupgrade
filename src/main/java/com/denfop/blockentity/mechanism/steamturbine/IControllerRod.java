package com.denfop.blockentity.mechanism.steamturbine;

import com.denfop.api.multiblock.MultiBlockElement;

import java.util.List;

public interface IControllerRod extends MultiBlockElement {

    List<IRod> getRods();

    void setList(List<IRod> rods);

}
