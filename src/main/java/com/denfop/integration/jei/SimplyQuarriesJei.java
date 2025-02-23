package com.denfop.integration.jei;

import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.integration.jei.analyzer.AnalyzerCategory;
import com.simplequarries.BlockQuarry;
import mezz.jei.api.IModRegistry;

public class SimplyQuarriesJei {

    public static void init(IModRegistry registry) {
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockQuarry.simply_quarry),
                new AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockQuarry.adv_simply_quarry),
                new AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockQuarry.imp_simply_quarry),
                new AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockQuarry.per_simply_quarry),
                new AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
        registry.addRecipeCatalyst(
                JEICompat.getBlockStack(BlockQuarry.pho_simply_quarry),
                new AnalyzerCategory(registry.getJeiHelpers().getGuiHelper()).getUid()
        );
    }

}
