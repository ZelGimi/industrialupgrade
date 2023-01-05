package com.denfop.api.research;

import com.denfop.api.research.main.IResearch;
import com.denfop.api.research.main.IResearchPages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataResearches implements IDataResearches {

    List<IResearch> list_researches;
    List<IResearchPages> list_researches_pages;
    Map<IResearchPages, IResearch> map_researches_pages;
    Map<IResearch, IResearchPages> map_researches;

    public DataResearches() {
        this.list_researches = new ArrayList<>();
        this.list_researches_pages = new ArrayList<>();
        this.map_researches = new HashMap<>();
        this.map_researches_pages = new HashMap<>();
    }

    @Override
    public List<IResearch> getListResearches() {
        return this.list_researches;
    }

    @Override
    public List<IResearchPages> getListResearchesPages() {
        return this.list_researches_pages;
    }

    @Override
    public Map<IResearchPages, IResearch> getMapPagesResearches() {
        return this.map_researches_pages;
    }

    @Override
    public Map<IResearch, IResearchPages> getMapResearches() {
        return this.map_researches;
    }

}
