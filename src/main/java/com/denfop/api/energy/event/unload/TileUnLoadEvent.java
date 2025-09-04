package com.denfop.api.energy.event.unload;


import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.level.LevelEvent;

public class TileUnLoadEvent extends LevelEvent {


    public final BlockEntity tileentity;

    public TileUnLoadEvent(Level world, BlockEntity tile) {
        super(world);
        this.tileentity = tile;
    }


}
