package com.denfop.api.space.research.event;

import com.denfop.api.space.research.api.IResearchTable;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.LevelEvent;

public class ResearchTableUnLoadEvent extends LevelEvent {

    public final IResearchTable table;

    public ResearchTableUnLoadEvent(Level world, IResearchTable table) {
        super(world);
        this.table = table;
    }

}
