package com.denfop.api.hadroncollider;

import net.minecraftforge.fluids.FluidTank;

public interface IPurifierBlock extends IColliderBuilding {

    FluidTank getFluidTank();

    IMainController getController();

    void setController(IMainController controller);

    IExtractBlock getExtractBlock();

    void setExtractBlock(IExtractBlock extractBlock);

    IOverclockingBlock getOverclockingBlock();

    void setOverclockingBlock(IOverclockingBlock overclockingBlock);

    EnumLevelCollider getEnumLevel();

    int getPercent();

}
