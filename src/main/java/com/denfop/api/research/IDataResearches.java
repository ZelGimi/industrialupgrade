package com.denfop.api.research;

import com.denfop.api.research.main.IResearch;
import com.denfop.api.research.main.IResearchPages;

import java.util.List;
import java.util.Map;

public interface IDataResearches {

    List<IResearch> getListResearches();

    List<IResearchPages> getListResearchesPages();

    Map<IResearchPages, IResearch> getMapPagesResearches();

    Map<IResearch, IResearchPages> getMapResearches();

}
