package com.denfop.api.pressure.event;

import com.denfop.api.pressure.IPressureTile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class PressureTileEvent extends WorldEvent {

    public final IPressureTile tile;

    public PressureTileEvent(IPressureTile tile, World world) {
        super(world);
        this.tile = tile;

    }

}
