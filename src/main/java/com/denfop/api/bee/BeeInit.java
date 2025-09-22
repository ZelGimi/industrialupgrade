package com.denfop.api.bee;

import com.denfop.api.agriculture.CropInit;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.Arrays;

public class BeeInit {

    public static BeeBase PLAINS_BEE;
    public static BeeBase TROPICAL_BEE;
    public static BeeBase FOREST_BEE;
    public static BeeBase SWAMP_BEE;
    public static BeeBase WINTER_BEE;

    public static void init() {
        BeeNetwork.init();
        WINTER_BEE = new BeeBase(
                "winter",
                1,
                100,
                500,
                7500,
                new AxisAlignedBB(-8, -3, -8, 8, 3, 8),
                2,
                5,
                false,
                true,
                CropInit.raspberry,
                new ArrayList<>(),
                2, 0.05
        );
        SWAMP_BEE = new BeeBase(
                "swamp",
                5,
                110,
                395,
                6250,
                new AxisAlignedBB(-7, -3, -7, 7, 3, 7),
                2,
                8,
                false,
                true,
                CropInit.brown_mushroom,
                new ArrayList<>(), 1, 0.025
        );
        FOREST_BEE = new BeeBase(
                "forest",
                2,
                120,
                450,
                6875,
                new AxisAlignedBB(-8, -4, -8, 8, 4, 8),
                3,
                10,
                true,
                false,
                CropInit.poppy,
                new ArrayList<>(), 0, 0.03
        );

        TROPICAL_BEE = new BeeBase(
                "tropical",
                3,
                80,
                290,
                6875,
                new AxisAlignedBB(-7, -4, -7, 7, 4, 7),
                2,
                15,
                true,
                false,
                CropInit.reed_seed,
                new ArrayList<>(), 1, 0.01
        );

        PLAINS_BEE = new BeeBase(
                "plains",
                4,
                100,
                450,
                8750,
                new AxisAlignedBB(-8, -3, -8, 8, 3, 8),
                3,
                20,
                true,
                false,
                CropInit.dandelion,
                new ArrayList<>(), 0, 0.025
        );
        WINTER_BEE.setUnCompatibleBees(Arrays.asList(TROPICAL_BEE, SWAMP_BEE));
        FOREST_BEE.setUnCompatibleBees(Arrays.asList(PLAINS_BEE, SWAMP_BEE));
        PLAINS_BEE.setUnCompatibleBees(Arrays.asList(TROPICAL_BEE, FOREST_BEE));
        SWAMP_BEE.setUnCompatibleBees(Arrays.asList(FOREST_BEE, WINTER_BEE));
        TROPICAL_BEE.setUnCompatibleBees(Arrays.asList(WINTER_BEE, PLAINS_BEE));
        BeeAI.init();

    }

}
