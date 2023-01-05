package com.denfop.api.research.event;

import com.denfop.api.research.main.IResearch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;

public class EventSavePracticePoints extends WorldEvent {

    public final EntityPlayer player;
    public final int points;

    public EventSavePracticePoints(final EntityPlayer player, final IResearch research) {
        super(player.getEntityWorld());
        this.player = player;
        this.points = research.pointsPractise();
    }

}
