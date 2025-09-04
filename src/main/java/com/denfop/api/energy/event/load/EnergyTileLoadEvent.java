package com.denfop.api.energy.event.load;

import com.denfop.api.energy.interfaces.EnergyTile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.event.level.LevelEvent;

public class EnergyTileLoadEvent extends LevelEvent {

    public final EnergyTile tile;
    public final BlockEntity tileentity;

    public EnergyTileLoadEvent(Level world, BlockEntity tile, EnergyTile energyTile1) {
        super(world);
        this.tile = energyTile1;
        this.tileentity = tile;
    }

    public EnergyTileLoadEvent(Level world, EnergyTile energyTile1) {
        super(world);
        this.tile = energyTile1;
        this.tileentity = world.getBlockEntity(energyTile1.getPos());
    }

}
