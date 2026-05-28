package com.denfop.api.otherenergies.transport;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;

public class TransportNetGlobal implements ITransportNet {

    private static final Map<ResourceKey<Level>, TransportNetLocal> worldToEnergyNetMap = new HashMap<>();
    public static TransportNetGlobal instance;

    public static TransportNetGlobal initialize() {
        new EventHandler();
        instance = new TransportNetGlobal();
        return instance;
    }

    public static void onWorldUnload(Level world) {
        final TransportNetLocal local = worldToEnergyNetMap.remove(world.dimension());
        if (local != null) {
            local.onUnload();
        }
    }

    public static TransportNetLocal getForWorld(final Level world) {
        if (world == null) {
            return null;
        }

        return worldToEnergyNetMap.computeIfAbsent(world.dimension(), key -> new TransportNetLocal(world));
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
        if (local != null) {
            local.addTile(var1);
        }
    }

    @Override
    public void removeTile(final ITransportTile var1) {
        final TransportNetLocal local = getForWorld(((BlockEntity) var1).getLevel());
        if (local != null) {
            local.removeTile(var1);
        }
    }
}