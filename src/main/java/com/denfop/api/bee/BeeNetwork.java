package com.denfop.api.bee;


import com.denfop.tiles.bee.TileEntityApiary;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeeNetwork {
    public static BeeNetwork instance;
    Map<Integer, IBee> beeMap = new HashMap<>();
    Map<ResourceKey<Level>,Map<ChunkPos, List<TileEntityApiary>>> mapWorldApiary = new HashMap<>();

    public static void init() {
        if (instance == null) {
            instance = new BeeNetwork();
        }
    }
    public List<TileEntityApiary> getApiaryFromChunk(Level world, ChunkPos chunkPos){
        Map<ChunkPos, List<TileEntityApiary>> map = mapWorldApiary.computeIfAbsent(world.dimension(), k -> new HashMap<>());
        return map.computeIfAbsent(chunkPos,k -> new ArrayList<>());
    }
    public void addNewApiaryToWorld(TileEntityApiary apiary){
        Map<ChunkPos, List<TileEntityApiary>> map = mapWorldApiary.get(apiary.getWorld().dimension());
        if (map == null){
            map = new HashMap<>();
            ChunkPos chunkPos = new ChunkPos(apiary.getPos());
            List<TileEntityApiary> list = new ArrayList<>();
            list.add(apiary);
            map.put(chunkPos,list);
            mapWorldApiary.put(apiary.getWorld().dimension(),map);
        }else{
            ChunkPos chunkPos = new ChunkPos(apiary.getPos());
            List<TileEntityApiary> list = map.get(chunkPos);
            if (list == null){
                list = new ArrayList<>();
                list.add(apiary);
                map.put(chunkPos,list);
            }else{
                list.add(apiary);
            }
        }
    }
    public void removeApiaryFromWorld(TileEntityApiary apiary){
        Map<ChunkPos, List<TileEntityApiary>> map = mapWorldApiary.get(apiary.getWorld().dimension());
        if (map != null){
            ChunkPos chunkPos = new ChunkPos(apiary.getPos());
            List<TileEntityApiary> list = map.get(chunkPos);
            if (list != null){
                list.remove(apiary);
            }
        }
    }
    public IBee getBee(int id) {
        return beeMap.get(id);
    }

    public Map<Integer, IBee> getBeeMap() {
        return beeMap;
    }

    public void addBee(BeeBase beeBase) {
        this.beeMap.putIfAbsent(beeBase.getId(), beeBase);
    }


}
