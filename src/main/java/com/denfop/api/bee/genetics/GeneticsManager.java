package com.denfop.api.bee.genetics;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  GeneticsManager {

    public static GeneticsManager instance;

    public GeneticsManager() {

    }

    public static void init() {
        if (instance == null) {
            instance = new GeneticsManager();
        }
    }

    public GeneticsManager getInstance() {
        return instance;
    }




    public static Map<EnumGenetic, List<GeneticTraits>> enumGeneticListMap = new HashMap<>();
    public static Map<GeneticTraits,GeneticTraits> geneticTraitsMap = new HashMap<>();



}

