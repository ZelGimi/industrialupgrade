package com.denfop.api.energy.event;

import com.denfop.api.energy.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class TileUnLoadEvent extends WorldEvent {


    public final TileEntity tileentity;

    public TileUnLoadEvent(World world, TileEntity tile) {
        super(world);
        this.tileentity = tile;
    }


}
