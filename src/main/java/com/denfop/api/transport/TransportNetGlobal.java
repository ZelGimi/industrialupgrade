package com.denfop.api.transport;


import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class TransportNetGlobal implements ITransportNet {


    public static TransportNetGlobal instance;
    private static Map<World, TransportNetLocal> worldToEnergyNetMap;

    static {
        TransportNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static TransportNetGlobal initialize() {
        new EventHandler();
        instance = new TransportNetGlobal();
        return instance;
    }

    public static void onWorldUnload(World world) {
        final TransportNetLocal local = TransportNetGlobal.worldToEnergyNetMap.remove(world);
        if (local != null) {
            local.onUnload();
        }
    }

    public static TransportNetLocal getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        if (!TransportNetGlobal.worldToEnergyNetMap.containsKey(world)) {
            TransportNetGlobal.worldToEnergyNetMap.put(world, new TransportNetLocal(world));
        }
        return TransportNetGlobal.worldToEnergyNetMap.get(world);
    }


    public static void onTickEnd(final World world) {
        final TransportNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }


    @Override
    public ITransportTile getSubTile(final World var1, final BlockPos var2) {
        final TransportNetLocal local = getForWorld(var1);
        if (local != null) {
            return local.getTileEntity(var2);
        }
        return null;
    }

    @Override
    public <T extends TileEntity & ITransportTile> void addTile(final T var1) {
        final TransportNetLocal local = getForWorld(var1.getWorld());
        local.addTile(var1);
    }

    @Override
    public void removeTile(final ITransportTile var1) {
        final TransportNetLocal local = getForWorld(((TileEntity) var1).getWorld());
        local.removeTile(var1);
    }

}
