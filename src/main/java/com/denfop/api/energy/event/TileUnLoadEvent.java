package com.denfop.api.energy.event;


import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.event.level.LevelEvent;

public class TileUnLoadEvent extends LevelEvent {


    public final BlockEntity tileentity;

    public TileUnLoadEvent(Level world, BlockEntity tile) {
        super(world);
        this.tileentity = tile;
    }


}
