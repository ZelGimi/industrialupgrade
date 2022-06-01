package com.denfop.api.os.event;

import com.denfop.api.os.IOSTile;
import net.minecraft.world.World;

public class OSTileLoadEvent extends OSTileEvent {

    public OSTileLoadEvent(final IOSTile tile, World world) {
        super(tile, world);
    }

}
