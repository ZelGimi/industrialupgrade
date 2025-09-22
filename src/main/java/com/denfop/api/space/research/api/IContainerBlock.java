package com.denfop.api.space.research.api;


import com.denfop.api.recipe.InventoryOutput;

public interface IContainerBlock {

    IResearchTable getResearchTable();

    void setResearchTable(IResearchTable table);

    InventoryOutput getSlotOutput();

}
