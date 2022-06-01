package com.denfop.api.os.event;

import com.denfop.api.os.IOSTile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class OSTileEvent extends WorldEvent {

    public final IOSTile tile;

    public OSTileEvent(IOSTile tile, World world) {
        super(world);
        this.tile = tile;
    }

}
