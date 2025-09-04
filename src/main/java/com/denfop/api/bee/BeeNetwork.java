package com.denfop.api.bee;


import com.denfop.blockentity.bee.BlockEntityApiary;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeeNetwork {
    public static BeeNetwork instance;
    Map<Integer, Bee> beeMap = new HashMap<>();
    Map<ResourceKey<Level>, Map<ChunkPos, List<BlockEntityApiary>>> mapWorldApiary = new HashMap<>();

    public static void init() {
        if (instance == null) {
            instance = new BeeNetwork();
        }
    }

    public List<BlockEntityApiary> getApiaryFromChunk(Level world, ChunkPos chunkPos) {
        Map<ChunkPos, List<BlockEntityApiary>> map = mapWorldApiary.computeIfAbsent(world.dimension(), k -> new HashMap<>());
        return map.computeIfAbsent(chunkPos, k -> new ArrayList<>());
    }

    public void addNewApiaryToWorld(BlockEntityApiary apiary) {
        Map<ChunkPos, List<BlockEntityApiary>> map = mapWorldApiary.get(apiary.getWorld().dimension());
        if (map == null) {
            map = new HashMap<>();
            ChunkPos chunkPos = new ChunkPos(apiary.getPos());
            List<BlockEntityApiary> list = new ArrayList<>();
            list.add(apiary);
            map.put(chunkPos, list);
            mapWorldApiary.put(apiary.getWorld().dimension(), map);
        } else {
            ChunkPos chunkPos = new ChunkPos(apiary.getPos());
            List<BlockEntityApiary> list = map.get(chunkPos);
            if (list == null) {
                list = new ArrayList<>();
                list.add(apiary);
                map.put(chunkPos, list);
            } else {
                list.add(apiary);
            }
        }
    }

    public void removeApiaryFromWorld(BlockEntityApiary apiary) {
        Map<ChunkPos, List<BlockEntityApiary>> map = mapWorldApiary.get(apiary.getWorld().dimension());
        if (map != null) {
            ChunkPos chunkPos = new ChunkPos(apiary.getPos());
            List<BlockEntityApiary> list = map.get(chunkPos);
            if (list != null) {
                list.remove(apiary);
            }
        }
    }

    public Bee getBee(int id) {
        return beeMap.get(id);
    }

    public Map<Integer, Bee> getBeeMap() {
        return beeMap;
    }

    public void addBee(BeeBase beeBase) {
        this.beeMap.putIfAbsent(beeBase.getId(), beeBase);
    }


}
