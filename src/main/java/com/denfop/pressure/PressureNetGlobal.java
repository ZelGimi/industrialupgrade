package com.denfop.pressure;


import com.denfop.api.pressure.IPressureNet;
import com.denfop.api.pressure.IPressureTile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.WeakHashMap;

public class PressureNetGlobal implements IPressureNet {

    private static Map<ResourceKey<Level>, PressureNetLocal> worldToEnergyNetMap;

    static {
        PressureNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static PressureNetGlobal initialize() {
        new EventHandler();
        return new PressureNetGlobal();
    }

    public static void onWorldUnload(Level world) {
        final PressureNetLocal local = PressureNetGlobal.worldToEnergyNetMap.remove(world.dimension());
        if (local != null) {
            local.onUnload();
        }
    }

    public static PressureNetLocal getForWorld(final Level world) {
        if (world == null) {
            return null;
        }
        if (!PressureNetGlobal.worldToEnergyNetMap.containsKey(world.dimension())) {
            PressureNetGlobal.worldToEnergyNetMap.put(world.dimension(), new PressureNetLocal());
        }
        return PressureNetGlobal.worldToEnergyNetMap.get(world.dimension());
    }


    public static void onTickEnd(final Level world) {
        final PressureNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public IPressureTile getSubTile(final Level var1, final BlockPos var2) {
        final PressureNetLocal local = getForWorld(var1);
        if (local != null) {
            return local.getSubTile(var1, var2);
        }
        return null;
    }


}
