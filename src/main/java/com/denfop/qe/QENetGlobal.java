package com.denfop.qe;


import com.denfop.api.qe.IQENet;
import com.denfop.api.qe.IQETile;
import com.denfop.api.qe.NodeQEStats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class QENetGlobal implements IQENet {

    private static Map<World, QENetLocal> worldToEnergyNetMap;

    static {
        QENetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static QENetGlobal initialize() {
        new EventHandler();
        return new QENetGlobal();
    }

    public static void onWorldUnload(World world) {
        final QENetLocal local = QENetGlobal.worldToEnergyNetMap.remove(world);
        if (local != null) {
            local.onUnload();
        }
    }

    public static QENetLocal getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        if (!QENetGlobal.worldToEnergyNetMap.containsKey(world)) {
            QENetGlobal.worldToEnergyNetMap.put(world, new QENetLocal(world));
        }
        return QENetGlobal.worldToEnergyNetMap.get(world);
    }


    public static void onTickEnd(final World world) {
        final QENetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public IQETile getTile(final World var1, final BlockPos var2) {
        final QENetLocal local = getForWorld(var1);
        if (local != null) {
            return local.getTileEntity(var2);
        }
        return null;
    }

    @Override
    public IQETile getSubTile(final World var1, final BlockPos var2) {
        return getTile(var1, var2);
    }

    @Override
    public <T extends TileEntity & IQETile> void addTile(final T var1) {
        final QENetLocal local = getForWorld(var1.getWorld());
        local.addTile(var1);
    }

    @Override
    public void removeTile(final IQETile var1) {
        final QENetLocal local = getForWorld(((TileEntity) var1).getWorld());
        local.removeTile(var1);
    }

    @Override
    public NodeQEStats getNodeStats(final IQETile var1, World world) {
        final QENetLocal local = getForWorld(world);
        if (local == null) {
            return new NodeQEStats(0.0, 0.0);
        }
        return local.getNodeStats(var1);
    }


}
