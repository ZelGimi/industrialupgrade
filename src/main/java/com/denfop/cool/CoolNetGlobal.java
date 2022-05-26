package com.denfop.cool;


import com.denfop.api.cooling.*;
import com.denfop.api.cooling.ICoolTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class CoolNetGlobal implements ICoolNet {

    public static Map<Chunk, List<ICoolTile>> heatTileinChunk;
    private static Map<World, CoolNetLocal> worldToEnergyNetMap;

    static {
        CoolNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static CoolNetGlobal initialize() {
        new EventHandler();
        CoolNetLocal.list = new EnergyTransferList();
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
            CoolNetGlobal.worldToEnergyNetMap.put(world, new CoolNetLocal(world));
        }
        return CoolNetGlobal.worldToEnergyNetMap.get(world);
    }

    public static void onTickStart(final World world) {
        final CoolNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickStart();
        }
    }

    public static void onTickEnd(final World world) {
        final CoolNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    @Override
    public ICoolTile getTile(final World var1, final BlockPos var2) {
        if (var1.getTileEntity(var2) instanceof ICoolTile) {
            return (ICoolTile) var1.getTileEntity(var2);
        }
        return null;
    }

    @Override
    public ICoolTile getSubTile(final World var1, final BlockPos var2) {
        return getTile(var1, var2);
    }

    @Override
    public <T extends TileEntity & ICoolTile> void addTile(final T var1) {
        final CoolNetLocal local = getForWorld(var1.getWorld());
        local.addTile(var1);
    }

    @Override
    public void removeTile(final ICoolTile var1) {
        final CoolNetLocal local = getForWorld(((TileEntity)var1).getWorld());
        local.removeTile(var1);
    }

    @Override
    public NodeCoolStats getNodeStats(final ICoolTile var1,World world) {
        final CoolNetLocal local = getForWorld(world);
        if (local == null) {
            return new NodeCoolStats(0.0, 0.0);
        }
        return local.getNodeStats(var1);
    }

    @Override
    public List<ICoolSink> getListHeatInChunk(final Chunk chunk) {
        final CoolNetLocal local = getForWorld(chunk.getWorld());
        return local.getListCoolInChunk(chunk);
    }

    @Override
    public void transferTemperatureWireless(final ICoolWirelessSource source) {
        final CoolNetLocal local = getForWorld(((TileEntity)source).getWorld());
        local.transferTemperatureWireless(source);
    }


}
