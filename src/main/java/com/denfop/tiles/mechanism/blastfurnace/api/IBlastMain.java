package com.denfop.tiles.mechanism.blastfurnace.api;

public interface IBlastMain {

    boolean getFull();

    void setFull(boolean full);

    void update_block();

    IBlastHeat getHeat();

    void setHeat(IBlastHeat blastHeat);

    IBlastInputFluid getInputFluid();

    void setInputFluid(IBlastInputFluid blastInputFluid);

    IBlastInputItem getInputItem();

    void setInputItem(IBlastInputItem blastInputItem);

    IBlastOutputItem getOutputItem();

    void setOutputItem(IBlastOutputItem blastOutputItem);

    double getProgress();

}
