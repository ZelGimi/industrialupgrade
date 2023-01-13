package com.denfop.integration.gc;

import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import net.minecraft.tileentity.TileEntity;

public class GCIntegration {

    public static boolean check(TileEntity tile) {

        return tile instanceof TileBaseUniversalElectrical;
    }

}
