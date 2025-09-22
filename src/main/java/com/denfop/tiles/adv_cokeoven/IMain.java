package com.denfop.tiles.adv_cokeoven;

import com.denfop.api.multiblock.IMainMultiBlock;

public interface IMain extends IMainMultiBlock {

    IInputFluid getInputFluid();

    void setInputFluid(IInputFluid blastInputFluid);

    IOutputFluid getOutputFluid();

    void setOutputFluid(IOutputFluid blastInputFluid);


    double getProgress();

    IHeat getHeat();

    void setHeat(IHeat blastHeat);

}
