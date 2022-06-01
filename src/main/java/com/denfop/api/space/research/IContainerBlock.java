package com.denfop.api.space.research;

import ic2.core.block.invslot.InvSlotOutput;

public interface IContainerBlock {

    IResearchTable getResearchTable();

    void setResearchTable(IResearchTable table);

    InvSlotOutput getSlotOutput();

}
