package com.denfop.api.bee;

import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.ICrop;
import com.denfop.tiles.bee.TileEntityApiary;
import com.denfop.tiles.crop.TileEntityCrop;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeeNetwork {
    public static BeeNetwork instance;
    Map<Integer, IBee> beeMap = new HashMap<>();
    Map<Integer,Map<ChunkPos, List<TileEntityApiary>>> mapWorldApiary = new HashMap<>();

    public static void init() {
        if (instance == null) {
            instance = new BeeNetwork();
        }
    }
    public List<TileEntityApiary> getApiaryFromChunk(World world, ChunkPos chunkPos){
        Map<ChunkPos, List<TileEntityApiary>> map = mapWorldApiary.computeIfAbsent(world.provider.getDimension(), k -> new HashMap<>());
        return map.computeIfAbsent(chunkPos,k -> new ArrayList<>());
    }
    public void addNewApiaryToWorld(TileEntityApiary apiary){
        Map<ChunkPos, List<TileEntityApiary>> map = mapWorldApiary.get(apiary.getWorld().provider.getDimension());
        if (map == null){
            map = new HashMap<>();
            ChunkPos chunkPos = new ChunkPos(apiary.getPos());
            List<TileEntityApiary> list = new ArrayList<>();
            list.add(apiary);
            map.put(chunkPos,list);
            mapWorldApiary.put(apiary.getWorld().provider.getDimension(),map);
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
        Map<ChunkPos, List<TileEntityApiary>> map = mapWorldApiary.get(apiary.getWorld().provider.getDimension());
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
