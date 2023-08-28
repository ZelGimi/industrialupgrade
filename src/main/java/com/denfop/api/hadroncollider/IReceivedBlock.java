package com.denfop.api.hadroncollider;

public interface IReceivedBlock extends IColliderBuilding {

    IMainController getController();

    void setController(IMainController controller);

    IOverclockingBlock getOverclockingBlock();

    void setOverclockingBlock(IOverclockingBlock overclockingBlock);

    IPurifierBlock getPurifierBlock();

    void setPurifierBlock(IPurifierBlock purifierBlock);

    EnumLevelCollider getEnumLevel();

    IExtractBlock getExtractBlock();

}
