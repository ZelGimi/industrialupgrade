package com.denfop.api.hadroncollider;

public interface IHadronColliderSystem extends IColliderBuilding {

    void startProcess(IMainController mainController);

    void addProtonsInProcess(IMainController mainController);

    void Overclocking();

    boolean transfer(IExtractBlock block, Protons proton);

}
