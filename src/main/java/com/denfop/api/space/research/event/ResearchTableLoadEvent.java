package com.denfop.api.space.research.event;

import com.denfop.api.space.research.api.IResearchTable;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class ResearchTableLoadEvent extends WorldEvent {

    public final IResearchTable table;

    public ResearchTableLoadEvent(World world, IResearchTable table) {
        super(world);
        this.table = table;
    }

}
