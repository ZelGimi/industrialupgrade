package com.denfop.api.otherenergies.heat;


import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.WeakHashMap;

public class HeatNetGlobal implements IHeatNet {

    private static Map<ResourceKey<Level>, HeatNetLocal> worldToEnergyNetMap;

    static {
        HeatNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static HeatNetGlobal initialize() {
        new EventHandler();
        return new HeatNetGlobal();
    }

    public static void onWorldUnload(Level world) {
        final HeatNetLocal local = HeatNetGlobal.worldToEnergyNetMap.remove(world.dimension());
        if (local != null) {
            local.onUnload();
        }
    }

    public static HeatNetLocal getForWorld(final Level world) {
        if (world == null) {
            return null;
        }
        if (!HeatNetGlobal.worldToEnergyNetMap.containsKey(world.dimension())) {
            HeatNetGlobal.worldToEnergyNetMap.put(world.dimension(), new HeatNetLocal());
        }
        return HeatNetGlobal.worldToEnergyNetMap.get(world.dimension());
    }


    public static void onTickEnd(final Level world) {
        final HeatNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public IHeatTile getSubTile(final Level var1, final BlockPos var2) {
        final HeatNetLocal local = getForWorld(var1);
        if (local != null) {
            return local.getTileEntity(var2);
        }
        return null;
    }


}
