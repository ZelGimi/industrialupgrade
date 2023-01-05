package com.denfop.se;


import com.denfop.api.se.ISENet;
import com.denfop.api.se.ISETile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class SENetGlobal implements ISENet {

    private static Map<World, SENetLocal> worldToEnergyNetMap;

    static {
        SENetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static SENetGlobal initialize() {
        new EventHandler();
        return new SENetGlobal();
    }

    public static void onWorldUnload(World world) {
        final SENetLocal local = SENetGlobal.worldToEnergyNetMap.remove(world);
        if (local != null) {
            local.onUnload();
        }
    }

    public static SENetLocal getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        if (!SENetGlobal.worldToEnergyNetMap.containsKey(world)) {
            SENetGlobal.worldToEnergyNetMap.put(world, new SENetLocal(world));
        }
        return SENetGlobal.worldToEnergyNetMap.get(world);
    }


    public static void onTickEnd(final World world) {
        final SENetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public ISETile getTile(final World var1, final BlockPos var2) {
        final SENetLocal local = getForWorld(var1);
        if (local != null) {
            return local.getTileEntity(var2);
        }
        return null;
    }

    @Override
    public ISETile getSubTile(final World var1, final BlockPos var2) {
        return getTile(var1, var2);
    }

    @Override
    public <T extends TileEntity & ISETile> void addTile(final T var1) {
        final SENetLocal local = getForWorld(var1.getWorld());
        local.addTile(var1);
    }

    @Override
    public void removeTile(final ISETile var1) {
        final SENetLocal local = getForWorld(((TileEntity) var1).getWorld());
        local.removeTile(var1);
    }


}
