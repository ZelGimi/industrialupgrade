package com.denfop.api.qe.event;

import com.denfop.api.cooling.ICoolTile;
import com.denfop.api.qe.IQETile;
import net.minecraft.world.World;

public class QETileUnloadEvent extends  QETileEvent {

    public QETileUnloadEvent(final IQETile tile, World world) {
        super(tile,world);
    }

}
