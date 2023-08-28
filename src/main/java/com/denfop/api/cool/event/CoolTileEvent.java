package com.denfop.api.cool.event;

import com.denfop.api.cool.ICoolTile;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class CoolTileEvent extends WorldEvent {

    public final ICoolTile tile;

    public CoolTileEvent(ICoolTile tile, World world) {
        super(world);
        this.tile = tile;
    }

}
