package com.denfop.api.otherenergies.pressure.event;

import com.denfop.api.otherenergies.pressure.IPressureTile;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class PressureTileEvent extends LevelEvent {

    public final IPressureTile tile;

    public PressureTileEvent(IPressureTile tile, Level world) {
        super(world);
        this.tile = tile;

    }

}
