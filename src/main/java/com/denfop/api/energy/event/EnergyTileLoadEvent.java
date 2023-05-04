package com.denfop.api.energy.event;

import com.denfop.api.energy.IAdvEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EnergyTileLoadEvent extends WorldEvent {

    public final IAdvEnergyTile tile;
    public final TileEntity tileentity;

    public EnergyTileLoadEvent(World world, TileEntity tile, IAdvEnergyTile energyTile1) {
        super(world);
        this.tile = energyTile1;
        this.tileentity = tile;
    }

    public EnergyTileLoadEvent(World world, IAdvEnergyTile energyTile1) {
        super(world);
        this.tile = energyTile1;
        this.tileentity = energyTile1.getTileEntity();
    }

}
