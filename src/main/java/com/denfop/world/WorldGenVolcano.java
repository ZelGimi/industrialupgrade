package com.denfop.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WorldGenVolcano extends WorldGenerator {

    public static List<GeneratorVolcano> generatorVolcanoList = new LinkedList<>();

    @Override
    public boolean generate(final World world, final Random rand, final BlockPos position1) {
        generatorVolcanoList.add(new GeneratorVolcano(world, position1));

        return true;
    }

}
