package com.denfop.api.space.research;


import com.denfop.api.recipe.InvSlotOutput;

public interface IContainerBlock {

    IResearchTable getResearchTable();

    void setResearchTable(IResearchTable table);

    InvSlotOutput getSlotOutput();

}
