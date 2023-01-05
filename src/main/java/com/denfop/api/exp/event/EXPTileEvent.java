package com.denfop.api.exp.event;

import com.denfop.api.exp.IEXPTile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EXPTileEvent extends WorldEvent {

    public final IEXPTile tile;

    public EXPTileEvent(IEXPTile tile, World world) {
        super(world);
        this.tile = tile;
    }

}
