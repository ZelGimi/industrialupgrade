package com.denfop.blockentity.mechanism.steamturbine;

import com.denfop.api.multiblock.IMultiElement;

import java.util.List;

public interface IControllerRod extends IMultiElement {

    List<IRod> getRods();

    void setList(List<IRod> rods);

}
