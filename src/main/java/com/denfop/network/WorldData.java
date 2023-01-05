package com.denfop.network;

import ic2.core.IWorldTickCallback;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class WorldData {

    public static ConcurrentMap<Integer, WorldData> idxClient = FMLCommonHandler.instance().getSide().isClient()
            ? new ConcurrentHashMap<>()
            : null;
    public static ConcurrentMap<Integer, WorldData> idxServer = new ConcurrentHashMap<>();
    public final Queue<IWorldTickCallback> singleUpdates = new ConcurrentLinkedQueue<>();
    public final Set<IWorldTickCallback> continuousUpdates = new HashSet<>();
    public final List<IWorldTickCallback> continuousUpdatesToAdd = new ArrayList<>();
    public final List<IWorldTickCallback> continuousUpdatesToRemove = new ArrayList<>();
    public final Map<TileEntity, TeUpdateDataServer> tesToUpdate = new IdentityHashMap<>();
    public final Map<Chunk, NBTTagCompound> worldGenData = new IdentityHashMap<>();
    public final Set<Chunk> chunksToDecorate = Collections.newSetFromMap(new IdentityHashMap<>());
    public final Set<Chunk> pendingUnloadChunks = Collections.newSetFromMap(new IdentityHashMap<>());
    public boolean continuousUpdatesInUse = false;

    private WorldData(World world) {


    }

    public static WorldData get(World world) {
        return get(world, true);
    }

    public static WorldData get(World world, boolean load) {
        if (world == null) {
            throw new IllegalArgumentException("world is null");
        } else {
            ConcurrentMap<Integer, WorldData> index = getIndex(!world.isRemote);
            WorldData ret = index.get(world.provider.getDimension());
            if (ret == null && load) {
                ret = new WorldData(world);
                WorldData prev = index.putIfAbsent(world.provider.getDimension(), ret);
                if (prev != null) {
                    ret = prev;
                }

            }
            return ret;
        }
    }

    public static void onWorldUnload(World world) {
        getIndex(!world.isRemote).remove(world.provider.getDimension());
    }

    private static ConcurrentMap<Integer, WorldData> getIndex(boolean simulating) {
        return simulating ? idxServer : idxClient;
    }

}
