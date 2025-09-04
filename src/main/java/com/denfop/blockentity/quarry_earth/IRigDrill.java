package com.denfop.blockentity.quarry_earth;

import com.denfop.api.multiblock.MultiBlockElement;

import java.util.List;

public interface IRigDrill extends MultiBlockElement {

    void startOperation(List<DataPos> dataPos);

}
