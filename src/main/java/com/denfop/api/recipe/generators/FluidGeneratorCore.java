package com.denfop.api.recipe.generators;

import com.denfop.blocks.FluidName;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.HashMap;
import java.util.Map;

public class FluidGeneratorCore {

    public static FluidGeneratorCore instance;
    public Map<TypeGenerator, FluidGenerator> fluidGeneratorMap = new HashMap<>();

    public FluidGeneratorCore() {
        instance = this;
        this.init();
    }

    private void init() {
        fluidGeneratorMap.put(TypeGenerator.MATTER, new FluidGenerator(FluidName.fluiduu_matter.getInstance(), 1, 1000000,
                TypeGenerators.SINK
        ));
        fluidGeneratorMap.put(TypeGenerator.NEUTRON, new FluidGenerator(FluidName.fluidNeutron.getInstance(), 1000, 162500000,
                TypeGenerators.SINK
        ));
        fluidGeneratorMap.put(TypeGenerator.GAS, new FluidGenerator(FluidName.fluidgas.getInstance(), 1000, 100000,
                TypeGenerators.SOURCE
        ));
        fluidGeneratorMap.put(TypeGenerator.GASOLINE, new FluidGenerator(FluidName.fluidbenz.getInstance(), 1000, 30000,
                TypeGenerators.SOURCE
        ));
        fluidGeneratorMap.put(TypeGenerator.DIESEL, new FluidGenerator(FluidName.fluiddizel.getInstance(), 1000, 60000,
                TypeGenerators.SOURCE
        ));
        fluidGeneratorMap.put(TypeGenerator.HELIUM, new FluidGenerator(FluidName.fluidHelium.getInstance(), 1000, 1000000,
                TypeGenerators.SINK
        ));
        fluidGeneratorMap.put(TypeGenerator.LAVA, new FluidGenerator(FluidRegistry.LAVA, 1000, 20000,
                TypeGenerators.SINK
        ));
        fluidGeneratorMap.put(TypeGenerator.WATER, new FluidGenerator(FluidRegistry.WATER, 1000, 40000,
                TypeGenerators.SINK
        ));
        fluidGeneratorMap.put(TypeGenerator.GEO, new FluidGenerator(FluidRegistry.LAVA, 1000, 10000,
                TypeGenerators.SOURCE
        ));
    }

}
