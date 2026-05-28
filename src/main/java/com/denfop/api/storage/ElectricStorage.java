package com.denfop.api.storage;

import net.minecraft.core.BlockPos;

public interface ElectricStorage {

    double getRequiredPower();

    void setStorageNetwork(StorageNetwork network);

    BlockPos getPos();
}
