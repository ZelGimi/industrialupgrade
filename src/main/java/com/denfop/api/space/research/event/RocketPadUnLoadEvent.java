package com.denfop.api.space.research.event;

import com.denfop.api.space.research.api.IRocketLaunchPad;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class RocketPadUnLoadEvent extends LevelEvent {

    public final IRocketLaunchPad table;

    public RocketPadUnLoadEvent(Level world, IRocketLaunchPad table) {
        super(world);
        this.table = table;
    }

}
