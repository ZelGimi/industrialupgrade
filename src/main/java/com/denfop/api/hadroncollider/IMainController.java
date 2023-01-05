package com.denfop.api.hadroncollider;

import com.denfop.componets.AdvEnergy;

import java.util.List;

public interface IMainController extends IColliderBuilding {

    IExtractBlock getExtractBlock();

    void setExtractBlock(IExtractBlock extractBlock);

    IOverclockingBlock getOverclockingBlock();

    void setOverclockingBlock(IOverclockingBlock overclockingBlock);

    IPurifierBlock getPurifierBlock();

    void setPurifierBlock(IPurifierBlock purifierBlock);

    AdvEnergy getEnergy();

    EnumLevelCollider getEnumLevel();

    boolean canContainProtons();

    List<Protons> getProtons();

    Structures getStructure();

    boolean canWork();

}
