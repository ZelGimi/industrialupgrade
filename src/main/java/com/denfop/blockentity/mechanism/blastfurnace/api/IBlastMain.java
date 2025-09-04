package com.denfop.blockentity.mechanism.blastfurnace.api;

import com.denfop.api.multiblock.MainMultiBlock;

public interface IBlastMain extends MainMultiBlock {

    IBlastHeat getHeat();

    void setHeat(IBlastHeat blastHeat);

    IBlastInputFluid getInputFluid();

    void setInputFluid(IBlastInputFluid blastInputFluid);


    double getProgress();

}
