package com.denfop.api.research.event;

import com.denfop.api.research.main.IResearch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;

public class EventLoadResearch extends WorldEvent {

    public final EntityPlayer player;
    public final IResearch research;

    public EventLoadResearch(final EntityPlayer player, final IResearch research) {
        super(player.getEntityWorld());
        this.player = player;
        this.research = research;
    }

}
