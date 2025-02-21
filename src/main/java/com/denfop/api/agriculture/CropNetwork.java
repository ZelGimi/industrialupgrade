package com.denfop.api.agriculture;

import com.denfop.IUItem;
import com.denfop.api.agriculture.genetics.GeneticsManager;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.api.pollution.ChunkLevel;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.tiles.crop.TileEntityCrop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CropNetwork {

    public static CropNetwork instance;
    Map<Integer, ICrop> cropMap = new HashMap<>();

    Map<Integer, Map<ChunkPos, List<TileEntityCrop>>> mapWorldCrop = new HashMap<>();

    public static void init() {
        if (instance == null) {
            instance = new CropNetwork();
        }
    }

    public List<TileEntityCrop> getCropsFromChunk(World world, ChunkPos chunkPos) {
        Map<ChunkPos, List<TileEntityCrop>> map = mapWorldCrop.computeIfAbsent(world.provider.getDimension(), k -> new HashMap<>());
        return map.computeIfAbsent(chunkPos,k -> new ArrayList<>());
    }

    public void addNewCropToWorld(TileEntityCrop crop) {
        Map<ChunkPos, List<TileEntityCrop>> map = mapWorldCrop.get(crop.getWorld().provider.getDimension());
        if (map == null) {
            map = new HashMap<>();
            ChunkPos chunkPos = new ChunkPos(crop.getPos());
            List<TileEntityCrop> list = new ArrayList<>();
            list.add(crop);
            map.put(chunkPos, list);
            mapWorldCrop.put(crop.getWorld().provider.getDimension(), map);
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
        Map<ChunkPos, List<TileEntityCrop>> map = mapWorldCrop.get(crop.getWorld().provider.getDimension());
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

    public Map<Integer, ICrop> getCropMap() {
        return cropMap;
    }

    public ICrop getCropFromStack(ItemStack stack, Genome genome) {
        if (stack.getItem() instanceof ICropItem) {
            ICrop crop = ((ICropItem) stack.getItem()).getCrop(stack.getItemDamage(), stack).copy();
            GeneticsManager.instance.loadGenomeToCrop(genome, crop);
            return crop;
        } else {
            return null;
        }
    }


    public ICrop getCropFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ICropItem) {
            return ((ICropItem) stack.getItem()).getCrop(stack.getItemDamage(), stack);
        } else {
            return null;
        }
    }

    public ICrop getCrop(int id) {
        return cropMap.get(id);
    }


    public ICrop canCropCombine(List<ICrop> crops) {
        for (ICrop crop : cropMap.values()) {
            if (crop.isCombine() && crop.isCombineWithCrops(crops)) {
                return crop.copy();
            }
        }

        return null;
    }

    public boolean canPlantCrop(ItemStack stack, World world, BlockPos pos, IBlockState downBlock, Biome biome) {
        ICrop crop = getCropFromStack(stack);
        if (crop == null) {
            return false;
        }
        if (crop.isIgnoreSoil() || ((crop.getSoil().getState() == downBlock && !crop.getSoil().isIgnore()) || (crop
                .getSoil()
                .getBlock() == downBlock.getBlock() && crop.getSoil().isIgnore()) || (crop.getSoil() == EnumSoil.FARMLAND && downBlock.getBlock() == IUItem.humus))) {
            return crop.canGrowInBiome(biome);
        }
        return false;
    }

    public void addCrop(ICrop cropBase) {
        this.cropMap.putIfAbsent(cropBase.getId(), cropBase);
    }

    public boolean canGrow(
            World world, BlockPos pos, ChunkPos chunkPos, ICrop crop, final Radiation radLevel,
            final Chunk chunk, Biome biome, final ChunkLevel chunkLevel
    ) {
        if (radLevel.getLevel().ordinal() <= crop.getRadiationRequirements().ordinal() && isWaterNearby(world, pos, crop)) {
            int light = chunk.getLightSubtracted(pos, 0);
            if (light >= crop.getLightLevel()) {
                final ChunkLevel air = PollutionManager.pollutionManager.getChunkLevelAir(chunkPos);
                if ((air == null || air.getLevelPollution().ordinal() <= crop
                        .getAirRequirements()
                        .ordinal()) && (chunkLevel == null || chunkLevel.getLevelPollution().ordinal() <= crop
                        .getSoilRequirements()
                        .ordinal())) {
                    final boolean day = world.isDaytime();
                    if ((day && crop.isSun()) || (!day && crop.isNight())) {
                        boolean rain = world.isRaining();
                        boolean thundering = world.isThundering();
                        if ((rain && crop.getWeatherResistance() >= 1) || (thundering && crop.getWeatherResistance() >= 2) || (!rain && !thundering)) {
                            return crop.canGrowInBiome(biome);
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean canMultiGrow(
            World world, BlockPos pos, ChunkPos chunkPos, ICrop crop, final Radiation radLevel,
            final Chunk chunk, Biome biome, final ChunkLevel chunkLevel
    ) {
        if (radLevel.getLevel().ordinal() <= crop.getRadiationRequirements().ordinal()) {
            int light = chunk.getLightSubtracted(pos, 0);
            if (light >= crop.getLightLevel()) {
                final ChunkLevel air = PollutionManager.pollutionManager.getChunkLevelAir(chunkPos);
                if ((air == null || air.getLevelPollution().ordinal() <= crop
                        .getAirRequirements()
                        .ordinal()) && (chunkLevel == null || chunkLevel.getLevelPollution().ordinal() <= crop
                        .getSoilRequirements()
                        .ordinal())) {
                    final boolean day = world.isDaytime();
                    if ((day && crop.isSun()) || (!day && crop.isNight())) {
                        boolean rain = world.isRaining();
                        boolean thundering = world.isThundering();
                        if ((rain && crop.getWeatherResistance() >= 1) || (thundering && crop.getWeatherResistance() >= 2) || (!rain && !thundering)) {
                            return crop.canGrowInBiome(biome);
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean isWaterNearby(World world, BlockPos pos, ICrop crop) {
        if (crop.getWaterRequirement() == 0)
            return true;
        int radius = crop.getWaterRequirement()+1;


        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos checkPos = pos.add(x, -1, z);
                IBlockState state = world.getBlockState(checkPos);
                if (state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) {
                    return true;
                }
            }
        }

        return false;
    }

}
