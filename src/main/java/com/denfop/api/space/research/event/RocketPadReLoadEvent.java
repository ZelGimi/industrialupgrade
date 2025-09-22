package com.denfop.api.space.research.event;

import com.denfop.api.space.research.api.IRocketLaunchPad;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class RocketPadReLoadEvent extends WorldEvent {

    public final IRocketLaunchPad table;

    public RocketPadReLoadEvent(World world, IRocketLaunchPad table) {
        super(world);
        this.table = table;
    }

}
