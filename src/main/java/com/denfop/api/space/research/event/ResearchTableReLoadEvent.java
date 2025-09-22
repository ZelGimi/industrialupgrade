package com.denfop.api.space.research.event;

import com.denfop.api.space.research.api.IResearchTable;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class ResearchTableReLoadEvent extends WorldEvent {

    public final IResearchTable table;

    public ResearchTableReLoadEvent(World world, IResearchTable table) {
        super(world);
        this.table = table;
    }

}
