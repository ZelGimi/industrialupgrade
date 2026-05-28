package com.denfop.api.vein.common;

import com.denfop.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.common.Tags;

import java.util.*;

public class VeinSystem implements VeinBaseSystem {

    public static VeinBaseSystem system;
    public static VeinBase EMPTY = new VeinBase(Type.EMPTY, 0, new ChunkPos(999999999, 999999999));
    private final Random rand;
    List<VeinBase> list;
    List<ChunkPos> chunkPos;
    Map<ChunkPos, VeinBase> chunkPosVeinMap;

    public VeinSystem() {
        system = this;
        this.list = new ArrayList<>();
        this.chunkPos = new ArrayList<>();
        this.chunkPosVeinMap = new HashMap<>();
        this.rand = new Random();
    }

    public VeinBase getEMPTY() {
        return EMPTY;
    }

    public List<ChunkPos> getChunkPos() {
        return chunkPos;
    }

    @Override
    public List<VeinBase> getVeinsList() {
        return this.list;
    }

    @Override
    public void addVein(final LevelChunk chunk) {
        int chance = rand.nextInt(100);
        VeinBase vein = new VeinBase(Type.EMPTY, 0, chunk.getPos());
        Holder<Biome> holder = chunk.getLevel().getBiome(new BlockPos(chunk.getPos().x * 16, 0, chunk.getPos().z * 16));
        int hillBonus = holder.is(BiomeTags.IS_HILL)
                ? ModConfig.COMMON.hillChanceBonus.get()
                : 0;

        if (chance >= ModConfig.COMMON.baseChance.get() + hillBonus) {

            int biomePenalty = (holder.is(BiomeTags.IS_TAIGA) || holder.is(Tags.Biomes.IS_SNOWY))
                    ? ModConfig.COMMON.taigaSnowyChancePenalty.get()
                    : 0;

            if (rand.nextInt(100) < ModConfig.COMMON.gasRollChance.get() - biomePenalty) {
                getnumber(vein, holder);
            } else {
                vein.setType(Type.GAS);
                vein.setOldMineral(false);
                vein.setMeta(0);
                vein.setMaxCol(ModConfig.COMMON.gasVeinMaxCol.get());
                vein.setCol(ModConfig.COMMON.gasVeinMaxCol.get());
            }
        } else {
            int meta = rand.nextInt(30);
            vein.setType(Type.VEIN);
            vein.setOldMineral(meta <= 15);
            vein.setMeta(meta % 16);
            vein.setMaxCol(ModConfig.COMMON.maxVein.get());
            vein.setCol(ModConfig.COMMON.maxVein.get());
        }
        this.list.add(vein);
        this.chunkPos.add(vein.getChunk());
        this.chunkPosVeinMap.put(vein.getChunk(), vein);
    }

    @Override
    public void addVein(final CompoundTag tag) {
        VeinBase vein = new VeinBase(tag);
        if (!this.list.contains(vein)) {
            this.list.add(vein);
            this.chunkPos.add(vein.getChunk());
            this.chunkPosVeinMap.put(vein.getChunk(), vein);
        }
    }

    @Override
    public VeinBase getVein(final ChunkPos pos) {
        return this.chunkPosVeinMap.getOrDefault(pos, EMPTY);
    }

    @Override
    public void unload() {
        this.list.clear();
        this.chunkPos.clear();
        this.chunkPosVeinMap.clear();
    }

    private void applyOilVein(VeinBase vein, int chance, int min, int max, int meta) {
        int roll = rand.nextInt(100);

        if (roll < chance) {
            int number = rand.nextInt(max) + min;
            vein.setCol(number);
            vein.setMaxCol(number);
            vein.setType(Type.OIL);
            vein.setOldMineral(true);
            vein.setMeta(meta);
        } else {
            vein.setType(Type.EMPTY);
            vein.setCol(0);
            vein.setMaxCol(0);
        }
    }

    private void getnumber(VeinBase vein, final Holder<Biome> biome) {
        int meta = rand.nextInt(6);
        if (biome.is(Tags.Biomes.IS_DESERT)) {
            applyOilVein(vein,
                    ModConfig.COMMON.desertChance.get(),
                    ModConfig.COMMON.desertMin.get(),
                    ModConfig.COMMON.desertMax.get(),
                    meta);

        } else if (biome.is(BiomeTags.IS_OCEAN)) {
            applyOilVein(vein,
                    ModConfig.COMMON.oceanChance.get(),
                    ModConfig.COMMON.oceanMin.get(),
                    ModConfig.COMMON.oceanMax.get(),
                    meta);

        } else if (biome.is(BiomeTags.IS_DEEP_OCEAN)) {
            applyOilVein(vein,
                    ModConfig.COMMON.deepOceanChance.get(),
                    ModConfig.COMMON.deepOceanMin.get(),
                    ModConfig.COMMON.deepOceanMax.get(),
                    meta);

        } else if (biome.is(BiomeTags.IS_RIVER)) {
            applyOilVein(vein,
                    ModConfig.COMMON.riverChance.get(),
                    ModConfig.COMMON.riverMin.get(),
                    ModConfig.COMMON.riverMax.get(),
                    meta);

        } else if (biome.is(BiomeTags.IS_SAVANNA)) {
            applyOilVein(vein,
                    ModConfig.COMMON.savannaChance.get(),
                    ModConfig.COMMON.savannaMin.get(),
                    ModConfig.COMMON.savannaMax.get(),
                    meta);

        } else {
            applyOilVein(vein,
                    ModConfig.COMMON.defaultChance.get(),
                    ModConfig.COMMON.defaultMin.get(),
                    ModConfig.COMMON.defaultMax.get(),
                    meta);
        }
    }

}
