package com.denfop.network;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.world.IWorldTickCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class  WorldData {

    public static Map<Integer, WorldData> idxClient = FMLCommonHandler.instance().getSide().isClient()
            ? new HashMap<>()
            : null;
    public static Map<Integer, WorldData> idxServer = new HashMap<>();
    public final Queue<IWorldTickCallback> singleUpdates = new LinkedList<>();
    public final Map<TileEntityBlock, Map<EntityPlayer, CustomPacketBuffer>> mapUpdateContainer = new HashMap<>();

    public final List<TileEntityBlock> listUpdateTile = new ArrayList<>();
    public final Map<TileEntityBlock, List<CustomPacketBuffer>> mapUpdateField = new HashMap<>();

    public final Map<BlockPos, TileEntityBlock> mapUpdateOvertimeField = new HashMap<>();
    private final World world;

    public WorldData(World world) {
        this.world = world;
    }

    public static WorldData get(World world) {
        return get(world, true);
    }

    public static WorldData get(World world, boolean load) {
        Map<Integer, WorldData> index = getIndex(!world.isRemote);
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

    public static void onWorldUnload(World world) {
        getIndex(!world.isRemote).remove(world.provider.getDimension());
    }

    private static Map<Integer, WorldData> getIndex(boolean simulating) {
        return simulating ? idxServer : idxClient;
    }

    public World getWorld() {
        return world;
    }

}
