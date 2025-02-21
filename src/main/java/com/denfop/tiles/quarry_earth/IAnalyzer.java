package com.denfop.tiles.quarry_earth;

import com.denfop.api.multiblock.IMultiElement;
import net.minecraft.util.math.ChunkPos;

import java.util.List;
import java.util.Map;

public interface IAnalyzer extends IMultiElement {

    boolean isAnalyzed();

    boolean fullAnalyzed();

    Map<ChunkPos, List<DataPos>> getChunkPoses();

}
