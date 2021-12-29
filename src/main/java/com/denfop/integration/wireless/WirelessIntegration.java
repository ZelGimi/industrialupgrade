package com.denfop.integration.wireless;

import ic2.core.block.TeBlockRegistry;
import net.minecraft.block.Block;
import ru.wirelesstools.tileentities.wireless.receivers.TEWSB;

public class WirelessIntegration {

    public static Block panel;

    public static void init() {
        panel = TeBlockRegistry.get(TEWSB.IDENTITY);

    }

}
