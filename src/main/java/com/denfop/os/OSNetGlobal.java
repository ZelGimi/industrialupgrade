package com.denfop.os;


import com.denfop.api.os.IOSNet;
import com.denfop.api.os.IOSTile;
import com.denfop.api.os.NodeOSStats;
import ic2.core.block.TileEntityBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class OSNetGlobal implements IOSNet {


    private static Map<World, OSNetLocal> worldToEnergyNetMap;

    static {
        OSNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static OSNetGlobal initialize() {
        new EventHandler();
        return new OSNetGlobal();
    }

    public static void onWorldUnload(World world) {
        final OSNetLocal local = OSNetGlobal.worldToEnergyNetMap.remove(world);
        if (local != null) {
            local.onUnload();
        }
    }

    public static OSNetLocal getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        if (!OSNetGlobal.worldToEnergyNetMap.containsKey(world)) {
            OSNetGlobal.worldToEnergyNetMap.put(world, new OSNetLocal(world));
        }
        return OSNetGlobal.worldToEnergyNetMap.get(world);
    }

    public static void onTickStart(final World world) {
        final OSNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickStart();
        }
    }

    public static void onTickEnd(final World world) {
        final OSNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public IOSTile getTile(final World var1, final BlockPos var2) {
        if (var1.getTileEntity(var2) instanceof IOSTile) {
            return (IOSTile) var1.getTileEntity(var2);
        }
        if (var1.getTileEntity(var2) instanceof TileEntityBlock) {
            TileEntityBlock tile = (TileEntityBlock) var1.getTileEntity(var2);
            assert tile != null;

        }
        return null;
    }

    @Override
    public IOSTile getSubTile(final World var1, final BlockPos var2) {
        return getTile(var1, var2);
    }

    @Override
    public <T extends TileEntity & IOSTile> void addTile(final T var1) {
        final OSNetLocal local = getForWorld(var1.getWorld());
        local.addTile(var1);
    }

    @Override
    public void removeTile(final IOSTile var1) {
        final OSNetLocal local = getForWorld(((TileEntity) var1).getWorld());
        local.removeTile(var1);
    }

    @Override
    public NodeOSStats getNodeStats(final IOSTile var1, World world) {
        final OSNetLocal local = getForWorld(world);
        if (local == null) {
            return new NodeOSStats(0.0, 0.0);
        }
        return local.getNodeStats(var1);
    }


}
