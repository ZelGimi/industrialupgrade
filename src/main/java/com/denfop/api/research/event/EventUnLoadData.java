package com.denfop.api.research.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;

public class EventUnLoadData extends WorldEvent {

    public final EntityPlayer player;

    public EventUnLoadData(final EntityPlayer player) {
        super(player.getEntityWorld());
        this.player = player;
    }

}
