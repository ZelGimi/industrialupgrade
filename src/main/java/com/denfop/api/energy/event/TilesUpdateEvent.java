package com.denfop.api.energy.event;


import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.List;

public class TilesUpdateEvent extends LevelEvent {


    public final List<BlockEntity> tiles;

    public TilesUpdateEvent(Level world, List<BlockEntity> tiles) {
        super(world);
        this.tiles = tiles;
    }


}
