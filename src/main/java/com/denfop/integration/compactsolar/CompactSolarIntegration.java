package com.denfop.integration.compactsolar;

import cpw.mods.compactsolars.CompactSolars;
import net.minecraft.block.Block;

public class CompactSolarIntegration {
    public static Block solar;

    public static void init() {
        solar = CompactSolars.compactSolarBlock;
    }
}
