package com.denfop.api.crop;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.crop.genetics.GeneticsManager;
import com.denfop.api.crop.genetics.Genome;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.blockentity.crop.TileEntityCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CropNetwork {

    public static CropNetwork instance;
    Map<Integer, Crop> cropMap = new HashMap<>();

    Map<ResourceKey<Level>, Map<ChunkPos, List<TileEntityCrop>>> mapWorldCrop = new HashMap<>();

    public static void init() {
        if (instance == null) {
            instance = new CropNetwork();
        }
    }

    public List<TileEntityCrop> getCropsFromChunk(Level world, ChunkPos chunkPos) {
        Map<ChunkPos, List<TileEntityCrop>> map = mapWorldCrop.computeIfAbsent(world.dimension(), k -> new HashMap<>());
        return map.computeIfAbsent(chunkPos, k -> new ArrayList<>());
    }

    public void addNewCropToWorld(TileEntityCrop crop) {
        Map<ChunkPos, List<TileEntityCrop>> map = mapWorldCrop.get(crop.getWorld().dimension());
        if (map == null) {
            map = new HashMap<>();
            ChunkPos chunkPos = new ChunkPos(crop.getPos());
            List<TileEntityCrop> list = new ArrayList<>();
            list.add(crop);
            map.put(chunkPos, list);
            mapWorldCrop.put(crop.getWorld().dimension(), map);
        } else {
            ChunkPos chunkPos = new ChunkPos(crop.getPos());
            List<TileEntityCrop> list = map.get(chunkPos);
            if (list == null) {
                list = new ArrayList<>();
                list.add(crop);
                map.put(chunkPos, list);
            } else {
                list.add(crop);
            }
        }
    }

    public void removeCropFromWorld(TileEntityCrop crop) {
        Map<ChunkPos, List<TileEntityCrop>> map = mapWorldCrop.get(crop.getWorld().dimension());
        if (map != null) {
            ChunkPos chunkPos = new ChunkPos(crop.getPos());
            List<TileEntityCrop> list = map.get(chunkPos);
            if (list != null) {
                list.remove(crop);
            }
        }
    }

    public CropNetwork getInstance() {
        return instance;
    }

    public Map<Integer, Crop> getCropMap() {
        return cropMap;
    }

    public Crop getCropFromStack(ItemStack stack, Genome genome) {
        if (stack.getItem() instanceof CropItem) {
            Crop crop = ((CropItem) stack.getItem()).getCrop(stack.getDamageValue(), stack).copy();
            GeneticsManager.instance.loadGenomeToCrop(genome, crop);
            return crop;
        } else {
            return null;
        }
    }


    public Crop getCropFromStack(ItemStack stack) {
        if (stack.getItem() instanceof CropItem) {
            return ((CropItem) stack.getItem()).getCrop(stack.getDamageValue(), stack);
        } else {
            return IUCore.cropMap.get(stack.getItem());
        }
    }

    public Crop getCrop(int id) {
        return cropMap.get(id);
    }


    public Crop canCropCombine(List<Crop> crops) {
        for (Crop crop : cropMap.values()) {
            if (crop.isCombine() && crop.isCombineWithCrops(crops)) {
                return crop.copy();
            }
        }

        return null;
    }

    public boolean canPlantCrop(ItemStack stack, Level world, BlockPos pos, BlockState downBlock, Biome biome) {
        Crop crop = getCropFromStack(stack);
        if (crop == null) {
            return false;
        }
        if (crop.isIgnoreSoil() || ((crop.getSoil().getState() == downBlock && !crop.getSoil().isIgnore()) || (crop
                .getSoil()
                .getBlock() == downBlock.getBlock() && crop.getSoil().isIgnore())) || (crop.getSoil() == EnumSoil.FARMLAND && downBlock.getBlock() == IUItem.humus.getBlock(0))) {
            return true;
        }
        return false;
    }

    public void addCrop(Crop cropBase) {
        this.cropMap.putIfAbsent(cropBase.getId(), cropBase);
    }

    public boolean canGrow(
            Level world, BlockPos pos, ChunkPos chunkPos, Crop crop, final Radiation radLevel,
            final ChunkAccess chunk, Biome biome, final ChunkLevel chunkLevel
    ) {
        if ((radLevel == null || radLevel.getLevel().ordinal() <= crop.getRadiationRequirements().ordinal()) && isWaterNearby(world
                , pos,
                crop)) {
            int light = (int) (world.getRawBrightness(pos, 0));
            if (light >= crop.getLightLevel()) {
                final ChunkLevel air = PollutionManager.pollutionManager.getChunkLevelAir(chunkPos);
                if ((air == null || air.getLevelPollution().ordinal() <= crop
                        .getAirRequirements()
                        .ordinal()) && (chunkLevel == null || chunkLevel.getLevelPollution().ordinal() <= crop
                        .getSoilRequirements()
                        .ordinal())) {
                    final boolean day = world.isDay();
                    if ((day && crop.isSun()) || (!day && crop.isNight())) {
                        boolean rain = world.isRaining();
                        boolean thundering = world.isThundering();
                        if ((rain && crop.getWeatherResistance() >= 1) || (thundering && crop.getWeatherResistance() >= 2) || (!rain && !thundering)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean canMultiGrow(
            Level world, BlockPos pos, ChunkPos chunkPos, Crop crop, final Radiation radLevel,
            final ChunkAccess chunk, Biome biome, final ChunkLevel chunkLevel
    ) {
        if (radLevel.getLevel().ordinal() <= crop.getRadiationRequirements().ordinal()) {
            int light = (int) (world.getRawBrightness(pos, 0));
            if (light >= crop.getLightLevel()) {
                final ChunkLevel air = PollutionManager.pollutionManager.getChunkLevelAir(chunkPos);
                if ((air == null || air.getLevelPollution().ordinal() <= crop
                        .getAirRequirements()
                        .ordinal()) && (chunkLevel == null || chunkLevel.getLevelPollution().ordinal() <= crop
                        .getSoilRequirements()
                        .ordinal())) {
                    final boolean day = world.isDay();
                    if ((day && crop.isSun()) || (!day && crop.isNight())) {
                        boolean rain = world.isRaining();
                        boolean thundering = world.isThundering();
                        if ((rain && crop.getWeatherResistance() >= 1) || (thundering && crop.getWeatherResistance() >= 2) || (!rain && !thundering)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isWaterNearby(Level world, BlockPos pos, Crop crop) {
        if (crop.getWaterRequirement() == 0)
            return true;
        int radius = crop.getWaterRequirement() + 1;


        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos checkPos = pos.offset(x, -1, z);
                BlockState state = world.getBlockState(checkPos);
                if (state.getBlock() == Blocks.WATER) {
                    return true;
                }
            }
        }

        return false;
    }

}
