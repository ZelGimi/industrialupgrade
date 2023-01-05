package com.denfop.api.hadroncollider;

import java.util.List;

public interface ISecondLevelHadronCollider extends IFirstLevelHadronCollider {

    IBatteryBlock getBatteryBlock();

    void setBatteryBlock(IBatteryBlock block);

    IMagnetBlock getMagnetBlock();

    void setMagnetBlock(IMagnetBlock magnetBlock);

    IReceivedBlock getReceived();

    void setReceived(IReceivedBlock received);

    List<IBlocksStabilizator> getStabilizators();

}
