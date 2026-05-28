package com.denfop.api.storage;

import com.denfop.api.storage.autocrafting.SameStack;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Map;

public interface Controller extends ElectricStorage {

    StorageNetwork getStorageNetwork();

    void setStack(boolean isFluid, boolean isCreate, List<SameStack> stacks);

    double getPower();

    void setStackForCraft(Map<String, Map<SameStack, Integer>> stacksMapCount);

    BlockPos getPosController();
}
