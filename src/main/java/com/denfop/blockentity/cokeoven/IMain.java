package com.denfop.blockentity.cokeoven;

import com.denfop.api.multiblock.MainMultiBlock;

public interface IMain extends MainMultiBlock {

    IInputFluid getInputFluid();

    void setInputFluid(IInputFluid blastInputFluid);

    IOutputFluid getOutputFluid();

    void setOutputFluid(IOutputFluid blastInputFluid);


    double getProgress();

    IHeat getHeat();

    void setHeat(IHeat blastHeat);

}
