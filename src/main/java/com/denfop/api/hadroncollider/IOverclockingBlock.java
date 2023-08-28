package com.denfop.api.hadroncollider;


import java.util.List;

public interface IOverclockingBlock extends IColliderBuilding {

    List<Protons> getProtons();

    IMainController getController();

    void setController(IMainController controller);

    IExtractBlock getExtractBlock();

    void setExtractBlock(IExtractBlock extractBlock);

    IPurifierBlock getPurifierBlock();

    void setPurifierBlock(IPurifierBlock purifierBlock);

    EnumLevelCollider getEnumLevel();

}
