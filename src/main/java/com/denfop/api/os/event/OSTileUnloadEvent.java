package com.denfop.api.os.event;

import com.denfop.api.os.IOSTile;
import net.minecraft.world.World;

public class OSTileUnloadEvent extends OSTileEvent {

    public OSTileUnloadEvent(final IOSTile tile, World world) {
        super(tile, world);
    }

}
