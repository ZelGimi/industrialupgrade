package com.denfop.api.space.research.api;

import com.denfop.api.space.IBody;
import com.denfop.api.space.fakebody.Data;
import com.denfop.api.space.rovers.api.IRovers;

public interface IResearchSystem {

    void sendingOperation(IRovers rovers, IBody body, IResearchTable table);

    boolean canSendingOperation(IRovers rovers, IBody body, IResearchTable table);

    void returnOperation(IBody body, IResearchTable table);

    Data getDataFromPlayer(final IResearchTable table, IBody body);

    void sendingAutoOperation(IRovers rover, IBody target, IResearchTable researchTable);

}
