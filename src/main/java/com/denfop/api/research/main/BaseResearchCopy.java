package com.denfop.api.research.main;

import java.util.ArrayList;
import java.util.List;

public class BaseResearchCopy implements IResearchCopy {

    public final IResearch research;
    public final List<IResearchPart> list;

    public BaseResearchCopy(IResearch research) {
        this.research = research;
        this.list = new ArrayList<>();
    }


    @Override
    public List<IResearchPart> getParts() {
        return this.list;
    }

    @Override
    public IResearch getResearch() {
        return this.research;
    }

}


