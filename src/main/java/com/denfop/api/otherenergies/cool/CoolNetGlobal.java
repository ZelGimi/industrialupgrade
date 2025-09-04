package com.denfop.api.otherenergies.cool;


import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.WeakHashMap;

public class CoolNetGlobal implements ICoolNet {

    private static Map<ResourceKey<Level>, CoolNetLocal> worldToEnergyNetMap;

    static {
        CoolNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static CoolNetGlobal initialize() {
        new EventHandler();
        return new CoolNetGlobal();
    }

    public static void onWorldUnload(Level world) {
        final CoolNetLocal local = CoolNetGlobal.worldToEnergyNetMap.remove(world.dimension());
        if (local != null) {
            local.onUnload();
        }
    }

    public static CoolNetLocal getForWorld(final Level world) {
        if (world == null) {
            return null;
        }
        if (!CoolNetGlobal.worldToEnergyNetMap.containsKey(world.dimension())) {
            CoolNetGlobal.worldToEnergyNetMap.put(world.dimension(), new CoolNetLocal());
        }
        return CoolNetGlobal.worldToEnergyNetMap.get(world.dimension());
    }


    public static void onTickEnd(final Level world) {
        final CoolNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public ICoolTile getSubTile(final Level var1, final BlockPos var2) {
        final CoolNetLocal local = getForWorld(var1);
        if (local != null) {
            return local.getTileEntity(var2);
        }
        return null;
    }


}
