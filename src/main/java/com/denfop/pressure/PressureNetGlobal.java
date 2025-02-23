package com.denfop.pressure;


import com.denfop.api.pressure.IPressureNet;
import com.denfop.api.pressure.IPressureTile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class PressureNetGlobal implements IPressureNet {

    private static Map<World, PressureNetLocal> worldToEnergyNetMap;

    static {
        PressureNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static PressureNetGlobal initialize() {
        new EventHandler();
        return new PressureNetGlobal();
    }

    public static void onWorldUnload(World world) {
        final PressureNetLocal local = PressureNetGlobal.worldToEnergyNetMap.remove(world);
        if (local != null) {
            local.onUnload();
        }
    }

    public static PressureNetLocal getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        if (!PressureNetGlobal.worldToEnergyNetMap.containsKey(world)) {
            PressureNetGlobal.worldToEnergyNetMap.put(world, new PressureNetLocal());
        }
        return PressureNetGlobal.worldToEnergyNetMap.get(world);
    }


    public static void onTickEnd(final World world) {
        final PressureNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public IPressureTile getSubTile(final World var1, final BlockPos var2) {
        final PressureNetLocal local = getForWorld(var1);
        if (local != null) {
            return local.getTileEntity(var2);
        }
        return null;
    }


}
