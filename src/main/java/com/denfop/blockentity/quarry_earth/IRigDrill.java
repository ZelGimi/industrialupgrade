package com.denfop.blockentity.quarry_earth;

import com.denfop.api.multiblock.IMultiElement;

import java.util.List;

public interface IRigDrill extends IMultiElement {

    void startOperation(List<DataPos> dataPos);

}
