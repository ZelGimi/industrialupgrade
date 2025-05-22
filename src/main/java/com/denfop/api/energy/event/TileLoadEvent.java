package com.denfop.api.energy.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class TileLoadEvent extends WorldEvent {


    public final TileEntity tileentity;

    public TileLoadEvent(World world, TileEntity tile) {
        super(world);
        this.tileentity = tile;
    }


}
