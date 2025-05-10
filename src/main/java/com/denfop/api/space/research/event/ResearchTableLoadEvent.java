package com.denfop.api.space.research.event;

import com.denfop.api.space.research.api.IResearchTable;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class ResearchTableLoadEvent extends LevelEvent {

    public final IResearchTable table;

    public ResearchTableLoadEvent(Level world, IResearchTable table) {
        super(world);
        this.table = table;
    }

}
