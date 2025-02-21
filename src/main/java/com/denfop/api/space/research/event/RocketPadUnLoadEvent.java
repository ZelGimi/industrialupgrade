package com.denfop.api.space.research.event;

import com.denfop.api.space.research.api.IRocketLaunchPad;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class RocketPadUnLoadEvent extends WorldEvent {

    public final IRocketLaunchPad table;

    public RocketPadUnLoadEvent(World world, IRocketLaunchPad table) {
        super(world);
        this.table = table;
    }

}
