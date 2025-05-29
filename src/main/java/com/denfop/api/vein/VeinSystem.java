package com.denfop.api.vein;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;

import java.util.*;

public class VeinSystem implements IVeinSystem {

    public static IVeinSystem system;
    public static Vein EMPTY = new Vein(Type.EMPTY, 0, new ChunkPos(999999999, 999999999));
    private final Random rand;
    List<Vein> list;
    List<ChunkPos> chunkPos;
    Map<ChunkPos, Vein> chunkPosVeinMap;

    public VeinSystem() {
        system = this;
        this.list = new ArrayList<>();
        this.chunkPos = new ArrayList<>();
        this.chunkPosVeinMap = new HashMap<>();
        this.rand = new Random();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public Vein getEMPTY() {
        return EMPTY;
    }

    public List<ChunkPos> getChunkPos() {
        return chunkPos;
    }

    @Override
    public List<Vein> getVeinsList() {
        return this.list;
    }

    @Override
    public void addVein(final LevelChunk chunk) {
        Random rand = new Random();
        int chance = rand.nextInt(100);
        Vein vein = new Vein(Type.EMPTY, 0, chunk.getPos());
        Holder<Biome> holder = chunk.getLevel().getBiome(new BlockPos(chunk.getPos().x * 16, 0, chunk.getPos().z * 16));
        int col = holder.containsTag(BiomeTags.IS_HILL) ? 25 : 0;
        if (chance >= 15 + col) {
            col = (holder.containsTag(BiomeTags.IS_TAIGA) || holder.containsTag(Tags.Biomes.IS_SNOWY)) ? 15 : 0;
            if (rand.nextInt(100) < 85 - col) {
                getnumber(vein, holder);
            } else {
                vein.setType(Type.GAS);
                vein.setOldMineral(false);
                vein.setMeta(0);
                vein.setMaxCol(450000);
                vein.setCol(450000);
            }
            getnumber(vein, holder);

        } else {
            int meta = rand.nextInt(16);
            vein.setType(Type.VEIN);
            vein.setOldMineral(meta <= 15);
            vein.setMeta(meta);
            vein.setMaxCol(30000);
            vein.setCol(30000);
        }
        this.list.add(vein);
        this.chunkPos.add(vein.getChunk());
        this.chunkPosVeinMap.put(vein.getChunk(), vein);
    }

    @Override
    public void addVein(final CompoundTag tag) {
        Vein vein = new Vein(tag);
        if (!this.list.contains(vein)) {
            this.list.add(vein);
            this.chunkPos.add(vein.getChunk());
            this.chunkPosVeinMap.put(vein.getChunk(), vein);
        }
    }

    @Override
    public Vein getVein(final ChunkPos pos) {
        return this.chunkPosVeinMap.getOrDefault(pos, EMPTY);
    }

    @Override
    public void unload() {
        this.list.clear();
        this.chunkPos.clear();
        this.chunkPosVeinMap.clear();
    }

    private void getnumber(Vein vein, final Holder<Biome> biome) {
        int number;

        rand.setSeed(rand.nextLong());
        int meta = rand.nextInt(6);
        if (biome.is(Tags.Biomes.IS_DESERT)) {
            int random = rand.nextInt(100);
            if (random >= 35) {
                number = rand.nextInt(500000) + 150000;
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
        } else if (biome.is(BiomeTags.IS_OCEAN)) {
            int random;
            random = rand.nextInt(100);
            if (random >= 40) {
                number = rand.nextInt(500000) + 150000;
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
        } else if (biome.is(BiomeTags.IS_DEEP_OCEAN)) {
            int random;
            random = rand.nextInt(100);
            if (random > 35) {
                number = rand.nextInt(300000) + 100000;
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
        } else if (biome.is(BiomeTags.IS_RIVER)) {
            int random;
            random = rand.nextInt(100);
            if (random > 50) {
                number = rand.nextInt(200000) + 50000;
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
        } else if (biome.is(BiomeTags.IS_SAVANNA)) {
            int random;
            random = rand.nextInt(100);
            if (random > 50) {
                number = rand.nextInt(300000) + 100000;
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
        } else {
            int random;
            random = rand.nextInt(100);
            if (random > 89) {
                number = rand.nextInt(300000);
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
    }

}
