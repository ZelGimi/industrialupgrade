package com.denfop.api.qe.event;

import com.denfop.api.qe.IQETile;
import net.minecraft.world.World;

public class QETileLoadEvent extends QETileEvent {

    public QETileLoadEvent(final IQETile tile, World world) {
        super(tile,world);
    }

}
