package com.denfop.api.bee.genetics;

import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.ICrop;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.denfop.api.agriculture.genetics.Genome.geneticBiomes;

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

