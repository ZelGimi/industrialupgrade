package com.denfop.cool;


import com.denfop.api.cool.ICoolNet;
import com.denfop.api.cool.ICoolTile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class CoolNetGlobal implements ICoolNet {

    private static Map<World, CoolNetLocal> worldToEnergyNetMap;

    static {
        CoolNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static CoolNetGlobal initialize() {
        new EventHandler();
        return new CoolNetGlobal();
    }

    public static void onWorldUnload(World world) {
        final CoolNetLocal local = CoolNetGlobal.worldToEnergyNetMap.remove(world);
        if (local != null) {
            local.onUnload();
        }
    }

    public static CoolNetLocal getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        if (!CoolNetGlobal.worldToEnergyNetMap.containsKey(world)) {
            CoolNetGlobal.worldToEnergyNetMap.put(world, new CoolNetLocal());
        }
        return CoolNetGlobal.worldToEnergyNetMap.get(world);
    }


    public static void onTickEnd(final World world) {
        final CoolNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public ICoolTile getSubTile(final World var1, final BlockPos var2) {
        final CoolNetLocal local = getForWorld(var1);
        if (local != null) {
            return local.getTileEntity(var2);
        }
        return null;
    }


}
