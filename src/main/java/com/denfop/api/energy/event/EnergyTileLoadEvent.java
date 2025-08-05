package com.denfop.api.energy.event;

import com.denfop.api.energy.IEnergyTile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.level.LevelEvent;

public class EnergyTileLoadEvent extends LevelEvent {

    public final IEnergyTile tile;
    public final BlockEntity tileentity;

    public EnergyTileLoadEvent(Level world, BlockEntity tile, IEnergyTile energyTile1) {
        super(world);
        this.tile = energyTile1;
        this.tileentity = tile;
    }

    public EnergyTileLoadEvent(Level world, IEnergyTile energyTile1) {
        super(world);
        this.tile = energyTile1;
        this.tileentity = world.getBlockEntity(energyTile1.getPos());
    }

}
