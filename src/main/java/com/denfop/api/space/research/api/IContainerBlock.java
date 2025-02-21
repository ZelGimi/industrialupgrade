package com.denfop.api.space.research.api;


import com.denfop.api.recipe.InvSlotOutput;

public interface IContainerBlock {

    IResearchTable getResearchTable();

    void setResearchTable(IResearchTable table);

    InvSlotOutput getSlotOutput();

}
