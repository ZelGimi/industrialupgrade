package com.denfop.api.multiblock;

import java.util.HashMap;
import java.util.Map;

public class MultiBlockSystem {

    public static MultiBlockSystem instance;
    public final Map<String, MultiBlockStructure> mapMultiBlocks = new HashMap<>();

    public MultiBlockSystem() {
        instance = this;
    }

    public static MultiBlockSystem getInstance() {
        return instance;
    }

    public MultiBlockStructure add(String structure) {
        MultiBlockStructure multiBlockStructure = new MultiBlockStructure();
        this.mapMultiBlocks.put(structure, multiBlockStructure);
        return multiBlockStructure;
    }

    public MultiBlockStructure add(String structure, MultiBlockStructure multiBlockStructure) {
        this.mapMultiBlocks.put(structure, multiBlockStructure);
        return multiBlockStructure;
    }

    public Map<String, MultiBlockStructure> getMapMultiBlocks() {
        return mapMultiBlocks;
    }

}
