package com.denfop.villager.worldgen;

import com.denfop.IUItem;
import com.denfop.api.bee.Bee;
import com.denfop.api.bee.BeeInit;
import com.denfop.api.crop.Crop;
import com.denfop.api.crop.CropInit;
import com.denfop.api.crop.EnumSoil;
import com.denfop.blockentity.bee.BlockEntityApiary;
import com.denfop.blockentity.crop.TileEntityCrop;
import com.denfop.blocks.mechanism.BlockBaseMachine1Entity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.villager.ApiaryStructureHelper;
import com.denfop.villager.VillagerInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ProfessionHamletPiece extends ScatteredFeaturePiece {

    public static final int WIDTH = 58;
    public static final int HEIGHT = 20;
    public static final int DEPTH = 42;

    private static final HouseSlot[] HOUSE_SLOTS = new HouseSlot[]{
            new HouseSlot(2, 2),
            new HouseSlot(15, 2),
            new HouseSlot(28, 2),
            new HouseSlot(41, 2),
            new HouseSlot(2, 19)
    };

    private final boolean includeNuclear;
    private final ProfessionHouseKind[] houseLayout;
    private final GardenVariant gardenVariant;

    public ProfessionHamletPiece(final BlockPos origin, final boolean includeNuclear) {
        super(
                ModVillageStructures.PROFESSION_HAMLET_PIECE.get(),
                origin.getX(),
                origin.getY(),
                origin.getZ(),
                WIDTH,
                HEIGHT,
                DEPTH,
                Direction.NORTH
        );
        this.includeNuclear = includeNuclear;
        this.houseLayout = this.createDeterministicLayout(origin);
        this.gardenVariant = this.createDeterministicGardenVariant(origin);
    }

    public ProfessionHamletPiece(final CompoundTag tag) {
        super(ModVillageStructures.PROFESSION_HAMLET_PIECE.get(), tag);
        this.includeNuclear = tag.getBoolean("IncludeNuclear");
        this.houseLayout = this.readLayout(tag);
        this.gardenVariant = this.readGardenVariant(tag);
    }

    private static BlockState withFacing(final BlockState state, final Direction facing) {
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
        }
        if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
            return state.setValue(HorizontalDirectionalBlock.FACING, facing);
        }
        if (state.hasProperty(BlockStateProperties.FACING)) {
            return state.setValue(BlockStateProperties.FACING, facing);
        }
        return state;
    }

    @Override
    protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putBoolean("IncludeNuclear", this.includeNuclear);

        final int[] layout = new int[this.houseLayout.length];
        for (int i = 0; i < this.houseLayout.length; i++) {
            layout[i] = this.houseLayout[i].ordinal();
        }
        tag.putIntArray("HouseLayout", layout);
        tag.putInt("GardenVariant", this.gardenVariant.ordinal());
    }

    @Override
    public void postProcess(
            final WorldGenLevel level,
            final StructureManager structureManager,
            final ChunkGenerator chunkGenerator,
            final RandomSource random,
            final BoundingBox box,
            final ChunkPos chunkPos,
            final BlockPos pivot
    ) {
        final VillagePalette palette = this.resolvePalette(level);

        this.clearVolume(level, box, 0, 1, 0, WIDTH - 1, HEIGHT - 1, DEPTH - 1);
        this.buildCourtyard(level, box, palette);
        this.buildDynamicProfessionLayout(level, box, random, chunkPos, palette);

        if (this.includeNuclear) {
            this.buildNuclearLab(level, box, random, chunkPos, palette);
        } else {
            this.buildOpenStorageYard(level, box, random, chunkPos, palette);
        }

        this.decorateRoadside(level, box, palette);
    }

    private ProfessionHouseKind[] createDeterministicLayout(final BlockPos origin) {
        final ProfessionHouseKind[] layout = new ProfessionHouseKind[]{
                ProfessionHouseKind.ENGINEER,
                ProfessionHouseKind.MECHANIC,
                ProfessionHouseKind.METALLURG,
                ProfessionHouseKind.CHEMIST,
                ProfessionHouseKind.BOTANIST
        };

        final long seed = 341873128712L * origin.getX()
                + 132897987541L * origin.getZ()
                + 42317861L * origin.getY();

        final RandomSource random = RandomSource.create(seed);

        for (int i = layout.length - 1; i > 0; i--) {
            final int j = random.nextInt(i + 1);
            final ProfessionHouseKind tmp = layout[i];
            layout[i] = layout[j];
            layout[j] = tmp;
        }

        return layout;
    }

    private GardenVariant createDeterministicGardenVariant(final BlockPos origin) {
        final long seed = 9182736451L
                + 71L * origin.getX()
                + 131L * origin.getY()
                + 197L * origin.getZ();
        return GardenVariant.values()[RandomSource.create(seed).nextInt(GardenVariant.values().length)];
    }

    private ProfessionHouseKind[] readLayout(final CompoundTag tag) {
        final int[] saved = tag.getIntArray("HouseLayout");
        if (saved.length != HOUSE_SLOTS.length) {
            return new ProfessionHouseKind[]{
                    ProfessionHouseKind.ENGINEER,
                    ProfessionHouseKind.MECHANIC,
                    ProfessionHouseKind.METALLURG,
                    ProfessionHouseKind.CHEMIST,
                    ProfessionHouseKind.BOTANIST
            };
        }

        final ProfessionHouseKind[] result = new ProfessionHouseKind[HOUSE_SLOTS.length];
        for (int i = 0; i < saved.length; i++) {
            final int idx = Math.max(0, Math.min(saved[i], ProfessionHouseKind.values().length - 1));
            result[i] = ProfessionHouseKind.values()[idx];
        }
        return result;
    }

    private GardenVariant readGardenVariant(final CompoundTag tag) {
        final int idx = Math.max(0, Math.min(tag.getInt("GardenVariant"), GardenVariant.values().length - 1));
        return GardenVariant.values()[idx];
    }

    private VillagePalette resolvePalette(final WorldGenLevel level) {
        final int centerX = this.getWorldX(WIDTH / 2, DEPTH / 2);
        final int centerZ = this.getWorldZ(WIDTH / 2, DEPTH / 2);
        final ResourceKey<Biome> biomeKey = level.getBiome(new BlockPos(centerX, this.getWorldY(1), centerZ)).unwrapKey().orElse(null);
        final String path = biomeKey == null ? "" : biomeKey.location().getPath();

        final BiomeStyle style;
        if (path.contains("desert") || path.contains("badlands")) {
            style = BiomeStyle.DESERT;
        } else if (path.contains("savanna")) {
            style = BiomeStyle.SAVANNA;
        } else if (path.contains("taiga") || path.contains("old_growth")) {
            style = BiomeStyle.TAIGA;
        } else if (path.contains("snow") || path.contains("frozen") || path.contains("ice") || path.contains("grove")) {
            style = BiomeStyle.SNOWY;
        } else if (path.contains("swamp") || path.contains("mangrove")) {
            style = BiomeStyle.SWAMP;
        } else if (path.contains("jungle") || path.contains("bamboo")) {
            style = BiomeStyle.JUNGLE;
        } else {
            style = BiomeStyle.PLAINS;
        }

        final VillagePalette palette = new VillagePalette();
        palette.style = style;
        palette.path = Blocks.DIRT_PATH.defaultBlockState();
        palette.pathBorder = Blocks.COBBLESTONE.defaultBlockState();
        palette.window = Blocks.GLASS.defaultBlockState();
        palette.chimney = Blocks.STONE_BRICKS.defaultBlockState();
        palette.ground = Blocks.GRASS_BLOCK.defaultBlockState();
        palette.subsoil = Blocks.DIRT.defaultBlockState();

        switch (style) {
            case TAIGA -> {
                palette.foundation = Blocks.COBBLESTONE.defaultBlockState();
                palette.floor = Blocks.SPRUCE_PLANKS.defaultBlockState();
                palette.wall = Blocks.SPRUCE_PLANKS.defaultBlockState();
                palette.wallAccent = Blocks.COBBLESTONE.defaultBlockState();
                palette.pillar = Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState();
                palette.trim = Blocks.SPRUCE_PLANKS.defaultBlockState();
                palette.roofFill = Blocks.SPRUCE_PLANKS.defaultBlockState();
                palette.roofStairsBlock = Blocks.SPRUCE_STAIRS;
                palette.roofSlabBlock = Blocks.SPRUCE_SLAB;
                palette.woodStairsBlock = Blocks.SPRUCE_STAIRS;
                palette.woodSlabBlock = Blocks.SPRUCE_SLAB;
                palette.fenceBlock = Blocks.SPRUCE_FENCE;
                palette.gateBlock = Blocks.SPRUCE_FENCE_GATE;
                palette.doorBlock = Blocks.SPRUCE_DOOR;
                palette.flatRoof = false;
            }
            case DESERT -> {
                palette.foundation = Blocks.SANDSTONE.defaultBlockState();
                palette.floor = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
                palette.wall = Blocks.CUT_SANDSTONE.defaultBlockState();
                palette.wallAccent = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
                palette.pillar = Blocks.SANDSTONE.defaultBlockState();
                palette.trim = Blocks.WHITE_TERRACOTTA.defaultBlockState();
                palette.roofFill = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
                palette.roofStairsBlock = Blocks.SMOOTH_SANDSTONE_STAIRS;
                palette.roofSlabBlock = Blocks.SMOOTH_SANDSTONE_SLAB;
                palette.woodStairsBlock = Blocks.ACACIA_STAIRS;
                palette.woodSlabBlock = Blocks.ACACIA_SLAB;
                palette.fenceBlock = Blocks.ACACIA_FENCE;
                palette.gateBlock = Blocks.ACACIA_FENCE_GATE;
                palette.doorBlock = Blocks.ACACIA_DOOR;
                palette.path = Blocks.SANDSTONE.defaultBlockState();
                palette.pathBorder = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
                palette.window = Blocks.ORANGE_STAINED_GLASS.defaultBlockState();
                palette.chimney = Blocks.CUT_SANDSTONE.defaultBlockState();
                palette.ground = Blocks.SAND.defaultBlockState();
                palette.subsoil = Blocks.SANDSTONE.defaultBlockState();
                palette.flatRoof = true;
            }
            case SAVANNA -> {
                palette.foundation = Blocks.COBBLESTONE.defaultBlockState();
                palette.floor = Blocks.ACACIA_PLANKS.defaultBlockState();
                palette.wall = Blocks.WHITE_TERRACOTTA.defaultBlockState();
                palette.wallAccent = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
                palette.pillar = Blocks.STRIPPED_ACACIA_LOG.defaultBlockState();
                palette.trim = Blocks.ACACIA_PLANKS.defaultBlockState();
                palette.roofFill = Blocks.ACACIA_PLANKS.defaultBlockState();
                palette.roofStairsBlock = Blocks.ACACIA_STAIRS;
                palette.roofSlabBlock = Blocks.ACACIA_SLAB;
                palette.woodStairsBlock = Blocks.ACACIA_STAIRS;
                palette.woodSlabBlock = Blocks.ACACIA_SLAB;
                palette.fenceBlock = Blocks.ACACIA_FENCE;
                palette.gateBlock = Blocks.ACACIA_FENCE_GATE;
                palette.doorBlock = Blocks.ACACIA_DOOR;
                palette.window = Blocks.GLASS.defaultBlockState();
                palette.ground = Blocks.COARSE_DIRT.defaultBlockState();
                palette.subsoil = Blocks.DIRT.defaultBlockState();
                palette.flatRoof = false;
            }
            case SNOWY -> {
                palette.foundation = Blocks.COBBLESTONE.defaultBlockState();
                palette.floor = Blocks.SPRUCE_PLANKS.defaultBlockState();
                palette.wall = Blocks.PACKED_MUD.defaultBlockState();
                palette.wallAccent = Blocks.COBBLESTONE.defaultBlockState();
                palette.pillar = Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState();
                palette.trim = Blocks.SPRUCE_PLANKS.defaultBlockState();
                palette.roofFill = Blocks.SPRUCE_PLANKS.defaultBlockState();
                palette.roofStairsBlock = Blocks.SPRUCE_STAIRS;
                palette.roofSlabBlock = Blocks.SPRUCE_SLAB;
                palette.woodStairsBlock = Blocks.SPRUCE_STAIRS;
                palette.woodSlabBlock = Blocks.SPRUCE_SLAB;
                palette.fenceBlock = Blocks.SPRUCE_FENCE;
                palette.gateBlock = Blocks.SPRUCE_FENCE_GATE;
                palette.doorBlock = Blocks.SPRUCE_DOOR;
                palette.window = Blocks.WHITE_STAINED_GLASS.defaultBlockState();
                palette.ground = Blocks.SNOW_BLOCK.defaultBlockState();
                palette.subsoil = Blocks.DIRT.defaultBlockState();
                palette.flatRoof = false;
            }
            case SWAMP -> {
                palette.foundation = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
                palette.floor = Blocks.MANGROVE_PLANKS.defaultBlockState();
                palette.wall = Blocks.MUD_BRICKS.defaultBlockState();
                palette.wallAccent = Blocks.MANGROVE_PLANKS.defaultBlockState();
                palette.pillar = Blocks.STRIPPED_MANGROVE_LOG.defaultBlockState();
                palette.trim = Blocks.MANGROVE_PLANKS.defaultBlockState();
                palette.roofFill = Blocks.MANGROVE_PLANKS.defaultBlockState();
                palette.roofStairsBlock = Blocks.MANGROVE_STAIRS;
                palette.roofSlabBlock = Blocks.MANGROVE_SLAB;
                palette.woodStairsBlock = Blocks.MANGROVE_STAIRS;
                palette.woodSlabBlock = Blocks.MANGROVE_SLAB;
                palette.fenceBlock = Blocks.MANGROVE_FENCE;
                palette.gateBlock = Blocks.MANGROVE_FENCE_GATE;
                palette.doorBlock = Blocks.MANGROVE_DOOR;
                palette.pathBorder = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
                palette.ground = Blocks.MUD.defaultBlockState();
                palette.subsoil = Blocks.DIRT.defaultBlockState();
                palette.flatRoof = false;
            }
            case JUNGLE -> {
                palette.foundation = Blocks.COBBLESTONE.defaultBlockState();
                palette.floor = Blocks.JUNGLE_PLANKS.defaultBlockState();
                palette.wall = Blocks.JUNGLE_PLANKS.defaultBlockState();
                palette.wallAccent = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
                palette.pillar = Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState();
                palette.trim = Blocks.JUNGLE_PLANKS.defaultBlockState();
                palette.roofFill = Blocks.JUNGLE_PLANKS.defaultBlockState();
                palette.roofStairsBlock = Blocks.JUNGLE_STAIRS;
                palette.roofSlabBlock = Blocks.JUNGLE_SLAB;
                palette.woodStairsBlock = Blocks.JUNGLE_STAIRS;
                palette.woodSlabBlock = Blocks.JUNGLE_SLAB;
                palette.fenceBlock = Blocks.JUNGLE_FENCE;
                palette.gateBlock = Blocks.JUNGLE_FENCE_GATE;
                palette.doorBlock = Blocks.JUNGLE_DOOR;
                palette.flatRoof = false;
            }
            default -> {
                palette.foundation = Blocks.COBBLESTONE.defaultBlockState();
                palette.floor = Blocks.OAK_PLANKS.defaultBlockState();
                palette.wall = Blocks.OAK_PLANKS.defaultBlockState();
                palette.wallAccent = Blocks.COBBLESTONE.defaultBlockState();
                palette.pillar = Blocks.STRIPPED_OAK_LOG.defaultBlockState();
                palette.trim = Blocks.OAK_PLANKS.defaultBlockState();
                palette.roofFill = Blocks.SPRUCE_PLANKS.defaultBlockState();
                palette.roofStairsBlock = Blocks.SPRUCE_STAIRS;
                palette.roofSlabBlock = Blocks.SPRUCE_SLAB;
                palette.woodStairsBlock = Blocks.OAK_STAIRS;
                palette.woodSlabBlock = Blocks.OAK_SLAB;
                palette.fenceBlock = Blocks.OAK_FENCE;
                palette.gateBlock = Blocks.OAK_FENCE_GATE;
                palette.doorBlock = Blocks.OAK_DOOR;
                palette.flatRoof = false;
            }
        }

        return palette;
    }

    private void buildDynamicProfessionLayout(
            final WorldGenLevel level,
            final BoundingBox box,
            final RandomSource random,
            final ChunkPos chunkPos,
            final VillagePalette palette
    ) {
        for (int i = 0; i < HOUSE_SLOTS.length; i++) {
            final HouseSlot slot = HOUSE_SLOTS[i];
            final ProfessionHouseKind kind = this.houseLayout[i];

            switch (kind) {
                case ENGINEER -> this.buildEngineerWorkshop(level, box, random, chunkPos, slot.x, slot.z, palette);
                case MECHANIC -> this.buildMechanicGarage(level, box, random, chunkPos, slot.x, slot.z, palette);
                case METALLURG -> this.buildMetallurgForge(level, box, random, chunkPos, slot.x, slot.z, palette);
                case CHEMIST -> this.buildChemistLab(level, box, random, chunkPos, slot.x, slot.z, palette);
                case BOTANIST -> this.buildBotanistHomestead(level, box, random, chunkPos, slot.x, slot.z, palette);
            }
        }
    }

    private void buildCourtyard(final WorldGenLevel level, final BoundingBox box, final VillagePalette palette) {
        this.prepareGroundRect(level, box, 0, 0, WIDTH - 1, DEPTH - 1, palette);

        this.paveRect(level, box, 0, 13, WIDTH - 1, 16, palette.path, palette.path);
        this.paveRect(level, box, 11, 0, 14, DEPTH - 1, palette.path, palette.path);
        this.paveRect(level, box, 24, 0, 27, DEPTH - 1, palette.path, palette.path);
        this.paveRect(level, box, 37, 0, 40, DEPTH - 1, palette.path, palette.path);
        this.paveRect(level, box, 14, 16, 37, 24, palette.pathBorder, palette.pathBorder);

        this.placeCourtyardFountain(level, box, 19, 18, palette);
        this.placeVillageBellFrame(level, box, 25, 1, 20, palette);

        this.placeLanternPost(level, box, 6, 1, 14, palette);
        this.placeLanternPost(level, box, 16, 1, 18, palette);
        this.placeLanternPost(level, box, 35, 1, 18, palette);
        this.placeLanternPost(level, box, 45, 1, 14, palette);
        this.placeLanternPost(level, box, 12, 1, 31, palette);
        this.placeLanternPost(level, box, 25, 1, 35, palette);
        this.placeLanternPost(level, box, 38, 1, 31, palette);
        this.placeLanternPost(level, box, 50, 1, 24, palette);

        this.placeBench(level, box, 18, 1, 23, Direction.EAST, palette);
        this.placeBench(level, box, 30, 1, 17, Direction.WEST, palette);

        this.placePlanter(level, box, 16, 18, palette, this.getDecorFlowers(palette.style)[0]);
        this.placePlanter(level, box, 33, 22, palette, this.getDecorFlowers(palette.style)[1]);
        this.placePlanter(level, box, 17, 22, palette, this.getDecorFlowers(palette.style)[2]);
        this.placePlanter(level, box, 32, 18, palette, this.getDecorFlowers(palette.style)[3]);
    }

    private void buildEngineerWorkshop(
            final WorldGenLevel level,
            final BoundingBox box,
            final RandomSource random,
            final ChunkPos chunkPos,
            final int baseX,
            final int baseZ,
            final VillagePalette palette
    ) {
        final int minX = baseX;
        final int minZ = baseZ;
        final int maxX = baseX + 9;
        final int maxZ = baseZ + 7;

        this.buildFramedHouse(level, box, minX, minZ, maxX, maxZ, palette, 4);
        this.placeDoorway(level, box, baseX + 6, 1, maxZ, Direction.SOUTH, palette.doorBlock);
        this.buildCoveredPorch(level, box, baseX + 4, baseX + 8, maxZ + 1, palette);

        this.placeWindow(level, box, baseX + 2, 2, minZ, palette.window);
        this.placeWindow(level, box, baseX + 7, 2, minZ, palette.window);
        this.placeWindow(level, box, minX, 2, baseZ + 3, palette.window);
        this.placeWindow(level, box, maxX, 2, baseZ + 4, palette.window);

        this.placeJobBlock(level, box, baseX + 2, 1, baseZ + 2,
                withFacing(IUItem.programming_table.getObject(0).get().defaultBlockState(), Direction.SOUTH));
        this.placeBlock(level, Blocks.CRAFTING_TABLE.defaultBlockState(), baseX + 5, 1, baseZ + 2, box);
        this.placeBlock(level, Blocks.BOOKSHELF.defaultBlockState(), baseX + 8, 1, baseZ + 2, box);
        this.placeBlock(level, Blocks.LECTERN.defaultBlockState(), baseX + 8, 1, baseZ + 4, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), baseX + 1, 1, baseZ + 5, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), baseX + 2, 1, baseZ + 5, box);
        this.placeBlock(level, Blocks.CRAFTING_TABLE.defaultBlockState(), baseX + 8, 1, baseZ + 5, box);
        this.placeCeilingLantern(level, box, baseX + 5, 4, baseZ + 3);
        this.placeBed(level, box, baseX + 1, 1, baseZ + 1, Direction.EAST, Blocks.BLUE_BED);
        this.placeBlock(level, Blocks.AIR.defaultBlockState(), baseX + 1, 2, baseZ + 1, box);
        this.placeBlock(level, Blocks.AIR.defaultBlockState(), baseX + 2, 2, baseZ + 1, box);

        this.tryCreateChest(level, box, random, chunkPos, baseX + 7, 1, baseZ + 5, BuiltInLootTables.VILLAGE_PLAINS_HOUSE);
        this.trySpawnVillager(level, box, chunkPos, baseX + 5, 1, baseZ + 4, VillagerInit.ENGINEER.get());
    }

    private void buildMechanicGarage(
            final WorldGenLevel level,
            final BoundingBox box,
            final RandomSource random,
            final ChunkPos chunkPos,
            final int baseX,
            final int baseZ,
            final VillagePalette palette
    ) {
        final int minX = baseX;
        final int minZ = baseZ;
        final int maxX = baseX + 8;
        final int maxZ = baseZ + 7;

        this.buildWorkshopHouse(level, box, minX, minZ, maxX, maxZ, palette, 4);
        this.placeDoorway(level, box, baseX + 1, 1, maxZ, Direction.SOUTH, palette.doorBlock);
        this.clearVolume(level, box, baseX + 4, 1, maxZ, baseX + 6, 3, maxZ);
        this.buildGarageAwning(level, box, baseX + 3, baseX + 7, maxZ + 1, palette);
        this.paveRect(level, box, baseX + 3, maxZ + 1, baseX + 7, maxZ + 2, palette.path, palette.path);

        this.placeWindow(level, box, baseX + 2, 2, minZ, palette.window);
        this.placeWindow(level, box, minX, 2, baseZ + 3, palette.window);

        this.placeJobBlock(level, box, baseX + 2, 1, baseZ + 2,
                withFacing(IUItem.basemachine2.getObject(BlockBaseMachine3Entity.generator_iu.getId()).get().defaultBlockState(), Direction.SOUTH));
        this.placeBlock(level, Blocks.BLAST_FURNACE.defaultBlockState(), baseX + 6, 1, baseZ + 2, box);
        this.placeBlock(level, Blocks.SMITHING_TABLE.defaultBlockState(), baseX + 6, 1, baseZ + 4, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), baseX + 1, 1, baseZ + 4, box);
        this.placeBlock(level, Blocks.PISTON.defaultBlockState(), baseX + 5, 1, baseZ + 4, box);
        this.placeBlock(level, Blocks.GRINDSTONE.defaultBlockState(), baseX + 5, 1, baseZ + 5, box);
        this.placeBed(level, box, baseX + 2, 1, baseZ + 6, Direction.EAST, Blocks.GRAY_BED);
        this.placeCeilingLantern(level, box, baseX + 3, 4, baseZ + 3);

        this.tryCreateChest(level, box, random, chunkPos, baseX + 2, 1, baseZ + 4, BuiltInLootTables.VILLAGE_TOOLSMITH);
        this.trySpawnVillager(level, box, chunkPos, baseX + 4, 1, baseZ + 4, VillagerInit.MECHANIC.get());
    }

    private void buildMetallurgForge(
            final WorldGenLevel level,
            final BoundingBox box,
            final RandomSource random,
            final ChunkPos chunkPos,
            final int baseX,
            final int baseZ,
            final VillagePalette palette
    ) {
        final int mainMinX = baseX + 2;
        final int mainMinZ = baseZ;
        final int mainMaxX = baseX + 8;
        final int mainMaxZ = baseZ + 7;

        final int wingMinX = baseX;
        final int wingMinZ = baseZ + 4;
        final int wingMaxX = baseX + 2;
        final int wingMaxZ = baseZ + 7;

        final BlockState forgeWall = this.getForgeWall(palette);
        this.buildHeavyForgeHouse(level, box, mainMinX, mainMinZ, mainMaxX, mainMaxZ, forgeWall, palette);
        this.buildForgeWing(level, box, wingMinX, wingMinZ, wingMaxX, wingMaxZ, forgeWall, palette);
        this.clearVolume(level, box, baseX + 2, 1, baseZ + 4, baseX + 2, 2, baseZ + 5);

        this.placeDoorway(level, box, baseX + 5, 1, mainMaxZ, Direction.SOUTH, palette.doorBlock);
        this.buildSmithApron(level, box, baseX + 4, baseX + 6, mainMaxZ + 1, palette);
        this.paveRect(level, box, baseX, wingMaxZ + 1, baseX + 3, wingMaxZ + 2, palette.path, palette.path);

        this.placeWindow(level, box, baseX + 3, 2, mainMinZ, palette.window);
        this.placeWindow(level, box, baseX + 6, 2, mainMinZ, palette.window);
        this.placeWindow(level, box, wingMinX, 2, baseZ + 5, palette.window);

        this.placeJobBlock(level, box, baseX + 5, 1, baseZ + 3,
                withFacing(IUItem.anvil.getObject(0).get().defaultBlockState(), Direction.SOUTH));
        this.placeBlock(level, Blocks.BLAST_FURNACE.defaultBlockState(), baseX + 7, 1, baseZ + 2, box);
        this.placeBlock(level, Blocks.SMITHING_TABLE.defaultBlockState(), baseX + 7, 1, baseZ + 5, box);
        this.placeBlock(level, Blocks.CHAIN.defaultBlockState(), baseX + 4, 4, baseZ + 3, box);
        this.placeBlock(level, Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true), baseX + 4, 3, baseZ + 3, box);
        this.placeBlock(level, Blocks.CAULDRON.defaultBlockState(), baseX + 3, 1, baseZ + 5, box);
        this.placeBlock(level, Blocks.CHEST.defaultBlockState(), baseX + 0, 1, baseZ + 5, box);
        this.placeBlock(level, Blocks.CHEST.defaultBlockState(), baseX + 1, 1, baseZ + 5, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), baseX + 0, 1, baseZ + 6, box);
        this.placeBed(level, box, baseX + 3, 1, baseZ + 1, Direction.EAST, Blocks.RED_BED);
        this.placeBlock(level, Blocks.AIR.defaultBlockState(), baseX + 3, 2, baseZ + 1, box);
        this.placeBlock(level, Blocks.AIR.defaultBlockState(), baseX + 4, 2, baseZ + 1, box);

        this.buildChimney(level, box, baseX + 8, 1, baseZ + 1, forgeWall, true);

        this.tryCreateChest(level, box, random, chunkPos, baseX + 1, 1, baseZ + 5, BuiltInLootTables.VILLAGE_ARMORER);
        this.trySpawnVillager(level, box, chunkPos, baseX + 5, 1, baseZ + 4, VillagerInit.METALLURG.get());
    }

    private void buildChemistLab(
            final WorldGenLevel level,
            final BoundingBox box,
            final RandomSource random,
            final ChunkPos chunkPos,
            final int baseX,
            final int baseZ,
            final VillagePalette palette
    ) {
        final int minX = baseX;
        final int minZ = baseZ;
        final int maxX = baseX + 8;
        final int maxZ = baseZ + 7;
        final BlockState cleanWall = this.getLabWall(palette);

        this.buildCleanLabHouse(level, box, minX, minZ, maxX, maxZ, palette);
        this.buildInsetSecondFloor(level, box, baseX + 1, baseZ + 1, baseX + 7, baseZ + 4, cleanWall, palette, 5, 7, true);
        this.placeDoorway(level, box, baseX + 4, 1, maxZ, Direction.SOUTH, palette.doorBlock);
        this.buildCoveredPorch(level, box, baseX + 2, baseX + 6, maxZ + 1, palette);
        this.buildChemistStairs(level, box, baseX + 7, baseZ + 3, palette);

        this.placeWindow(level, box, baseX + 2, 2, minZ, palette.window);
        this.placeWindow(level, box, baseX + 6, 2, minZ, palette.window);
        this.placeWindow(level, box, maxX, 2, baseZ + 3, palette.window);
        this.placeWindow(level, box, baseX + 2, 6, baseZ + 1, palette.window);
        this.placeWindow(level, box, baseX + 6, 6, baseZ + 1, palette.window);
        this.placeWindow(level, box, baseX + 1, 6, baseZ + 3, palette.window);
        this.placeWindow(level, box, baseX + 7, 6, baseZ + 3, palette.window);

        this.placeJobBlock(level, box, baseX + 2, 1, baseZ + 2,
                withFacing(IUItem.fluidIntegrator.getObject(0).get().defaultBlockState(), Direction.SOUTH));
        this.placeBlock(level, Blocks.BREWING_STAND.defaultBlockState(), baseX + 6, 1, baseZ + 2, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), baseX + 1, 1, baseZ + 4, box);
        this.placeBlock(level, Blocks.WATER_CAULDRON.defaultBlockState(), baseX + 6, 1, baseZ + 5, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), baseX + 5, 1, baseZ + 5, box);
        this.placeBlock(level, Blocks.POTTED_BLUE_ORCHID.defaultBlockState(), baseX + 5, 2, baseZ + 5, box);
        this.placeBlock(level, Blocks.BOOKSHELF.defaultBlockState(), baseX + 2, 5, baseZ + 2, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), baseX + 6, 5, baseZ + 2, box);
        this.placeBlock(level, Blocks.POTTED_ALLIUM.defaultBlockState(), baseX + 6, 6, baseZ + 2, box);
        this.placeCeilingLantern(level, box, baseX + 4, 4, baseZ + 3);
        this.placeCeilingLantern(level, box, baseX + 4, 7, baseZ + 3);
        this.placeBed(level, box, baseX + 1, 1, baseZ + 6, Direction.EAST, Blocks.WHITE_BED);

        this.tryCreateChest(level, box, random, chunkPos, baseX + 7, 1, baseZ + 4, BuiltInLootTables.VILLAGE_TANNERY);
        this.trySpawnVillager(level, box, chunkPos, baseX + 4, 1, baseZ + 4, VillagerInit.CHEMIST.get());
    }

    private void buildBotanistHomestead(
            final WorldGenLevel level,
            final BoundingBox box,
            final RandomSource random,
            final ChunkPos chunkPos,
            final int baseX,
            final int baseZ,
            final VillagePalette palette
    ) {
        final int mainMinX = baseX;
        final int mainMinZ = baseZ;
        final int mainMaxX = baseX + 8;
        final int mainMaxZ = baseZ + 6;

        final int greenhouseMinX = baseX + 9;
        final int greenhouseMinZ = baseZ + 2;
        final int greenhouseMaxX = baseX + 11;
        final int greenhouseMaxZ = baseZ + 6;

        this.buildFramedHouse(level, box, mainMinX, mainMinZ, mainMaxX, mainMaxZ, palette, 4);
        this.buildGreenhouseAnnex(level, box, greenhouseMinX, greenhouseMinZ, greenhouseMaxX, greenhouseMaxZ, palette);
        this.clearVolume(level, box, mainMaxX, 1, baseZ + 3, greenhouseMinX, 2, baseZ + 4);
        this.placeBlock(level, palette.trim, mainMaxX, 3, baseZ + 3, box);
        this.placeBlock(level, palette.trim, mainMaxX, 3, baseZ + 4, box);
        this.placeBlock(level, palette.trim, greenhouseMinX, 3, baseZ + 3, box);
        this.placeBlock(level, palette.trim, greenhouseMinX, 3, baseZ + 4, box);

        this.placeDoorway(level, box, baseX + 4, 1, mainMaxZ, Direction.SOUTH, palette.doorBlock);
        this.buildCoveredPorch(level, box, baseX + 2, baseX + 6, mainMaxZ + 1, palette);

        this.placeWindow(level, box, baseX + 2, 2, mainMinZ, palette.window);
        this.placeWindow(level, box, baseX + 6, 2, mainMinZ, palette.window);
        this.placeWindow(level, box, mainMinX, 2, baseZ + 3, palette.window);
        this.placeWindow(level, box, greenhouseMaxX, 2, baseZ + 4, palette.window);

        this.placeJobBlock(level, box, baseX + 2, 1, baseZ + 2,
                withFacing(IUItem.apiary.getObject(0).get().defaultBlockState(), Direction.SOUTH));
        this.placeBlock(level, Blocks.CRAFTING_TABLE.defaultBlockState(), baseX + 4, 1, baseZ + 2, box);
        this.placeBlock(level, Blocks.COMPOSTER.defaultBlockState(), baseX + 7, 1, baseZ + 5, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), baseX + 6, 1, baseZ + 5, box);
        this.placeBlock(level, Blocks.MOSS_BLOCK.defaultBlockState(), baseX + 5, 1, baseZ + 4, box);
        this.placeBlock(level, Blocks.AZALEA.defaultBlockState(), baseX + 5, 2, baseZ + 4, box);
        this.placeCeilingLantern(level, box, baseX + 4, 4, baseZ + 3);
        this.placeBed(level, box, baseX + 1, 1, baseZ + 1, Direction.EAST, Blocks.GREEN_BED);
        this.placeBlock(level, Blocks.AIR.defaultBlockState(), baseX + 1, 2, baseZ + 1, box);
        this.placeBlock(level, Blocks.AIR.defaultBlockState(), baseX + 2, 2, baseZ + 1, box);

        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), greenhouseMinX + 1, 1, greenhouseMinZ + 1, box);
        this.placeBlock(level, Blocks.POTTED_FERN.defaultBlockState(), greenhouseMinX + 1, 2, greenhouseMinZ + 1, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), greenhouseMaxX - 1, 1, greenhouseMaxZ - 1, box);
        this.placeBlock(level, Blocks.POTTED_DANDELION.defaultBlockState(), greenhouseMaxX - 1, 2, greenhouseMaxZ - 1, box);
        this.placeBlock(level, Blocks.MOSS_BLOCK.defaultBlockState(), greenhouseMinX + 1, 1, greenhouseMinZ + 3, box);
        this.placeBlock(level, Blocks.FLOWERING_AZALEA.defaultBlockState(), greenhouseMinX + 1, 2, greenhouseMinZ + 3, box);

        this.tryCreateChest(level, box, random, chunkPos, baseX + 7, 1, baseZ + 4, BuiltInLootTables.VILLAGE_PLAINS_HOUSE);
        this.trySpawnVillager(level, box, chunkPos, baseX + 4, 1, baseZ + 4, VillagerInit.BOTANIST.get());

        final GardenPlan gardenPlan = this.createBotanistGardenPlan(level, random);
        this.buildBotanistGarden(level, box, random, chunkPos, gardenPlan, palette);
    }

    private void buildFramedHouse(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final VillagePalette palette,
            final int wallHeight
    ) {
        this.clearVolume(level, box, minX, 1, minZ, maxX, 12, maxZ);
        this.foundationRect(level, box, minX, minZ, maxX, maxZ, palette.foundation);
        this.generateBox(level, box, minX + 1, 0, minZ + 1, maxX - 1, 0, maxZ - 1, palette.floor, palette.floor, false);

        this.generateBox(level, box, minX, 1, minZ, maxX, wallHeight, maxZ, palette.wall, palette.wall, false);
        this.generateBox(level, box, minX + 1, 1, minZ + 1, maxX - 1, wallHeight - 1, maxZ - 1, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);

        for (int x = minX; x <= maxX; x++) {
            this.placeBlock(level, palette.wallAccent, x, 1, minZ, box);
            this.placeBlock(level, palette.wallAccent, x, 1, maxZ, box);
            this.placeBlock(level, palette.trim, x, wallHeight, minZ, box);
            this.placeBlock(level, palette.trim, x, wallHeight, maxZ, box);
        }
        for (int z = minZ; z <= maxZ; z++) {
            this.placeBlock(level, palette.wallAccent, minX, 1, z, box);
            this.placeBlock(level, palette.wallAccent, maxX, 1, z, box);
            this.placeBlock(level, palette.trim, minX, wallHeight, z, box);
            this.placeBlock(level, palette.trim, maxX, wallHeight, z, box);
        }

        for (int y = 1; y <= wallHeight; y++) {
            this.placeBlock(level, palette.pillar, minX, y, minZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, minZ, box);
            this.placeBlock(level, palette.pillar, minX, y, maxZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, maxZ, box);
        }

        this.buildVillageRoof(level, box, minX, wallHeight + 1, minZ, maxX, maxZ, palette);
    }

    private void buildWorkshopHouse(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final VillagePalette palette,
            final int wallHeight
    ) {
        this.clearVolume(level, box, minX, 1, minZ, maxX, 12, maxZ);
        this.foundationRect(level, box, minX, minZ, maxX, maxZ, palette.foundation);
        this.generateBox(level, box, minX + 1, 0, minZ + 1, maxX - 1, 0, maxZ - 1, palette.floor, palette.floor, false);

        final BlockState workshopWall = this.getWorkshopWall(palette);
        this.generateBox(level, box, minX, 1, minZ, maxX, wallHeight, maxZ, workshopWall, workshopWall, false);
        this.generateBox(level, box, minX + 1, 1, minZ + 1, maxX - 1, wallHeight - 1, maxZ - 1, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);

        for (int x = minX; x <= maxX; x++) {
            this.placeBlock(level, palette.wallAccent, x, 1, minZ, box);
            this.placeBlock(level, palette.wallAccent, x, 1, maxZ, box);
            this.placeBlock(level, palette.trim, x, wallHeight, minZ, box);
            this.placeBlock(level, palette.trim, x, wallHeight, maxZ, box);
        }
        for (int z = minZ; z <= maxZ; z++) {
            this.placeBlock(level, palette.wallAccent, minX, 1, z, box);
            this.placeBlock(level, palette.wallAccent, maxX, 1, z, box);
            this.placeBlock(level, palette.trim, minX, wallHeight, z, box);
            this.placeBlock(level, palette.trim, maxX, wallHeight, z, box);
        }

        for (int y = 1; y <= wallHeight; y++) {
            this.placeBlock(level, palette.pillar, minX, y, minZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, minZ, box);
            this.placeBlock(level, palette.pillar, minX, y, maxZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, maxZ, box);
        }

        this.buildVillageRoof(level, box, minX, wallHeight + 1, minZ, maxX, maxZ, palette);
    }

    private void buildHeavyForgeHouse(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final BlockState wall,
            final VillagePalette palette
    ) {
        this.clearVolume(level, box, minX, 1, minZ, maxX, 13, maxZ);
        this.foundationRect(level, box, minX, minZ, maxX, maxZ, wall);
        this.generateBox(level, box, minX + 1, 0, minZ + 1, maxX - 1, 0, maxZ - 1, Blocks.POLISHED_ANDESITE.defaultBlockState(), Blocks.POLISHED_ANDESITE.defaultBlockState(), false);

        this.generateBox(level, box, minX, 1, minZ, maxX, 4, maxZ, wall, wall, false);
        this.generateBox(level, box, minX + 1, 1, minZ + 1, maxX - 1, 3, maxZ - 1, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);

        for (int y = 1; y <= 5; y++) {
            this.placeBlock(level, wall, minX, y, minZ, box);
            this.placeBlock(level, wall, maxX, y, minZ, box);
            this.placeBlock(level, wall, minX, y, maxZ, box);
            this.placeBlock(level, wall, maxX, y, maxZ, box);
        }

        this.buildVillageRoof(level, box, minX, 5, minZ, maxX, maxZ, palette);
    }

    private void buildCleanLabHouse(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final VillagePalette palette
    ) {
        final BlockState cleanWall = this.getLabWall(palette);
        this.clearVolume(level, box, minX, 1, minZ, maxX, 12, maxZ);
        this.foundationRect(level, box, minX, minZ, maxX, maxZ, palette.foundation);
        this.generateBox(level, box, minX + 1, 0, minZ + 1, maxX - 1, 0, maxZ - 1, palette.floor, palette.floor, false);

        this.generateBox(level, box, minX, 1, minZ, maxX, 4, maxZ, cleanWall, cleanWall, false);
        this.generateBox(level, box, minX + 1, 1, minZ + 1, maxX - 1, 3, maxZ - 1, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);

        for (int y = 1; y <= 4; y++) {
            this.placeBlock(level, palette.pillar, minX, y, minZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, minZ, box);
            this.placeBlock(level, palette.pillar, minX, y, maxZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, maxZ, box);
        }

        for (int x = minX; x <= maxX; x++) {
            this.placeBlock(level, palette.trim, x, 4, minZ, box);
            this.placeBlock(level, palette.trim, x, 4, maxZ, box);
        }
        for (int z = minZ; z <= maxZ; z++) {
            this.placeBlock(level, palette.trim, minX, 4, z, box);
            this.placeBlock(level, palette.trim, maxX, 4, z, box);
        }

        this.buildFlatRoof(level, box, minX, 5, minZ, maxX, maxZ, palette);
    }

    private void buildVillageRoof(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int roofBaseY,
            final int minZ,
            final int maxX,
            final int maxZ,
            final VillagePalette palette
    ) {
        if (palette.flatRoof) {
            this.buildFlatRoof(level, box, minX, roofBaseY, minZ, maxX, maxZ, palette);
        } else {
            this.buildGabledRoof(level, box, minX, roofBaseY, minZ, maxX, maxZ, palette);
        }
    }

    private void buildGabledRoof(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int roofBaseY,
            final int minZ,
            final int maxX,
            final int maxZ,
            final VillagePalette palette
    ) {
        int north = minZ;
        int south = maxZ;
        int y = roofBaseY;

        final BlockState northFacing = withFacing(palette.roofStairsBlock.defaultBlockState(), Direction.NORTH);
        final BlockState southFacing = withFacing(palette.roofStairsBlock.defaultBlockState(), Direction.SOUTH);

        while (north <= south) {
            this.generateBox(level, box, minX, y, north, maxX, y, south, palette.roofFill, palette.roofFill, false);

            for (int x = minX - 1; x <= maxX + 1; x++) {
                this.placeBlock(level, northFacing, x, y, north - 1, box);
                this.placeBlock(level, southFacing, x, y, south + 1, box);
            }

            north++;
            south--;
            y++;
        }

        for (int z = minZ; z <= maxZ; z++) {
            this.placeBlock(level, palette.trim, minX - 1, roofBaseY, z, box);
            this.placeBlock(level, palette.trim, maxX + 1, roofBaseY, z, box);
        }
    }

    private void buildFlatRoof(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int roofBaseY,
            final int minZ,
            final int maxX,
            final int maxZ,
            final VillagePalette palette
    ) {
        this.generateBox(level, box, minX - 1, roofBaseY, minZ - 1, maxX + 1, roofBaseY, maxZ + 1, palette.roofFill, palette.roofFill, false);

        for (int x = minX - 1; x <= maxX + 1; x++) {
            this.placeBlock(level, palette.trim, x, roofBaseY + 1, minZ - 1, box);
            this.placeBlock(level, palette.trim, x, roofBaseY + 1, maxZ + 1, box);
        }
        for (int z = minZ - 1; z <= maxZ + 1; z++) {
            this.placeBlock(level, palette.trim, minX - 1, roofBaseY + 1, z, box);
            this.placeBlock(level, palette.trim, maxX + 1, roofBaseY + 1, z, box);
        }

        this.placeBlock(level, palette.roofSlabBlock.defaultBlockState(), minX + 1, roofBaseY + 1, minZ + 1, box);
        this.placeBlock(level, palette.roofSlabBlock.defaultBlockState(), maxX - 1, roofBaseY + 1, minZ + 1, box);
        this.placeBlock(level, palette.roofSlabBlock.defaultBlockState(), minX + 1, roofBaseY + 1, maxZ - 1, box);
        this.placeBlock(level, palette.roofSlabBlock.defaultBlockState(), maxX - 1, roofBaseY + 1, maxZ - 1, box);
    }

    private BlockState getWorkshopWall(final VillagePalette palette) {
        return switch (palette.style) {
            case DESERT -> Blocks.SMOOTH_SANDSTONE.defaultBlockState();
            case SAVANNA -> Blocks.YELLOW_TERRACOTTA.defaultBlockState();
            case SWAMP -> Blocks.MUD_BRICKS.defaultBlockState();
            case JUNGLE -> Blocks.COBBLESTONE.defaultBlockState();
            case SNOWY -> Blocks.STONE_BRICKS.defaultBlockState();
            case TAIGA -> Blocks.COBBLESTONE.defaultBlockState();
            default -> Blocks.STONE_BRICKS.defaultBlockState();
        };
    }

    private BlockState getForgeWall(final VillagePalette palette) {
        return switch (palette.style) {
            case DESERT -> Blocks.CUT_RED_SANDSTONE.defaultBlockState();
            case SAVANNA -> Blocks.BROWN_TERRACOTTA.defaultBlockState();
            case SWAMP -> Blocks.DEEPSLATE_BRICKS.defaultBlockState();
            case JUNGLE -> Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
            default -> Blocks.DEEPSLATE_BRICKS.defaultBlockState();
        };
    }

    private BlockState getLabWall(final VillagePalette palette) {
        return switch (palette.style) {
            case DESERT -> Blocks.SMOOTH_SANDSTONE.defaultBlockState();
            case SAVANNA -> Blocks.WHITE_TERRACOTTA.defaultBlockState();
            case SWAMP -> Blocks.MUD_BRICKS.defaultBlockState();
            case JUNGLE -> Blocks.BIRCH_PLANKS.defaultBlockState();
            case TAIGA -> Blocks.BIRCH_PLANKS.defaultBlockState();
            default -> Blocks.QUARTZ_BRICKS.defaultBlockState();
        };
    }

    private void buildCoveredPorch(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int maxX,
            final int z,
            final VillagePalette palette
    ) {
        for (int x = minX; x <= maxX; x++) {
            this.placeBlock(level, palette.woodSlabBlock.defaultBlockState(), x, 0, z, box);
            this.fillColumnDown(level, palette.foundation, x, -1, z, box);
        }

        this.placeBlock(level, palette.pillar, minX, 1, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), minX, 2, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), minX, 3, z, box);
        this.placeBlock(level, palette.pillar, maxX, 1, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), maxX, 2, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), maxX, 3, z, box);

        this.generateBox(level, box, minX - 1, 4, z - 1, maxX + 1, 4, z + 1, palette.roofFill, palette.roofFill, false);
        for (int x = minX - 1; x <= maxX + 1; x++) {
            this.placeBlock(level, withFacing(palette.roofStairsBlock.defaultBlockState(), Direction.SOUTH), x, 4, z + 2, box);
            this.placeBlock(level, withFacing(palette.roofStairsBlock.defaultBlockState(), Direction.NORTH), x, 4, z - 2, box);
        }
    }

    private void buildGarageAwning(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int maxX,
            final int z,
            final VillagePalette palette
    ) {
        this.placeBlock(level, palette.pillar, minX, 1, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), minX, 2, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), minX, 3, z, box);
        this.placeBlock(level, palette.pillar, maxX, 1, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), maxX, 2, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), maxX, 3, z, box);

        this.generateBox(level, box, minX, 4, z - 1, maxX, 4, z, palette.roofFill, palette.roofFill, false);
        for (int x = minX; x <= maxX; x++) {
            this.placeBlock(level, withFacing(palette.roofStairsBlock.defaultBlockState(), Direction.SOUTH), x, 4, z + 1, box);
        }
    }

    private void buildSmithApron(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int maxX,
            final int z,
            final VillagePalette palette
    ) {
        this.paveRect(level, box, minX, z, maxX, z + 1, palette.pathBorder, palette.pathBorder);
        this.placeBlock(level, Blocks.CAULDRON.defaultBlockState(), minX, 1, z + 1, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), maxX, 1, z + 1, box);
    }

    private void buildChimney(
            final WorldGenLevel level,
            final BoundingBox box,
            final int x,
            final int y,
            final int z,
            final BlockState wall,
            final boolean withCampfire
    ) {
        this.placeBlock(level, wall, x - 1, y, z, box);
        this.placeBlock(level, wall, x, y, z, box);
        this.placeBlock(level, wall, x - 1, y + 1, z, box);
        this.placeBlock(level, wall, x, y + 1, z, box);

        if (withCampfire) {
            this.placeBlock(level, Blocks.CAMPFIRE.defaultBlockState(), x - 1, y, z + 1, box);
            this.placeBlock(level, Blocks.CAMPFIRE.defaultBlockState(), x, y, z + 1, box);
            this.placeBlock(level, wall, x - 1, y + 1, z + 1, box);
            this.placeBlock(level, wall, x, y + 1, z + 1, box);
        }

        for (int yy = y + 2; yy <= y + 6; yy++) {
            this.placeBlock(level, wall, x - 1, yy, z, box);
            this.placeBlock(level, wall, x, yy, z, box);
        }

        this.placeBlock(level, Blocks.COBBLESTONE_WALL.defaultBlockState(), x - 1, y + 7, z, box);
        this.placeBlock(level, Blocks.COBBLESTONE_WALL.defaultBlockState(), x, y + 7, z, box);
    }

    private void buildBotanistGarden(
            final WorldGenLevel level,
            final BoundingBox box,
            final RandomSource random,
            final ChunkPos chunkPos,
            final GardenPlan gardenPlan,
            final VillagePalette palette
    ) {
        final int minX = 13;
        final int minZ = 18;
        final int maxX = 31;
        final int maxZ = 39;

        this.prepareGardenArea(level, box, minX, minZ, maxX, maxZ, palette);
        this.buildGardenFence(level, box, minX, minZ, maxX, maxZ, 22, palette);
        this.placeLanternPost(level, box, 15, 1, 20, palette);
        this.placeLanternPost(level, box, 29, 1, 20, palette);
        this.placeLanternPost(level, box, 15, 1, 37, palette);
        this.placeLanternPost(level, box, 29, 1, 37, palette);

        this.placeMiniGardenFountain(level, box, 28, 20, palette);
        this.tryCreateChest(level, box, random, chunkPos, 28, 1, 36, BuiltInLootTables.VILLAGE_PLAINS_HOUSE);

        this.paveRect(level, box, 21, 18, 23, 39, palette.path, palette.path);
        this.paveRect(level, box, 13, 27, 31, 29, palette.path, palette.path);

        this.placeWaterCell(level, box, 18, 25, palette);
        this.placeWaterCell(level, box, 18, 31, palette);
        this.placeWaterCell(level, box, 24, 25, palette);
        this.placeWaterCell(level, box, 24, 31, palette);

        this.placeJobBlock(level, box, 22, 1, 28, withFacing(IUItem.apiary.getObject(0).get().defaultBlockState(), Direction.SOUTH));
        this.populateApiary(level, box, 22, 1, 28, random, gardenPlan);

        final List<GardenCropSpec> beds;
        switch (this.gardenVariant) {
            case SMALL -> beds = this.createGardenBedAllocation(gardenPlan, 4, random);
            case MEDIUM -> beds = this.createGardenBedAllocation(gardenPlan, 6, random);
            default -> beds = this.createGardenBedAllocation(gardenPlan, 8, random);
        }

        this.placeCropBed(level, box, 18, 20, 22, 24, beds.get(0));
        this.placeCropBed(level, box, 24, 26, 22, 24, beds.get(1));
        this.placeCropBed(level, box, 18, 20, 32, 34, beds.get(2));
        this.placeCropBed(level, box, 24, 26, 32, 34, beds.get(3));

        if (beds.size() >= 6) {
            this.placeCropBed(level, box, 14, 16, 25, 27, beds.get(4));
            this.placeCropBed(level, box, 28, 30, 29, 31, beds.get(5));
        }

        if (beds.size() >= 8) {
            this.placeCropBed(level, box, 14, 16, 21, 23, beds.get(6));
            this.placeCropBed(level, box, 28, 30, 33, 35, beds.get(7));
        }

        final BlockState[] flowers = this.getDecorFlowers(palette.style);
        this.placeFlowerPatch(level, box, 15, 35, 18, 37, flowers);
        this.placeFlowerPatch(level, box, 25, 24, 29, 26, flowers);
    }

    private void buildAnnexRoom(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final BlockState wallState,
            final VillagePalette palette,
            final int wallHeight
    ) {
        this.clearVolume(level, box, minX, 1, minZ, maxX, 10, maxZ);
        this.foundationRect(level, box, minX, minZ, maxX, maxZ, palette.foundation);
        this.generateBox(level, box, minX + 1, 0, minZ + 1, maxX - 1, 0, maxZ - 1, palette.floor, palette.floor, false);

        this.generateBox(level, box, minX, 1, minZ, maxX, wallHeight, maxZ, wallState, wallState, false);
        this.generateBox(level, box, minX + 1, 1, minZ + 1, maxX - 1, wallHeight - 1, maxZ - 1, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);

        for (int y = 1; y <= wallHeight; y++) {
            this.placeBlock(level, palette.pillar, minX, y, minZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, minZ, box);
            this.placeBlock(level, palette.pillar, minX, y, maxZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, maxZ, box);
        }

        for (int x = minX; x <= maxX; x++) {
            this.placeBlock(level, palette.trim, x, wallHeight, minZ, box);
            this.placeBlock(level, palette.trim, x, wallHeight, maxZ, box);
        }
        for (int z = minZ; z <= maxZ; z++) {
            this.placeBlock(level, palette.trim, minX, wallHeight, z, box);
            this.placeBlock(level, palette.trim, maxX, wallHeight, z, box);
        }

        this.buildVillageRoof(level, box, minX, wallHeight + 1, minZ, maxX, maxZ, palette);
    }

    private void buildInsetSecondFloor(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final BlockState wallState,
            final VillagePalette palette,
            final int bottomY,
            final int topY,
            final boolean useFlatRoof
    ) {
        this.generateBox(level, box, minX, bottomY - 1, minZ, maxX, bottomY - 1, maxZ, palette.floor, palette.floor, false);
        this.generateBox(level, box, minX, bottomY, minZ, maxX, topY, maxZ, wallState, wallState, false);
        this.generateBox(level, box, minX + 1, bottomY, minZ + 1, maxX - 1, topY - 1, maxZ - 1, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);

        for (int y = bottomY; y <= topY; y++) {
            this.placeBlock(level, palette.pillar, minX, y, minZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, minZ, box);
            this.placeBlock(level, palette.pillar, minX, y, maxZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, maxZ, box);
        }

        for (int x = minX; x <= maxX; x++) {
            this.placeBlock(level, palette.trim, x, topY, minZ, box);
            this.placeBlock(level, palette.trim, x, topY, maxZ, box);
        }
        for (int z = minZ; z <= maxZ; z++) {
            this.placeBlock(level, palette.trim, minX, topY, z, box);
            this.placeBlock(level, palette.trim, maxX, topY, z, box);
        }

        if (useFlatRoof || palette.flatRoof) {
            this.buildFlatRoof(level, box, minX, topY + 1, minZ, maxX, maxZ, palette);
        } else {
            this.buildGabledRoof(level, box, minX, topY + 1, minZ, maxX, maxZ, palette);
        }
    }

    private void buildFrontBalcony(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int maxX,
            final int z,
            final int y,
            final VillagePalette palette
    ) {
        for (int x = minX; x <= maxX; x++) {
            this.placeBlock(level, palette.woodSlabBlock.defaultBlockState(), x, y, z, box);
        }
        for (int x = minX; x <= maxX; x++) {
            if (x == minX || x == maxX) {
                this.placeBlock(level, palette.pillar, x, y - 1, z, box);
            }
            this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x, y + 1, z, box);
        }
    }

    private void buildChemistStairs(
            final WorldGenLevel level,
            final BoundingBox box,
            final int ladderX,
            final int ladderZ,
            final VillagePalette palette
    ) {
        this.clearVolume(level, box, ladderX, 1, ladderZ, ladderX, 5, ladderZ);
        this.placeBlock(level, Blocks.AIR.defaultBlockState(), ladderX, 4, ladderZ, box);
        this.placeBlock(level, Blocks.AIR.defaultBlockState(), ladderX, 5, ladderZ, box);

        final BlockState ladder = withFacing(Blocks.LADDER.defaultBlockState(), Direction.WEST);
        this.placeBlock(level, ladder, ladderX, 1, ladderZ, box);
        this.placeBlock(level, ladder, ladderX, 2, ladderZ, box);
        this.placeBlock(level, ladder, ladderX, 3, ladderZ, box);
        this.placeBlock(level, ladder, ladderX, 4, ladderZ, box);

        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), ladderX - 1, 5, ladderZ, box);
    }

    private void buildForgeWing(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final BlockState wallState,
            final VillagePalette palette
    ) {
        this.clearVolume(level, box, minX, 1, minZ, maxX, 9, maxZ);
        this.foundationRect(level, box, minX, minZ, maxX, maxZ, palette.foundation);
        this.generateBox(level, box, minX + 1, 0, minZ + 1, maxX - 1, 0, maxZ - 1, Blocks.POLISHED_ANDESITE.defaultBlockState(), Blocks.POLISHED_ANDESITE.defaultBlockState(), false);
        this.generateBox(level, box, minX, 1, minZ, maxX, 3, maxZ, wallState, wallState, false);
        this.generateBox(level, box, minX + 1, 1, minZ + 1, maxX - 1, 2, maxZ - 1, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);

        for (int y = 1; y <= 3; y++) {
            this.placeBlock(level, palette.pillar, minX, y, minZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, minZ, box);
            this.placeBlock(level, palette.pillar, minX, y, maxZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, maxZ, box);
        }

        this.buildFlatRoof(level, box, minX, 4, minZ, maxX, maxZ, palette);
    }

    private void buildGreenhouseAnnex(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final VillagePalette palette
    ) {
        this.clearVolume(level, box, minX, 1, minZ, maxX, 8, maxZ);
        this.foundationRect(level, box, minX, minZ, maxX, maxZ, palette.foundation);
        this.generateBox(level, box, minX + 1, 0, minZ + 1, maxX - 1, 0, maxZ - 1, palette.floor, palette.floor, false);

        for (int y = 1; y <= 3; y++) {
            this.placeBlock(level, palette.pillar, minX, y, minZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, minZ, box);
            this.placeBlock(level, palette.pillar, minX, y, maxZ, box);
            this.placeBlock(level, palette.pillar, maxX, y, maxZ, box);
        }

        for (int x = minX + 1; x <= maxX - 1; x++) {
            this.placeBlock(level, palette.window, x, 1, minZ, box);
            this.placeBlock(level, palette.window, x, 2, minZ, box);
            this.placeBlock(level, palette.window, x, 1, maxZ, box);
            this.placeBlock(level, palette.window, x, 2, maxZ, box);
        }
        for (int z = minZ + 1; z <= maxZ - 1; z++) {
            this.placeBlock(level, palette.window, minX, 1, z, box);
            this.placeBlock(level, palette.window, minX, 2, z, box);
            this.placeBlock(level, palette.window, maxX, 1, z, box);
            this.placeBlock(level, palette.window, maxX, 2, z, box);
        }

        this.generateBox(level, box, minX, 3, minZ, maxX, 3, maxZ, palette.trim, palette.trim, false);
        this.generateBox(level, box, minX, 4, minZ, maxX, 4, maxZ, palette.window, palette.window, false);
        for (int x = minX; x <= maxX; x++) {
            this.placeBlock(level, withFacing(palette.roofStairsBlock.defaultBlockState(), Direction.NORTH), x, 4, minZ - 1, box);
            this.placeBlock(level, withFacing(palette.roofStairsBlock.defaultBlockState(), Direction.SOUTH), x, 4, maxZ + 1, box);
        }
    }

    private GardenPlan createBotanistGardenPlan(final WorldGenLevel level, final RandomSource random) {
        final BlockPos apiaryCenter = new BlockPos(this.getWorldX(22, 28), this.getWorldY(1), this.getWorldZ(22, 28));

        final List<Bee> biomeBees = this.selectBiomeBees(level, apiaryCenter, random);

        final List<GardenCropSpec> beeFlowerPool = new ArrayList<>();
        final Set<String> usedBeeFlowerIds = new HashSet<>();

        for (Bee bee : biomeBees) {
            final GardenCropSpec spec = this.specFromBeeFlower(bee.getCropFlower());
            if (spec != null && usedBeeFlowerIds.add(spec.id)) {
                beeFlowerPool.add(spec);
            }
        }

        if (beeFlowerPool.isEmpty()) {
            beeFlowerPool.add(this.modCrop("dandelion", 12, CropInit.dandelion.getSoil()));
            beeFlowerPool.add(this.modCrop("poppy", 6, CropInit.poppy.getSoil()));
        }

        final GardenCropSpec primaryBeeCrop = beeFlowerPool.get(0);
        final GardenCropSpec secondaryBeeCrop = beeFlowerPool.size() > 1 ? beeFlowerPool.get(1) : primaryBeeCrop;

        final List<GardenCropSpec> commonPool = new ArrayList<>();
        commonPool.add(this.vanillaCrop("wheat", () -> new ItemStack(Items.WHEAT_SEEDS), EnumSoil.FARMLAND));
        commonPool.add(this.vanillaCrop("carrot", () -> new ItemStack(Items.CARROT), EnumSoil.FARMLAND));
        commonPool.add(this.vanillaCrop("beetroot", () -> new ItemStack(Items.BEETROOT_SEEDS), EnumSoil.FARMLAND));
        commonPool.add(this.vanillaCrop("potato", () -> new ItemStack(Items.POTATO), EnumSoil.FARMLAND));
        commonPool.add(this.modCrop("corn", 16, CropInit.corn.getSoil()));
        commonPool.add(this.modCrop("tomato", 22, CropInit.tomato.getSoil()));
        commonPool.add(this.modCrop("raspberry", 20, CropInit.raspberry.getSoil()));
        commonPool.add(this.modCrop("hops", 21, CropInit.hops.getSoil()));
        commonPool.add(this.modCrop("onion", 7, CropInit.onion.getSoil()));
        commonPool.add(this.modCrop("chamomile", 10, CropInit.chamomile.getSoil()));
        commonPool.add(this.modCrop("blue_orchid", 15, CropInit.blue_orchid.getSoil()));
        commonPool.add(this.modCrop("tulip_white", 11, CropInit.tulip_white.getSoil()));
        commonPool.add(this.modCrop("tulip_red", 13, CropInit.tulip_red.getSoil()));

        final List<GardenCropSpec> industrialPool = new ArrayList<>();
        industrialPool.add(this.modCrop("copper_heart", 44, CropInit.copper_heart.getSoil()));
        industrialPool.add(this.modCrop("iron_crimson", 46, CropInit.iron_crimson.getSoil()));
        industrialPool.add(this.modCrop("gold_astral", 45, CropInit.gold_astral.getSoil()));
        industrialPool.add(this.modCrop("quartz_storm", 49, CropInit.quartz_storm.getSoil()));
        industrialPool.add(this.modCrop("tin_ghost", 52, CropInit.tin_ghost.getSoil()));
        industrialPool.add(this.modCrop("lead_stream", 51, CropInit.lead_stream.getSoil()));
        industrialPool.add(this.modCrop("silicon_avalanche", 74, CropInit.silicon_avalanche.getSoil()));
        industrialPool.add(this.modCrop("mikhailovskaya_lavra", 25, CropInit.mikhailovskaya_lavra.getSoil()));
        industrialPool.add(this.modCrop("aluminum_plutonka", 26, CropInit.aluminum_plutonka.getSoil()));

        final List<GardenCropSpec> supportCrops = new ArrayList<>();
        final Set<String> usedSupport = new HashSet<>();
        usedSupport.add(primaryBeeCrop.id);
        usedSupport.add(secondaryBeeCrop.id);

        supportCrops.add(this.takeUnique(commonPool, usedSupport, random,
                this.vanillaCrop("wheat", () -> new ItemStack(Items.WHEAT_SEEDS), EnumSoil.FARMLAND)));
        supportCrops.add(this.takeUnique(commonPool, usedSupport, random,
                this.vanillaCrop("carrot", () -> new ItemStack(Items.CARROT), EnumSoil.FARMLAND)));

        if (random.nextInt(100) < 35) {
            supportCrops.add(this.takeUnique(industrialPool, usedSupport, random,
                    this.modCrop("copper_heart", 44, CropInit.copper_heart.getSoil())));
        } else {
            supportCrops.add(this.takeUnique(commonPool, usedSupport, random,
                    this.modCrop("tomato", 22, CropInit.tomato.getSoil())));
        }

        supportCrops.add(this.takeUnique(commonPool, usedSupport, random,
                this.modCrop("corn", 16, CropInit.corn.getSoil())));
        supportCrops.add(this.takeUnique(commonPool, usedSupport, random,
                this.modCrop("raspberry", 20, CropInit.raspberry.getSoil())));

        return new GardenPlan(biomeBees, primaryBeeCrop, secondaryBeeCrop, supportCrops);
    }

    private List<GardenCropSpec> createGardenBedAllocation(
            final GardenPlan plan,
            final int totalBeds,
            final RandomSource random
    ) {
        final List<GardenCropSpec> result = new ArrayList<>(totalBeds);

        final int primaryBeds = (totalBeds + 1) / 2;
        for (int i = 0; i < primaryBeds; i++) {
            result.add(plan.primaryBeeCrop);
        }

        boolean secondaryPlaced = false;
        int supportIndex = 0;

        while (result.size() < totalBeds) {
            if (!secondaryPlaced
                    && plan.secondaryBeeCrop != null
                    && !plan.secondaryBeeCrop.id.equals(plan.primaryBeeCrop.id)) {
                result.add(plan.secondaryBeeCrop);
                secondaryPlaced = true;
                continue;
            }

            if (!plan.supportCrops.isEmpty()) {
                result.add(plan.supportCrops.get(supportIndex % plan.supportCrops.size()));
                supportIndex++;
            } else {
                result.add(plan.primaryBeeCrop);
            }
        }

        return result;
    }

    private List<Bee> selectBiomeBees(final WorldGenLevel level, final BlockPos pos, final RandomSource random) {
        final List<Bee> candidates = new ArrayList<>();
        final ResourceKey<Biome> biomeKey = level.getBiome(pos).unwrapKey().orElse(null);

        this.tryAddBeeCandidate(candidates, BeeInit.FOREST_BEE, biomeKey);
        this.tryAddBeeCandidate(candidates, BeeInit.PLAINS_BEE, biomeKey);
        this.tryAddBeeCandidate(candidates, BeeInit.SWAMP_BEE, biomeKey);
        this.tryAddBeeCandidate(candidates, BeeInit.TROPICAL_BEE, biomeKey);
        this.tryAddBeeCandidate(candidates, BeeInit.WINTER_BEE, biomeKey);

        if (candidates.isEmpty()) {
            candidates.add(BeeInit.FOREST_BEE);
            candidates.add(BeeInit.PLAINS_BEE);
        }

        final List<Bee> result = new ArrayList<>();
        while (!candidates.isEmpty() && result.size() < 2) {
            result.add(candidates.remove(random.nextInt(candidates.size())));
        }

        return result;
    }

    private void tryAddBeeCandidate(final List<Bee> list, final Bee bee, final ResourceKey<Biome> biomeKey) {
        if (bee == null) {
            return;
        }
        if (biomeKey == null || bee.canWorkInBiome(biomeKey)) {
            list.add(bee);
        }
    }

    private GardenCropSpec specFromBeeFlower(final Crop crop) {
        if (crop == null) {
            return null;
        }

        final EnumSoil soil = crop.getSoil() == null ? EnumSoil.FARMLAND : crop.getSoil();

        if (crop == CropInit.dandelion) return this.modCrop("dandelion", 12, soil);
        if (crop == CropInit.poppy) return this.modCrop("poppy", 6, soil);
        if (crop == CropInit.chamomile) return this.modCrop("chamomile", 10, soil);
        if (crop == CropInit.blue_orchid) return this.modCrop("blue_orchid", 15, soil);
        if (crop == CropInit.tulip_red) return this.modCrop("tulip_red", 13, soil);
        if (crop == CropInit.tulip_white) return this.modCrop("tulip_white", 11, soil);
        if (crop == CropInit.tulip_orange) return this.modCrop("tulip_orange", 9, soil);
        if (crop == CropInit.tulip_pink) return this.modCrop("tulip_pink", 4, soil);
        if (crop == CropInit.haustonia_gray) return this.modCrop("haustonia_gray", 5, soil);
        if (crop == CropInit.onion) return this.modCrop("onion", 7, soil);
        if (crop == CropInit.red_mushroom) return this.modCrop("red_mushroom", 14, soil);
        if (crop == CropInit.brown_mushroom) return this.modCrop("brown_mushroom", 8, soil);
        if (crop == CropInit.wheat_seed) return this.vanillaCrop("wheat", () -> new ItemStack(Items.WHEAT_SEEDS), soil);
        if (crop == CropInit.carrot) return this.vanillaCrop("carrot", () -> new ItemStack(Items.CARROT), soil);
        if (crop == CropInit.beet) return this.vanillaCrop("beetroot", () -> new ItemStack(Items.BEETROOT_SEEDS), soil);
        if (crop == CropInit.potato) return this.vanillaCrop("potato", () -> new ItemStack(Items.POTATO), soil);
        if (crop == CropInit.corn) return this.modCrop("corn", 16, soil);
        if (crop == CropInit.tomato) return this.modCrop("tomato", 22, soil);
        if (crop == CropInit.raspberry) return this.modCrop("raspberry", 20, soil);
        if (crop == CropInit.hops) return this.modCrop("hops", 21, soil);
        if (crop == CropInit.melon) return this.modCrop("melon", 23, soil);
        if (crop == CropInit.pumpkin) return this.modCrop("pumpkin", 24, soil);

        return null;
    }

    private GardenCropSpec takeUnique(
            final List<GardenCropSpec> pool,
            final Set<String> used,
            final RandomSource random,
            final GardenCropSpec fallback
    ) {
        final List<GardenCropSpec> candidates = new ArrayList<>();
        for (GardenCropSpec spec : pool) {
            if (!used.contains(spec.id)) {
                candidates.add(spec);
            }
        }

        final GardenCropSpec picked = candidates.isEmpty() ? fallback : candidates.get(random.nextInt(candidates.size()));
        used.add(picked.id);
        return picked;
    }

    private GardenCropSpec vanillaCrop(final String id, final Supplier<ItemStack> seedSupplier, final EnumSoil soil) {
        return new GardenCropSpec(id, seedSupplier, this.normalizeSoilState(soil.getState()));
    }

    private GardenCropSpec modCrop(final String id, final int cropId, final EnumSoil soil) {
        return new GardenCropSpec(
                id,
                () -> IUItem.crops.getStack(0).getCrop(cropId).copy(),
                this.normalizeSoilState(soil.getState())
        );
    }

    private BlockState normalizeSoilState(final BlockState state) {
        if (state.hasProperty(FarmBlock.MOISTURE)) {
            return state.setValue(FarmBlock.MOISTURE, 7);
        }
        return state;
    }

    private void buildNuclearLab(
            final WorldGenLevel level,
            final BoundingBox box,
            final RandomSource random,
            final ChunkPos chunkPos,
            final VillagePalette palette
    ) {
        final BlockState shell = Blocks.DEEPSLATE_BRICKS.defaultBlockState();

        this.clearVolume(level, box, 41, 1, 19, 55, 13, 33);
        this.prepareGroundRect(level, box, 41, 19, 55, 33, palette);
        this.paveRect(level, box, 40, 29, 50, 31, palette.path, palette.path);
        this.foundationRect(level, box, 42, 21, 54, 31, shell);
        this.generateBox(level, box, 43, 0, 22, 53, 0, 30, Blocks.POLISHED_ANDESITE.defaultBlockState(), Blocks.POLISHED_ANDESITE.defaultBlockState(), false);
        this.generateBox(level, box, 42, 1, 21, 54, 6, 31, shell, shell, false);
        this.generateBox(level, box, 43, 1, 22, 53, 5, 30, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);

        for (int y = 1; y <= 7; y++) {
            this.placeBlock(level, shell, 42, y, 21, box);
            this.placeBlock(level, shell, 54, y, 21, box);
            this.placeBlock(level, shell, 42, y, 31, box);
            this.placeBlock(level, shell, 54, y, 31, box);
        }

        if (palette.flatRoof) {
            this.buildFlatRoof(level, box, 42, 7, 21, 54, 31, palette);
        } else {
            this.buildGabledRoof(level, box, 42, 7, 21, 54, 31, palette);
        }

        this.placeDoorway(level, box, 48, 1, 31, Direction.SOUTH, palette.doorBlock);
        this.placeBlock(level, Blocks.TINTED_GLASS.defaultBlockState(), 44, 3, 21, box);
        this.placeBlock(level, Blocks.TINTED_GLASS.defaultBlockState(), 52, 3, 21, box);
        this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState(), 48, 3, 21, box);

        for (int x = 44; x <= 52; x++) {
            final BlockState stripe = x % 2 == 0 ? Blocks.YELLOW_CONCRETE.defaultBlockState() : Blocks.BLACK_CONCRETE.defaultBlockState();
            this.placeBlock(level, stripe, x, 1, 22, box);
        }

        this.placeJobBlock(level, box, 45, 1, 24,
                withFacing(IUItem.basemachine.getObject(BlockBaseMachine1Entity.enrichment.getId()).get().defaultBlockState(), Direction.SOUTH));
        this.placeBlock(level, Blocks.IRON_BLOCK.defaultBlockState(), 50, 1, 24, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), 52, 1, 24, box);
        this.placeBlock(level, Blocks.LIGHTNING_ROD.defaultBlockState(), 48, 1, 27, box);
        this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState(), 47, 1, 25, box);
        this.placeBlock(level, Blocks.IRON_BARS.defaultBlockState(), 49, 1, 25, box);
        this.placeCeilingLantern(level, box, 48, 5, 27);
        this.placeBed(level, box, 43, 1, 29, Direction.EAST, Blocks.YELLOW_BED);

        this.placeLanternPost(level, box, 43, 1, 20, palette);
        this.placeLanternPost(level, box, 54, 1, 20, palette);
        this.placeLanternPost(level, box, 43, 1, 32, palette);
        this.placeLanternPost(level, box, 54, 1, 32, palette);

        this.tryCreateChest(level, box, random, chunkPos, 53, 1, 29, BuiltInLootTables.SIMPLE_DUNGEON);
        this.trySpawnVillager(level, box, chunkPos, 48, 1, 28, VillagerInit.NUCLEAR.get());
    }

    private void buildOpenStorageYard(
            final WorldGenLevel level,
            final BoundingBox box,
            final RandomSource random,
            final ChunkPos chunkPos,
            final VillagePalette palette
    ) {
        this.prepareGroundRect(level, box, 41, 19, 55, 33, palette);
        this.fenceYard(level, box, 42, 20, 54, 32, palette);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), 48, 1, 20, box);
        this.placeBlock(level, palette.gateBlock.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH), 48, 1, 32, box);
        this.buildWorkshopHouse(level, box, 43, 21, 53, 29, palette, 4);
        this.placeDoorway(level, box, 48, 1, 29, Direction.SOUTH, palette.doorBlock);
        this.buildCoveredPorch(level, box, 46, 50, 30, palette);

        this.placeBlock(level, Blocks.CHEST.defaultBlockState(), 44, 1, 23, box);
        this.placeBlock(level, Blocks.CHEST.defaultBlockState(), 45, 1, 23, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), 46, 1, 23, box);
        this.placeBlock(level, Blocks.CHEST.defaultBlockState(), 49, 1, 23, box);
        this.placeBlock(level, Blocks.CHEST.defaultBlockState(), 50, 1, 23, box);
        this.placeBlock(level, Blocks.SMOKER.defaultBlockState(), 51, 1, 23, box);
        this.placeBlock(level, Blocks.CHEST.defaultBlockState(), 52, 1, 23, box);
        this.placeBlock(level, Blocks.CRAFTING_TABLE.defaultBlockState(), 45, 1, 26, box);
        this.placeBlock(level, Blocks.HAY_BLOCK.defaultBlockState(), 44, 1, 26, box);
        this.placeBlock(level, Blocks.HAY_BLOCK.defaultBlockState(), 44, 2, 26, box);
        this.placeBlock(level, Blocks.HAY_BLOCK.defaultBlockState(), 45, 1, 26, box);
        this.placeBlock(level, Blocks.HAY_BLOCK.defaultBlockState(), 49, 1, 26, box);
        this.placeBlock(level, Blocks.HAY_BLOCK.defaultBlockState(), 50, 1, 26, box);
        this.placeBlock(level, Blocks.HAY_BLOCK.defaultBlockState(), 50, 2, 26, box);
        this.placeBlock(level, Blocks.HAY_BLOCK.defaultBlockState(), 51, 1, 26, box);
        this.placeBlock(level, Blocks.HAY_BLOCK.defaultBlockState(), 52, 1, 26, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), 47, 1, 27, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), 48, 1, 27, box);
        this.placeCeilingLantern(level, box, 48, 4, 25);

        this.paveRect(level, box, 40, 29, 50, 31, palette.path, palette.path);
        this.placeBlock(level, palette.pathBorder, 48, 0, 32, box);
        this.placeLanternPost(level, box, 43, 1, 20, palette);
        this.placeLanternPost(level, box, 54, 1, 20, palette);
        this.placeLanternPost(level, box, 43, 1, 32, palette);
        this.placeLanternPost(level, box, 54, 1, 32, palette);

        this.tryCreateChest(level, box, random, chunkPos, 52, 1, 23, BuiltInLootTables.VILLAGE_PLAINS_HOUSE);
    }

    private void decorateRoadside(final WorldGenLevel level, final BoundingBox box, final VillagePalette palette) {
        final BlockState[] flowers = this.getDecorFlowers(palette.style);
        this.placeFlowerPatch(level, box, 2, 0, 9, 0, flowers);
        this.placeFlowerPatch(level, box, 48, 0, 55, 0, flowers);
        this.placeFlowerPatch(level, box, 0, 36, 6, 41, flowers);
        this.placeFlowerPatch(level, box, 51, 36, 57, 41, flowers);
    }

    private BlockState[] getDecorFlowers(final BiomeStyle style) {
        return switch (style) {
            case DESERT -> new BlockState[]{
                    Blocks.DANDELION.defaultBlockState(),
                    Blocks.POPPY.defaultBlockState(),
                    Blocks.ALLIUM.defaultBlockState(),
                    Blocks.AZURE_BLUET.defaultBlockState(),
                    Blocks.CORNFLOWER.defaultBlockState()
            };
            case SAVANNA -> new BlockState[]{
                    Blocks.DANDELION.defaultBlockState(),
                    Blocks.ORANGE_TULIP.defaultBlockState(),
                    Blocks.RED_TULIP.defaultBlockState(),
                    Blocks.AZURE_BLUET.defaultBlockState(),
                    Blocks.FERN.defaultBlockState()
            };
            case SNOWY -> new BlockState[]{
                    Blocks.DANDELION.defaultBlockState(),
                    Blocks.AZURE_BLUET.defaultBlockState(),
                    Blocks.OXEYE_DAISY.defaultBlockState(),
                    Blocks.WHITE_TULIP.defaultBlockState(),
                    Blocks.CORNFLOWER.defaultBlockState()
            };
            case SWAMP -> new BlockState[]{
                    Blocks.BLUE_ORCHID.defaultBlockState(),
                    Blocks.AZURE_BLUET.defaultBlockState(),
                    Blocks.DANDELION.defaultBlockState(),
                    Blocks.POPPY.defaultBlockState(),
                    Blocks.CORNFLOWER.defaultBlockState()
            };
            case JUNGLE -> new BlockState[]{
                    Blocks.JUNGLE_SAPLING.defaultBlockState(),
                    Blocks.POPPY.defaultBlockState(),
                    Blocks.DANDELION.defaultBlockState(),
                    Blocks.CORNFLOWER.defaultBlockState(),
                    Blocks.FERN.defaultBlockState()
            };
            default -> new BlockState[]{
                    Blocks.DANDELION.defaultBlockState(),
                    Blocks.POPPY.defaultBlockState(),
                    Blocks.AZURE_BLUET.defaultBlockState(),
                    Blocks.OXEYE_DAISY.defaultBlockState(),
                    Blocks.CORNFLOWER.defaultBlockState()
            };
        };
    }

    private void prepareGroundRect(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final VillagePalette palette
    ) {
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                this.placeBlock(level, palette.ground, x, 0, z, box);
                this.fillColumnDown(level, palette.subsoil, x, -1, z, box);
            }
        }
    }

    private void prepareGardenArea(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final VillagePalette palette
    ) {
        this.clearVolume(level, box, minX, 1, minZ, maxX, 8, maxZ);

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                this.placeBlock(level, palette.ground, x, 0, z, box);
                this.fillColumnDown(level, palette.subsoil, x, -1, z, box);
            }
        }
    }

    private void buildGardenFence(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final int gateX,
            final VillagePalette palette
    ) {
        for (int x = minX; x <= maxX; x++) {
            if (x == gateX) {
                this.placeBlock(level, palette.gateBlock.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH), x, 1, minZ, box);
            } else {
                this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x, 1, minZ, box);
            }
            this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x, 1, maxZ, box);
            this.fillColumnDown(level, palette.pathBorder, x, 0, minZ, box);
            this.fillColumnDown(level, palette.pathBorder, x, 0, maxZ, box);
        }

        for (int z = minZ; z <= maxZ; z++) {
            this.placeBlock(level, palette.fenceBlock.defaultBlockState(), minX, 1, z, box);
            this.placeBlock(level, palette.fenceBlock.defaultBlockState(), maxX, 1, z, box);
            this.fillColumnDown(level, palette.pathBorder, minX, 0, z, box);
            this.fillColumnDown(level, palette.pathBorder, maxX, 0, z, box);
        }
    }

    private void placeWaterCell(final WorldGenLevel level, final BoundingBox box, final int x, final int z, final VillagePalette palette) {
        this.placeBlock(level, Blocks.WATER.defaultBlockState(), x, 0, z, box);
        this.fillColumnDown(level, palette.pathBorder, x, -1, z, box);
    }

    private void placeCropBed(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int maxX,
            final int minZ,
            final int maxZ,
            final GardenCropSpec spec
    ) {
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                this.placeCrop(level, box, x, 1, z, spec);
            }
        }
    }

    private void placeCourtyardFountain(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final VillagePalette palette
    ) {
        this.generateBox(level, box, minX, 0, minZ, minX + 5, 0, minZ + 5, palette.pathBorder, palette.pathBorder, false);

        for (int x = minX; x <= minX + 5; x++) {
            this.placeBlock(level, palette.pathBorder, x, 1, minZ, box);
            this.placeBlock(level, palette.pathBorder, x, 1, minZ + 5, box);
        }
        for (int z = minZ; z <= minZ + 5; z++) {
            this.placeBlock(level, palette.pathBorder, minX, 1, z, box);
            this.placeBlock(level, palette.pathBorder, minX + 5, 1, z, box);
        }

        for (int x = minX + 1; x <= minX + 4; x++) {
            for (int z = minZ + 1; z <= minZ + 4; z++) {
                this.placeBlock(level, Blocks.WATER.defaultBlockState(), x, 1, z, box);
            }
        }

        this.placeBlock(level, Blocks.STONE_BRICK_WALL.defaultBlockState(), minX + 2, 2, minZ + 2, box);
        this.placeBlock(level, Blocks.LANTERN.defaultBlockState(), minX + 2, 3, minZ + 2, box);
    }

    private void placeMiniGardenFountain(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final VillagePalette palette
    ) {
        this.generateBox(level, box, minX, 0, minZ, minX + 2, 0, minZ + 2, palette.pathBorder, palette.pathBorder, false);

        this.placeBlock(level, palette.pathBorder, minX, 0, minZ, box);
        this.placeBlock(level, palette.pathBorder, minX + 1, 0, minZ, box);
        this.placeBlock(level, palette.pathBorder, minX + 2, 0, minZ, box);
        this.placeBlock(level, palette.pathBorder, minX, 0, minZ + 2, box);
        this.placeBlock(level, palette.pathBorder, minX + 1, 0, minZ + 2, box);
        this.placeBlock(level, palette.pathBorder, minX + 2, 0, minZ + 2, box);
        this.placeBlock(level, palette.pathBorder, minX, 0, minZ + 1, box);
        this.placeBlock(level, palette.pathBorder, minX + 2, 0, minZ + 1, box);
        this.placeBlock(level, Blocks.WATER.defaultBlockState(), minX + 1, 0, minZ + 1, box);
        this.placeBlock(level, Blocks.COBBLESTONE_WALL.defaultBlockState(), minX + 1, 1, minZ + 1, box);
    }

    private void placeCrop(
            final WorldGenLevel level,
            final BoundingBox box,
            final int x,
            final int y,
            final int z,
            final GardenCropSpec spec
    ) {
        final BlockState soilState = spec.soilState;
        final BlockState supportState = this.getSupportState(soilState);

        this.placeBlock(level, soilState, x, y - 1, z, box);
        this.fillColumnDown(level, supportState, x, y - 2, z, box);

        final BlockState cropStake = IUItem.crop.getBlock().defaultBlockState();
        this.placeBlock(level, cropStake, x, y, z, box);

        final BlockPos worldPos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
        if (!box.isInside(worldPos)) {
            return;
        }

        final BlockEntity blockEntity = level.getBlockEntity(worldPos);
        if (blockEntity instanceof TileEntityCrop tileEntityCrop) {
            final ItemStack stack = spec.seedSupplier.get();
            if (!stack.isEmpty()) {
                tileEntityCrop.plantNewCrop(stack);
                tileEntityCrop.setChanged();
            }
        }
    }

    private BlockState getSupportState(final BlockState soilState) {
        if (soilState.is(Blocks.FARMLAND) || soilState.is(Blocks.MYCELIUM) || soilState.is(Blocks.GRASS_BLOCK)) {
            return Blocks.DIRT.defaultBlockState();
        }
        if (soilState.is(Blocks.SAND) || soilState.is(Blocks.GRAVEL) || soilState.is(Blocks.SOUL_SAND)) {
            return Blocks.DIRT.defaultBlockState();
        }
        return Blocks.COBBLESTONE.defaultBlockState();
    }

    private void populateApiary(
            final WorldGenLevel level,
            final BoundingBox box,
            final int x,
            final int y,
            final int z,
            final RandomSource random,
            final GardenPlan gardenPlan
    ) {
        final BlockPos worldPos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
        if (!box.isInside(worldPos)) {
            return;
        }

        final BlockEntity blockEntity = level.getBlockEntity(worldPos);
        if (blockEntity instanceof BlockEntityApiary apiary) {
            ApiaryStructureHelper.populateForSelectedBees(apiary, gardenPlan.bees, random);
        }
    }

    private void placeFlowerPatch(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final BlockState[] flowers
    ) {
        int index = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if ((x + z) % 2 == 0) {
                    this.placeBlock(level, Blocks.GRASS_BLOCK.defaultBlockState(), x, 0, z, box);
                    this.placeBlock(level, flowers[index % flowers.length], x, 1, z, box);
                    index++;
                }
            }
        }
    }

    private void placePlanter(
            final WorldGenLevel level,
            final BoundingBox box,
            final int x,
            final int z,
            final VillagePalette palette,
            final BlockState flower
    ) {
        this.placeBlock(level, palette.pathBorder, x, 0, z, box);
        this.placeBlock(level, Blocks.BARREL.defaultBlockState(), x, 1, z, box);
        this.placeBlock(level, flower, x, 2, z, box);
    }

    private void placeVillageBellFrame(
            final WorldGenLevel level,
            final BoundingBox box,
            final int x,
            final int y,
            final int z,
            final VillagePalette palette
    ) {
        this.placeBlock(level, palette.pillar, x - 1, y, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x - 1, y + 1, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x - 1, y + 2, z, box);
        this.placeBlock(level, palette.pillar, x + 1, y, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x + 1, y + 1, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x + 1, y + 2, z, box);
        this.placeBlock(level, palette.trim, x - 1, y + 3, z, box);
        this.placeBlock(level, palette.trim, x, y + 3, z, box);
        this.placeBlock(level, palette.trim, x + 1, y + 3, z, box);
        this.placeBlock(level, Blocks.BELL.defaultBlockState(), x, y + 2, z, box);
        this.fillColumnDown(level, palette.pathBorder, x - 1, y - 1, z, box);
        this.fillColumnDown(level, palette.pathBorder, x + 1, y - 1, z, box);
    }

    private void placeLanternPost(final WorldGenLevel level, final BoundingBox box, final int x, final int y, final int z, final VillagePalette palette) {
        this.placeBlock(level, palette.pathBorder, x, y, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x, y + 1, z, box);
        this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x, y + 2, z, box);
        this.placeBlock(level, Blocks.LANTERN.defaultBlockState(), x, y + 3, z, box);
        this.fillColumnDown(level, palette.pathBorder, x, y - 1, z, box);
    }

    private void placeCeilingLantern(final WorldGenLevel level, final BoundingBox box, final int x, final int y, final int z) {
        this.placeBlock(level, Blocks.CHAIN.defaultBlockState(), x, y, z, box);
        this.placeBlock(level, Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true), x, y - 1, z, box);
    }

    private void placeBench(
            final WorldGenLevel level,
            final BoundingBox box,
            final int x,
            final int y,
            final int z,
            final Direction facing,
            final VillagePalette palette
    ) {
        final BlockState stair = withFacing(
                palette.woodStairsBlock.defaultBlockState().setValue(StairBlock.HALF, net.minecraft.world.level.block.state.properties.Half.BOTTOM),
                facing
        );

        this.placeBlock(level, stair, x, y, z, box);

        if (facing == Direction.EAST || facing == Direction.WEST) {
            this.placeBlock(level, stair, x, y, z + 1, box);
        } else {
            this.placeBlock(level, stair, x + 1, y, z, box);
        }
    }

    private void clearVolume(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minY,
            final int minZ,
            final int maxX,
            final int maxY,
            final int maxZ
    ) {
        this.generateBox(level, box, minX, minY, minZ, maxX, maxY, maxZ,
                Blocks.AIR.defaultBlockState(),
                Blocks.AIR.defaultBlockState(),
                false);
    }

    private void foundationRect(final WorldGenLevel level, final BoundingBox box, final int minX, final int minZ, final int maxX, final int maxZ, final BlockState state) {
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                this.placeBlock(level, state, x, 0, z, box);
                this.fillColumnDown(level, state, x, -1, z, box);
            }
        }
    }

    private void paveRect(final WorldGenLevel level, final BoundingBox box, final int minX, final int minZ, final int maxX, final int maxZ, final BlockState middle, final BlockState border) {
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                final boolean edge = x == minX || x == maxX || z == minZ || z == maxZ;
                this.placeBlock(level, Blocks.AIR.defaultBlockState(), x, 1, z, box);
                this.placeBlock(level, Blocks.AIR.defaultBlockState(), x, 2, z, box);
                this.placeBlock(level, edge ? border : middle, x, 0, z, box);
                this.fillColumnDown(level, border, x, -1, z, box);
            }
        }
    }

    private void fenceYard(
            final WorldGenLevel level,
            final BoundingBox box,
            final int minX,
            final int minZ,
            final int maxX,
            final int maxZ,
            final VillagePalette palette
    ) {
        for (int x = minX; x <= maxX; x++) {
            this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x, 1, minZ, box);
            this.placeBlock(level, palette.fenceBlock.defaultBlockState(), x, 1, maxZ, box);
            this.fillColumnDown(level, palette.pathBorder, x, 0, minZ, box);
            this.fillColumnDown(level, palette.pathBorder, x, 0, maxZ, box);
        }

        for (int z = minZ; z <= maxZ; z++) {
            this.placeBlock(level, palette.fenceBlock.defaultBlockState(), minX, 1, z, box);
            this.placeBlock(level, palette.fenceBlock.defaultBlockState(), maxX, 1, z, box);
            this.fillColumnDown(level, palette.pathBorder, minX, 0, z, box);
            this.fillColumnDown(level, palette.pathBorder, maxX, 0, z, box);
        }

        this.placeBlock(level, palette.gateBlock.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH), minX + 6, 1, minZ, box);
    }

    private void placeDoorway(
            final WorldGenLevel level,
            final BoundingBox box,
            final int x,
            final int y,
            final int z,
            final Direction facing,
            final Block doorBlock
    ) {
        this.placeBlock(level, Blocks.AIR.defaultBlockState(), x, y, z, box);
        this.placeBlock(level, Blocks.AIR.defaultBlockState(), x, y + 1, z, box);

        final BlockState lower = doorBlock.defaultBlockState()
                .setValue(DoorBlock.FACING, facing)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                .setValue(DoorBlock.OPEN, false);

        final BlockState upper = lower.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);

        this.placeBlock(level, lower, x, y, z, box);
        this.placeBlock(level, upper, x, y + 1, z, box);
        this.fillColumnDown(level, Blocks.COBBLESTONE.defaultBlockState(), x, y - 1, z, box);
    }

    private void placeWindow(final WorldGenLevel level, final BoundingBox box, final int x, final int y, final int z, final BlockState glass) {
        this.placeBlock(level, glass, x, y, z, box);
        this.placeBlock(level, glass, x, y + 1, z, box);
    }

    private void placeJobBlock(final WorldGenLevel level, final BoundingBox box, final int x, final int y, final int z, final BlockState state) {
        this.placeBlock(level, state, x, y, z, box);
        this.fillColumnDown(level, Blocks.COBBLESTONE.defaultBlockState(), x, y - 1, z, box);
    }

    private void placeBed(final WorldGenLevel level, final BoundingBox box, final int x, final int y, final int z, final Direction facing, final Block bedBlock) {
        final BlockState foot = bedBlock.defaultBlockState()
                .setValue(BedBlock.FACING, facing)
                .setValue(BedBlock.PART, BedPart.FOOT)
                .setValue(BedBlock.OCCUPIED, false);

        final BlockState head = bedBlock.defaultBlockState()
                .setValue(BedBlock.FACING, facing)
                .setValue(BedBlock.PART, BedPart.HEAD)
                .setValue(BedBlock.OCCUPIED, false);

        this.placeBlock(level, foot, x, y, z, box);
        this.placeBlock(level, head, x + facing.getStepX(), y, z + facing.getStepZ(), box);
    }

    private void tryCreateChest(
            final WorldGenLevel level,
            final BoundingBox box,
            final RandomSource random,
            final ChunkPos currentChunk,
            final int x,
            final int y,
            final int z,
            final ResourceLocation lootTable
    ) {
        final BlockPos worldPos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
        if (!box.isInside(worldPos) || !new ChunkPos(worldPos).equals(currentChunk)) {
            return;
        }

        this.createChest(level, box, random, x, y, z, lootTable);
    }

    private void trySpawnVillager(
            final WorldGenLevel level,
            final BoundingBox box,
            final ChunkPos currentChunk,
            final int x,
            final int y,
            final int z,
            final VillagerProfession profession
    ) {
        final BlockPos worldPos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
        if (!box.isInside(worldPos) || !new ChunkPos(worldPos).equals(currentChunk)) {
            return;
        }

        final ServerLevel serverLevel = level.getLevel();
        final Villager villager = EntityType.VILLAGER.create(serverLevel);
        if (villager == null) {
            return;
        }

        villager.moveTo(worldPos.getX() + 0.5D, worldPos.getY(), worldPos.getZ() + 0.5D, 0.0F, 0.0F);
        villager.finalizeSpawn(level, level.getCurrentDifficultyAt(worldPos), MobSpawnType.STRUCTURE, null, null);
        villager.setVillagerData(villager.getVillagerData().setProfession(profession));
        level.addFreshEntityWithPassengers(villager);
    }

    private enum ProfessionHouseKind {
        ENGINEER,
        MECHANIC,
        METALLURG,
        CHEMIST,
        BOTANIST
    }

    private enum GardenVariant {
        SMALL,
        MEDIUM,
        LARGE
    }

    private enum BiomeStyle {
        PLAINS,
        TAIGA,
        DESERT,
        SAVANNA,
        SNOWY,
        SWAMP,
        JUNGLE
    }

    private static final class VillagePalette {
        private BiomeStyle style;
        private BlockState foundation;
        private BlockState floor;
        private BlockState wall;
        private BlockState wallAccent;
        private BlockState pillar;
        private BlockState trim;
        private BlockState roofFill;
        private BlockState path;
        private BlockState pathBorder;
        private BlockState window;
        private BlockState chimney;
        private BlockState ground;
        private BlockState subsoil;
        private Block roofStairsBlock;
        private Block roofSlabBlock;
        private Block woodStairsBlock;
        private Block woodSlabBlock;
        private Block fenceBlock;
        private Block gateBlock;
        private Block doorBlock;
        private boolean flatRoof;
    }

    private static final class HouseSlot {
        private final int x;
        private final int z;

        private HouseSlot(final int x, final int z) {
            this.x = x;
            this.z = z;
        }
    }

    private static final class GardenPlan {
        private final List<Bee> bees;
        private final GardenCropSpec primaryBeeCrop;
        private final GardenCropSpec secondaryBeeCrop;
        private final List<GardenCropSpec> supportCrops;

        private GardenPlan(
                final List<Bee> bees,
                final GardenCropSpec primaryBeeCrop,
                final GardenCropSpec secondaryBeeCrop,
                final List<GardenCropSpec> supportCrops
        ) {
            this.bees = bees;
            this.primaryBeeCrop = primaryBeeCrop;
            this.secondaryBeeCrop = secondaryBeeCrop;
            this.supportCrops = supportCrops;
        }
    }

    private static final class GardenCropSpec {
        private final String id;
        private final Supplier<ItemStack> seedSupplier;
        private final BlockState soilState;

        private GardenCropSpec(final String id, final Supplier<ItemStack> seedSupplier, final BlockState soilState) {
            this.id = id;
            this.seedSupplier = seedSupplier;
            this.soilState = soilState;
        }
    }
}
