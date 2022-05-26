package com.denfop.api.research.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EventLoadData extends WorldEvent {

    public final EntityPlayer player;

    public EventLoadData(final EntityPlayer player) {
        super(player.getEntityWorld());
        this.player=player;
    }

}
