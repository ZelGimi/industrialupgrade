package com.denfop.api.space.research.event;

import com.denfop.api.space.research.api.IRocketLaunchPad;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class RocketPadReLoadEvent extends LevelEvent {

    public final IRocketLaunchPad table;

    public RocketPadReLoadEvent(Level world, IRocketLaunchPad table) {
        super(world);
        this.table = table;
    }

}
