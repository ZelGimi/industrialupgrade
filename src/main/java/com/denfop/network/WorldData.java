package com.denfop.network;

import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.world.IWorldTickCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.*;

public class WorldData {

    public static Map<ResourceKey<Level>, WorldData> idxClient = FMLEnvironment.dist.isClient()
            ? new HashMap<>()
            : null;
    public static Map<ResourceKey<Level>, WorldData> idxServer = new HashMap<>();
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
        } else {
            Map<ResourceKey<Level>, WorldData> index = getIndex(!world.isClientSide);
            WorldData ret = index.get(world.dimension());
            if (ret == null && load) {
                ret = new WorldData(world);
                WorldData prev = index.putIfAbsent(world.dimension(), ret);
                if (prev != null) {
                    ret = prev;
                }

            }
            return ret;
        }
    }

    public static void onWorldUnload(Level world) {
        getIndex(!world.isClientSide).remove(world.dimension());
    }

    private static Map<ResourceKey<Level>, WorldData> getIndex(boolean simulating) {
        return simulating ? idxServer : idxClient;
    }

    public Level getWorld() {
        return world;
    }

}
