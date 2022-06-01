package com.denfop.api.space.research;

import com.denfop.api.space.IBody;
import com.denfop.api.space.rovers.IRovers;

public interface IResearchSystem {

    void sendingOperation(IRovers rovers, IBody body, IResearchTable table);

    boolean canSendingOperation(IRovers rovers, IBody body, IResearchTable table);

    void returnOperation(IBody body, IResearchTable table);


}
