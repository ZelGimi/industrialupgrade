package com.denfop.tiles.mechanism.blastfurnace.api;

import com.denfop.api.multiblock.IMainMultiBlock;

public interface IBlastMain extends IMainMultiBlock {

    IBlastHeat getHeat();

    void setHeat(IBlastHeat blastHeat);

    IBlastInputFluid getInputFluid();

    void setInputFluid(IBlastInputFluid blastInputFluid);


    double getProgress();

}
