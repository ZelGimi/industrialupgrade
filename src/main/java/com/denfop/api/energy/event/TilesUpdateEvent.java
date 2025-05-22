package com.denfop.api.energy.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

import java.util.List;

public class TilesUpdateEvent extends WorldEvent {


    public final List<TileEntity> tiles;

    public TilesUpdateEvent(World world, List<TileEntity> tiles) {
        super(world);
        this.tiles = tiles;
    }


}
