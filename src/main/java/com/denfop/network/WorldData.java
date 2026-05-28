package com.denfop.network;

import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.world.IWorldTickCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.*;

public class WorldData {

    public static Map<ResourceKey<Level>, WorldData> idxClient = FMLEnvironment.dist.isClient()
            ? new HashMap<>()
            : null;
    public static Map<ResourceKey<Level>, WorldData> idxServer = new HashMap<>();
    private static final Set<ResourceKey<Level>> unloadingServer = new HashSet<>();
    private static final Set<ResourceKey<Level>> unloadingClient = FMLEnvironment.dist.isClient()
            ? new HashSet<>()
            : null;
    private static volatile boolean serverStopping;

    public final Queue<IWorldTickCallback> singleUpdates = new LinkedList<>();
    public final List<BlockEntityBase> listUpdateTile = new LinkedList<>();
    public final Map<BlockEntityBase, List<CustomPacketBuffer>> mapUpdateField = new HashMap<>();
    public final Map<BlockEntityBase, Map<Player, CustomPacketBuffer>> mapUpdateContainer = new HashMap<>();

    public final Map<BlockPos, BlockEntityBase> mapUpdateOvertimeField = new HashMap<>();
    private final Level world;

    public WorldData(Level world) {
        this.world = world;
    }

    public static WorldData get(Level world) {
        return get(world, true);
    }

    public static WorldData get(Level world, boolean load) {
        if (world == null) {
            return null;
        }

        boolean simulating = !world.isClientSide;
        Map<ResourceKey<Level>, WorldData> index = getIndex(simulating);
        if (index == null) {
            return null;
        }

        ResourceKey<Level> dimension = world.dimension();
        WorldData ret = index.get(dimension);
        Set<ResourceKey<Level>> unloadingIndex = getUnloadingIndex(simulating);
        boolean unloading = unloadingIndex != null && unloadingIndex.contains(dimension);
        if (ret == null && load && !unloading && !(simulating && serverStopping)) {
            ret = new WorldData(world);
            WorldData prev = index.putIfAbsent(dimension, ret);
            if (prev != null) {
                ret = prev;
            }
        }
        return ret;
    }

    public static void onWorldLoad(Level world) {
        if (world == null) {
            return;
        }
        boolean simulating = !world.isClientSide;
        if (simulating) {
            serverStopping = false;
        }
        Set<ResourceKey<Level>> unloadingIndex = getUnloadingIndex(simulating);
        if (unloadingIndex != null) {
            unloadingIndex.remove(world.dimension());
        }
    }

    public static void onWorldUnload(Level world) {
        if (world == null) {
            return;
        }

        boolean simulating = !world.isClientSide;
        ResourceKey<Level> dimension = world.dimension();
        Set<ResourceKey<Level>> unloadingIndex = getUnloadingIndex(simulating);
        if (unloadingIndex != null) {
            unloadingIndex.add(dimension);
        }

        Map<ResourceKey<Level>, WorldData> index = getIndex(simulating);
        if (index == null) {
            return;
        }

        WorldData data = index.remove(dimension);
        if (data != null) {
            data.clear();
        }
    }

    public static void onServerStopping() {
        serverStopping = true;
        clearIndex(idxServer);
        idxServer.clear();
    }

    public static void onServerStopped() {
        onServerStopping();
        unloadingServer.clear();
    }

    public static boolean isStoppingOrUnloading(Level world) {
        if (world == null) {
            return true;
        }
        boolean simulating = !world.isClientSide;
        if (simulating && serverStopping) {
            return true;
        }
        Set<ResourceKey<Level>> unloadingIndex = getUnloadingIndex(simulating);
        return unloadingIndex != null && unloadingIndex.contains(world.dimension());
    }

    private static void clearIndex(Map<ResourceKey<Level>, WorldData> index) {
        if (index == null) {
            return;
        }
        for (WorldData data : index.values()) {
            if (data != null) {
                data.clear();
            }
        }
    }

    private static Map<ResourceKey<Level>, WorldData> getIndex(boolean simulating) {
        return simulating ? idxServer : idxClient;
    }

    private static Set<ResourceKey<Level>> getUnloadingIndex(boolean simulating) {
        return simulating ? unloadingServer : unloadingClient;
    }

    private void clear() {
        singleUpdates.clear();
        listUpdateTile.clear();
        mapUpdateField.clear();
        mapUpdateContainer.clear();
        mapUpdateOvertimeField.clear();
    }

    public Level getWorld() {
        return world;
    }

}
