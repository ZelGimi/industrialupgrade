package com.denfop.api.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ElectricStorage {

    double getRequiredPower();

    void setStorageNetwork(StorageNetwork network);

    BlockPos getPos();

    Level getWorld();
}
