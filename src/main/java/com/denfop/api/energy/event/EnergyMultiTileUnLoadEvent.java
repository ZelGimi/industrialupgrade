package com.denfop.api.energy.event;

import com.denfop.api.energy.IAdvEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EnergyMultiTileUnLoadEvent extends WorldEvent {

    public final IAdvEnergyTile tile;
    public final TileEntity tileentity;
    public final IAdvEnergyTile subject;

    public EnergyMultiTileUnLoadEvent(World world, TileEntity main_tile, IAdvEnergyTile main, IAdvEnergyTile subject) {
        super(world);
        this.tile = main;
        this.tileentity = main_tile;
        this.subject = subject;
    }

    public EnergyMultiTileUnLoadEvent(World world, IAdvEnergyTile main, IAdvEnergyTile subject) {
        super(world);
        this.tile = main;
        this.tileentity = main.getTileEntity();
        this.subject = subject;
    }

}