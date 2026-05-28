package com.denfop.api.multiblock.preview;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public final class TestMultiblockStructures {

    private TestMultiblockStructures() {
    }

    public static MultiblockPreviewModel createTestModel(Map<BlockPos, ItemStack> structureMap, Map<BlockPos, Direction> rotationMap) {
        return MultiblockPreviewModel.fromMap(structureMap, rotationMap);
    }

}