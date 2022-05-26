package com.denfop.api.qe.event;

import com.denfop.api.heat.IHeatTile;
import com.denfop.api.qe.IQETile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class QETileEvent extends WorldEvent {

    public final IQETile tile;

    public QETileEvent(IQETile tile, World world) {
        super(world);
        this.tile = tile;
    }

}
