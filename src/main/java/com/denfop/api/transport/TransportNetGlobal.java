package com.denfop.api.transport;


import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Map;
import java.util.WeakHashMap;

public class TransportNetGlobal implements ITransportNet {


    public static TransportNetGlobal instance;
    private static Map<ResourceKey<Level>, TransportNetLocal> worldToEnergyNetMap;

    static {
        TransportNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static TransportNetGlobal initialize() {
        new EventHandler();
        instance = new TransportNetGlobal();
        return instance;
    }

    public static void onWorldUnload(net.minecraft.world.level.Level world) {
        final TransportNetLocal local = TransportNetGlobal.worldToEnergyNetMap.remove(world.dimension());
        if (local != null) {
            local.onUnload();
        }
    }

    public static TransportNetLocal getForWorld(final Level world) {
        if (world == null) {
            return null;
        }
        if (!TransportNetGlobal.worldToEnergyNetMap.containsKey(world.dimension())) {
            TransportNetGlobal.worldToEnergyNetMap.put(world.dimension(), new TransportNetLocal(world));
        }
        return TransportNetGlobal.worldToEnergyNetMap.get(world.dimension());
    }


    public static void onTickEnd(final Level world) {
        final TransportNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }


    @Override
    public ITransportTile getSubTile(final Level var1, final BlockPos var2) {
        final TransportNetLocal local = getForWorld(var1);
        if (local != null) {
            return local.getTileEntity(var2);
        }
        return null;
    }

    @Override
    public <T extends BlockEntity & ITransportTile> void addTile(final T var1) {
        final TransportNetLocal local = getForWorld(var1.getLevel());
        local.addTile(var1);
    }

    @Override
    public void removeTile(final ITransportTile var1) {
        final TransportNetLocal local = getForWorld(((BlockEntity) var1).getLevel());
        local.removeTile(var1);
    }

}
