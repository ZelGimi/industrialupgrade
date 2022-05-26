package com.denfop.qe;


import com.denfop.api.qe.*;
import com.denfop.api.qe.IQETile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class QENetGlobal implements IQENet {

    public static Map<Chunk, List<IQETile>> heatTileinChunk;
    private static Map<World, QENetLocal> worldToEnergyNetMap;

    static {
        QENetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static QENetGlobal initialize() {
        new EventHandler();
        QENetLocal.list = new EnergyTransferList();
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

    public static void onTickStart(final World world) {
        final QENetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickStart();
        }
    }

    public static void onTickEnd(final World world) {
        final QENetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public IQETile getTile(final World var1, final BlockPos var2) {
        if (var1.getTileEntity(var2) instanceof IQETile) {
            return (IQETile) var1.getTileEntity(var2);
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
        final QENetLocal local = getForWorld(((TileEntity)var1).getWorld());
        local.removeTile(var1);
    }

    @Override
    public NodeQEStats getNodeStats(final IQETile var1,World world) {
        final QENetLocal local = getForWorld(world);
        if (local == null) {
            return new NodeQEStats(0.0, 0.0);
        }
        return local.getNodeStats(var1);
    }

    @Override
    public List<IQESink> getListQEInChunk(final Chunk chunk) {
        final QENetLocal local = getForWorld(chunk.getWorld());
        return local.getListQEInChunk(chunk);
    }

    @Override
    public void transferQEWireless(final IQEWirelessSource source) {
        final QENetLocal local = getForWorld(((TileEntity)source).getWorld());
        local.transferTemperatureWireless(source);
    }


}
