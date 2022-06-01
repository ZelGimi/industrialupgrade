package com.denfop.api.hadroncollider;

public interface IExtractBlock extends IFirstLevelHadronCollider, IColliderBuilding {

    boolean canTransfer();

    void transfer();

    IMainController getController();

    void setController(IMainController controller);

    IOverclockingBlock getOverclockingBlock();

    void setOverclockingBlock(IOverclockingBlock overclockingBlock);

    IPurifierBlock getPurifierBlock();

    void setPurifierBlock(IPurifierBlock purifierBlock);

    EnumLevelCollider getEnumLevel();

    IMainController getOtherController();

    IReceivedBlock getReceivedBlock();

}
