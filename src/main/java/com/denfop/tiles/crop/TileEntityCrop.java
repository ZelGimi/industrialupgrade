
package com.denfop.tiles.crop;


import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.agriculture.*;
import com.denfop.api.agriculture.genetics.EnumGenetic;
import com.denfop.api.agriculture.genetics.GeneticTraits;
import com.denfop.api.agriculture.genetics.GeneticsManager;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.api.bee.BeeNetwork;
import com.denfop.api.pollution.ChunkLevel;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCrop;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.render.oilquarry.DataBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.bee.TileEntityApiary;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.PlantType;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.denfop.api.agriculture.genetics.GeneticsManager.enumGeneticListMap;
import static com.denfop.api.agriculture.genetics.GeneticsManager.geneticTraitsMap;
import static com.denfop.api.agriculture.genetics.Genome.geneticBiomes;

public class TileEntityCrop extends TileEntityBlock implements ICropTile {

    @OnlyIn(Dist.CLIENT)
    public DataBlock upDataBlock;
    public Block downBlock = null;
    private ICrop crop = null;
    private long BeeId = 0;
    private Genome genome = null;
    public boolean hasDouble = false;
    private ItemStack cropItem = ItemStack.EMPTY;
    private Radiation radLevel;
    private ChunkPos chunkPos;
    private ChunkAccess chunk;
    private Biome biome;
    private int tickPest = 0;
    private BlockState downState;
    Map<BlockPos, TileEntityCrop> cropMap = new HashMap<>();
    private ChunkLevel chunkLevel;
    private int pestUse;
    private AABB axisAlignedBB;
    private List<ChunkPos> chunkPositions = new ArrayList<>();
    private Map<ChunkPos, List<TileEntityApiary>> chunkPosListMap = new HashMap<>();
    @OnlyIn(Dist.CLIENT)
    private Function render;
    private boolean humus;
    private boolean canGrow;
    private double biomeCoef = 1;

    public TileEntityCrop(BlockPos pos,BlockState state) {
        super(BlockCrop.crop, pos,state);

    }

    public static boolean hasWeed = true;
    public ItemStack getCropItem() {
        return cropItem;
    }



    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        hasDouble = nbt.getBoolean("hasDouble");
        pestUse = nbt.getByte("pestUse");
        tickPest = nbt.getShort("tickPest");
        if (nbt.contains("crop_id")) {
            final int id = nbt.getInt("crop_id");
            crop = CropNetwork.instance.getCrop(id).copy();
            crop.setTick(nbt.getInt("tick"));
            crop.setGeneration(nbt.getInt("generation"));
            this.cropItem =  ItemStack.of(nbt.getCompound("stack_crop"));
            this.genome = new Genome(this.cropItem);
            genome.loadCrop(crop);
        }
    }

    public int getPestUse() {
        return pestUse;
    }

    public void addPestUse() {
        pestUse++;
    }

    public Block getDownBlock() {
        return downBlock;
    }

    public BlockState getDownState() {
        return downState;
    }

    public Biome getBiome() {
        return biome;
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putBoolean("hasDouble", hasDouble);
        nbt.putByte("pestUse", (byte) pestUse);
        nbt.putShort("tickPest", (short) tickPest);
        if (this.crop != null) {
            nbt.putInt("tick", crop.getTick());
            nbt.putInt("generation", crop.getGeneration());
            nbt.putInt("crop_id", crop.getId());
            nbt.put("stack_crop", this.cropItem.serializeNBT());
        }
        return nbt;
    }

    public void event() {
        if (!level.isClientSide) {
            level.levelEvent(1505, this.pos, 0);
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("hasDouble")) {
            try {
                hasDouble = (boolean) DecoderHandler.decode(is);
                this.rerender();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("tick")) {
            try {
                int stage = crop.getStage();
                this.crop.setTick((int) DecoderHandler.decode(is));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("cropItem")) {
            try {
                this.cropItem = (ItemStack) DecoderHandler.decode(is);
                if (!this.cropItem.isEmpty()) {
                    crop = ((ICropItem) cropItem.getItem()).getCrop(cropItem.getDamageValue(), cropItem).copy();
                    this.genome = new Genome(cropItem);
                    this.genome.loadCrop(crop);
                } else {
                    crop = null;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("texture")) {

        }
        if (name.equals("crop")) {
            try {
                if (crop != null) {
                    this.crop.readPacket((CustomPacketBuffer) DecoderHandler.decode(is));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("cropItem1")) {
            this.cropItem = ItemStack.EMPTY;
            crop = null;
        }
    }

    public int getTickPest() {
        return tickPest;
    }

    public void setTickPest() {
        tickPest = 7000;
    }

    public boolean canPlace(TileEntityBlock te, BlockPos pos, Level world, Direction direction, LivingEntity entity) {
        EnumSoil[] soil = EnumSoil.values();
        downState = world.getBlockState(pos.below());
        downBlock = downState.getBlock();
        for (EnumSoil soil1 : soil) {
            if ((soil1.getState() == downState && !soil1.isIgnore()) || (soil1.getBlock() == downBlock && soil1.isIgnore())|| (downBlock == IUItem.humus.getBlock(0))) {
                return true;
            }
        }
        return false;
    }

    public int getLightOpacity() {
        return 0;
    }


    public PlantType getPlantType() {
        return PlantType.CROP;
    }

    public Genome getGenome() {
        return genome;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (!this.getWorld().isClientSide && added) {
            CropNetwork.instance.removeCropFromWorld(this);
            this.added = false;
        }
        if (this.getWorld().isClientSide) {
            GlobalRenderManager.removeRender(this.getWorld(), pos);
        }
    }

    boolean added = false;

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            this.chunkPos = new ChunkPos(pos);
            Radiation radiation1 = RadiationSystem.rad_system.getMap().get(chunkPos);
            if (radiation1 == null) {
                radiation1 = new Radiation(chunkPos);
                RadiationSystem.rad_system.addRadiation(radiation1);
            }
            this.radLevel = radiation1;
            ChunkLevel chunkLevel = PollutionManager.pollutionManager.getChunkLevelSoil(chunkPos);
            if (chunkLevel == null) {
                chunkLevel = new ChunkLevel(chunkPos, LevelPollution.VERY_LOW, 0);
                PollutionManager.pollutionManager.addChunkLevelSoil(chunkLevel);
            }
            this.chunkLevel = chunkLevel;
            this.chunk = this.getWorld().getChunk(pos);
            this.biome = this.getWorld().getBiome(pos).get();
            this.cropMap.clear();
            for (Direction facing1 : ModUtils.horizontalFacings) {
                final BlockPos pos1 = pos.offset(facing1.getNormal());
                BlockEntity tile = this.getWorld().getBlockEntity(pos1);
                if (tile instanceof TileEntityCrop) {
                    cropMap.put(pos1, (TileEntityCrop) tile);
                }
            }
            if (!added) {
                added = true;
                CropNetwork.instance.addNewCropToWorld(this);
            }
            this.axisAlignedBB = new AABB(
                    pos.getX() - 4,
                    pos.getY() - 2,
                    pos.getZ() - 4,
                    pos.getX() + 4,
                    pos.getY() + 2,
                    pos.getZ() + 4
            );
            int minX = (int) Math.floor(axisAlignedBB.minX) >> 4;
            int maxX = (int) Math.floor(axisAlignedBB.maxX) >> 4;
            int minZ = (int) Math.floor(axisAlignedBB.minZ) >> 4;
            int maxZ = (int) Math.floor(axisAlignedBB.maxZ) >> 4;

            chunkPositions.clear();
            ;
            chunkPosListMap.clear();
            for (int chunkX = minX; chunkX <= maxX; chunkX++) {
                for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
                    chunkPositions.add(new ChunkPos(chunkX, chunkZ));
                }
            }
            for (ChunkPos chunkPos : chunkPositions) {
                chunkPosListMap.put(chunkPos, BeeNetwork.instance.getApiaryFromChunk(level, chunkPos));
            }
        }


        if (downState == null) {
            downState = level.getBlockState(pos.below());
            downBlock = downState.getBlock();
        }
        if (crop != null){
            int tick = crop.getTick();
            int gen = crop.getGeneration();
            crop = CropNetwork.instance.getCrop(crop.getId()).copy();
            crop.setTick(tick);
            crop.setGeneration(gen);
            this.genome = new Genome(this.cropItem);
            genome.loadCrop(crop);
            this.biome = this.getWorld().getBiome(pos).value();
            biomeCoef = crop.canGrowInBiome(biome,level) ? 1 : 0.5;
        }
        this.humus = downBlock == IUItem.humus.getBlock(0);
        this.level.setBlock(pos.below(), downState,3);
    }



    @Override
    public CustomPacketBuffer writePacket() {


        CustomPacketBuffer customPacketBuffer = super.writePacket();
        customPacketBuffer.writeBoolean(this.hasDouble);
        customPacketBuffer.writeBoolean(crop != null);
        if (crop != null) {
            customPacketBuffer.writeItemStack(this.cropItem,false);
            customPacketBuffer.writeInt(crop.getId());
            customPacketBuffer.writeBytes(crop.writePacket());
        }
        return customPacketBuffer;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        hasDouble = customPacketBuffer.readBoolean();
        boolean hasCrop = customPacketBuffer.readBoolean();
        if (hasCrop) {
            this.cropItem = customPacketBuffer.readItem();
            int id = customPacketBuffer.readInt();
            this.crop = CropNetwork.instance.getCrop(id).copy();
            this.crop.readPacket(customPacketBuffer);

        }
    }

    public boolean contains(BlockPos vec) {
        if (vec.getX() > this.axisAlignedBB.minX && vec.getX() < axisAlignedBB.maxX) {
            if (vec.getY() > this.axisAlignedBB.minY && vec.getY() < axisAlignedBB.maxY) {
                return vec.getZ() > axisAlignedBB.minZ && vec.getZ() < axisAlignedBB.maxZ;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onNeighborChange(final BlockState neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (this.pos.below().equals(neighborPos)) {
            if (crop != null) {
                EnumSoil[] soil = EnumSoil.values();
                downState = neighbor;
                downBlock = downState.getBlock();
                for (EnumSoil soil1 : soil) {
                    if ((soil1.getState() == downState && !soil1.isIgnore()) || (soil1.getBlock() == downBlock && soil1.isIgnore())|| (downBlock == IUItem.humus.getBlock(0))) {
                        return;
                    }
                }
                if (!this.cropItem.isEmpty() && crop.getId() != 3) {
                    ModUtils.dropAsEntity(level, pos, cropItem, 1);
                    this.cropItem = ItemStack.EMPTY;
                    this.crop = null;
                    this.setActive("");
                }
            }
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), level.isClientSide ? 11 : 3);
            ModUtils.dropAsEntity(level, pos, new ItemStack(IUItem.crop.getItem()), hasDouble ? 2 : 1);
        }
        for (Direction facing1 : ModUtils.horizontalFacings) {
            if (pos.offset(facing1.getNormal()).equals(neighborPos)) {
                if (cropMap.containsKey(neighborPos)) {
                    cropMap.remove(neighborPos);
                } else {
                    BlockEntity tile = this.getWorld().getBlockEntity(neighborPos);
                    if (tile instanceof TileEntityCrop) {
                        cropMap.put(neighborPos, (TileEntityCrop) tile);
                    }
                }
            }
        }
    }

    public List<ItemStack> harvest(boolean dropInWorld) {
        this.crop.setGeneration(crop.getGeneration() + 1);
        this.crop.setTick(0);
        this.setActive(crop.getName().toLowerCase()+"_"+this.crop.getStage());
        if (dropInWorld) {
            for (ItemStack stack1 : crop.getDrops()) {
                ModUtils.dropAsEntity(level, pos, stack1, this.crop.getYield());
            }
            if (WorldBaseGen.random.nextInt(100) < 25) {
                ModUtils.dropAsEntity(level, pos, this.cropItem, WorldBaseGen.random.nextInt(crop.getSizeSeed() + 1));
            }
        }
        if (this.crop.getId() != 3) {
            this.canAdaptationCrop();
        }
        return crop.getDrops();
    }

    public AABB getAxisAlignedBB() {
        return axisAlignedBB;
    }

    public List<ChunkPos> getChunkPositions() {
        return chunkPositions;
    }

    public Map<ChunkPos, List<TileEntityApiary>> getChunkPosListMap() {
        return chunkPosListMap;
    }

    private void canAdaptationCrop() {
        if (WorldBaseGen.random.nextInt(100) != 0) {
            return;
        }
        Genome genome = this.genome;
        int geneticAdaptive = genome.hasGenome(EnumGenetic.GENOME_ADAPTIVE) ?
                genome.getLevelGenome(EnumGenetic.GENOME_ADAPTIVE, Integer.class) : 5;
        int geneticResistance = genome.hasGenome(EnumGenetic.GENOME_RESISTANCE) ?
                genome.getLevelGenome(EnumGenetic.GENOME_RESISTANCE, Integer.class) : 5;
        cycle:
        for (EnumGenetic enumGenetic : EnumGenetic.values()) {
            final List<GeneticTraits> genetic = enumGeneticListMap.get(enumGenetic);
            if (WorldBaseGen.random.nextInt(100) <= geneticAdaptive) {
                if (WorldBaseGen.random.nextInt(100) > geneticResistance) {
                    boolean hasGenome = genome.hasGenome(enumGenetic);
                    if (!hasGenome && WorldBaseGen.random.nextInt(100) == 0) {
                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                        genome.addGenome(geneticTraits, cropItem);
                    } else {
                        boolean needRemove =
                                WorldBaseGen.random.nextInt(geneticResistance) > WorldBaseGen.random.nextInt(geneticAdaptive);
                        boolean canUpgrade =
                                WorldBaseGen.random.nextInt(geneticAdaptive) > WorldBaseGen.random.nextInt(geneticResistance);
                        if ((needRemove || !canUpgrade) && hasGenome) {
                            GeneticTraits geneticTraits = genome.removeGenome(enumGenetic, cropItem);
                            GeneticsManager.instance.deleteGenomeCrop(crop, geneticTraits);
                        } else if (canUpgrade) {
                            switch (enumGenetic) {
                                case SUN:
                                    if (!hasGenome) {
                                        genome.addGenome(genetic.get(0), cropItem);
                                        crop.setSun(true);
                                    }
                                    break cycle;
                                case BIOME:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        List<ResourceKey<Biome>> biomeList = geneticBiomes.get(geneticTraits);
                                        if (!crop.canGrowInBiome(biomeList.get(0))) {
                                            biomeList.forEach(crop::addBiome);
                                            genome.addGenome(geneticTraits, cropItem);
                                        }
                                    } else {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        GeneticTraits geneticTraits1 = genome.removeGenome(enumGenetic, cropItem);
                                        GeneticsManager.instance.deleteGenomeCrop(crop, geneticTraits1);
                                        List< ResourceKey<Biome>> biomeList = geneticBiomes.get(geneticTraits);
                                        biomeList.forEach(crop::addBiome);
                                        genome.addGenome(geneticTraits, cropItem);
                                    }
                                    break cycle;
                                case AIR:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.setAirRequirements(geneticTraits.getValue(LevelPollution.class));
                                    } else {
                                        boolean needDecrease = WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            GeneticTraits traits = genome.getGenome(enumGenetic);
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setAirRequirements(traits.getPrev().getValue(LevelPollution.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setAirRequirements(LevelPollution.LOW);
                                            }

                                        } else {
                                            GeneticTraits traits = genome.getGenome(enumGenetic);
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setAirRequirements(traits1.getValue(LevelPollution.class));
                                            }
                                        }
                                    }
                                    break cycle;
                                case SOIL:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        LevelPollution levelPollution = geneticTraits.getValue(LevelPollution.class);
                                        if (levelPollution.ordinal() >= this.chunkLevel.getLevelPollution().ordinal()) {
                                            genome.addGenome(geneticTraits, cropItem);
                                            crop.setAirRequirements(levelPollution);
                                        }
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        LevelPollution levelPollution = traits.getValue(LevelPollution.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean() && this.chunkLevel
                                                        .getLevelPollution()
                                                        .ordinal() < levelPollution.ordinal();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setSoilRequirements(traits.getPrev().getValue(LevelPollution.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setSoilRequirements(LevelPollution.LOW);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setSoilRequirements(traits1.getValue(LevelPollution.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case WATER:
                                    if (!hasGenome) {
                                        genome.addGenome(genetic.get(0), cropItem);
                                        crop.setWaterRequirement(0);
                                    } else {
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            genome.removeGenome(genetic.get(0), cropItem);
                                            crop.setWaterRequirement(crop.getDefaultWaterRequirement());
                                        }
                                    }
                                    break cycle;
                                case PEST:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        int level = geneticTraits.getValue(Integer.class);
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.setPestResistance(level);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        int level = traits.getValue(Integer.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setPestResistance(crop.getPestResistance() - level + traits
                                                        .getPrev()
                                                        .getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setPestResistance(crop.getPestResistance() - level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setPestResistance(crop.getPestResistance() - level + traits1.getValue(Integer.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case WEED:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        int level = geneticTraits.getValue(Integer.class);
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.addChanceWeed(level);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        int level = traits.getValue(Integer.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.addChanceWeed(-level + traits.getPrev().getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.addChanceWeed(-level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.addChanceWeed(-level + traits1.getValue(Integer.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case LIGHT:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        int level = geneticTraits.getValue(Integer.class);
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.setLight(level);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setLight(traits.getPrev().getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setLight(crop.getDefaultLightLevel());
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setLight(traits1.getValue(Integer.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case RADIATION:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        EnumLevelRadiation levelPollution = geneticTraits.getValue(EnumLevelRadiation.class);
                                        if (levelPollution.ordinal() >= this.radLevel.getLevel().ordinal()) {
                                            genome.addGenome(geneticTraits, cropItem);
                                            crop.setRadiationRequirements(levelPollution);
                                        }
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        EnumLevelRadiation levelPollution = traits.getValue(EnumLevelRadiation.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean() && this.radLevel
                                                        .getLevel()
                                                        .ordinal() < levelPollution.ordinal();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setRadiationRequirements(traits
                                                        .getPrev()
                                                        .getValue(EnumLevelRadiation.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setRadiationRequirements(EnumLevelRadiation.LOW);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setRadiationRequirements(traits1.getValue(EnumLevelRadiation.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case SEED:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        int level = geneticTraits.getValue(Integer.class);
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.addSizeSeed(level);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        int level = traits.getValue(Integer.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.addSizeSeed(-level + traits.getPrev().getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.addSizeSeed(-level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.addSizeSeed(-level + traits1.getValue(Integer.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case YIELD:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        int level = geneticTraits.getValue(Integer.class);
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.setYield(level);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        int level = traits.getValue(Integer.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setYield(crop.getYield() - level + traits.getPrev().getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setYield(crop.getYield() - level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setYield(crop.getYield() - level + traits1.getValue(Integer.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case BEE:
                                    if (!hasGenome) {
                                        genome.addGenome(genetic.get(0), cropItem);
                                        crop.setBeeCombine(true);
                                    }
                                    break cycle;
                                case NIGHT_GROW:
                                    if (!hasGenome) {
                                        genome.addGenome(genetic.get(0), cropItem);
                                        crop.setNight(true);
                                    }
                                    break cycle;
                                case SOIL_BLOCK:
                                    if (!hasGenome) {
                                        genome.addGenome(genetic.get(0), cropItem);
                                        crop.setIgnoreSoil(true);
                                    }
                                    break cycle;
                                case CHANCE:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        int level = geneticTraits.getValue(Integer.class);
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.setChance(level);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        int level = traits.getValue(Integer.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setChance(crop.getChance() - level + traits
                                                        .getPrev()
                                                        .getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setChance(crop.getChance() - level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setChance(crop.getChance() - level + traits1.getValue(Integer.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case GROW_SPEED:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        double level = geneticTraits.getValue(Double.class);
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.setGrowthSpeed(level);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        double level = traits.getValue(Double.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setGrowthSpeed(traits
                                                        .getPrev()
                                                        .getValue(Double.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setGrowthSpeed(1);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setGrowthSpeed(traits1.getValue(Double.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case WEATHER:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        int level = geneticTraits.getValue(Integer.class);
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.setWeatherResistance(level);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setWeatherResistance(traits.getPrev().getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setWeatherResistance(crop.getDefaultWeatherResistance());
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setWeatherResistance(traits1.getValue(Integer.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case GENOME_ADAPTIVE:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        int level = geneticTraits.getValue(Integer.class);
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.setGenomeAdaptive(level);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        int level = traits.getValue(Integer.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setGenomeAdaptive(crop.getGenomeAdaptive() - level + traits
                                                        .getPrev()
                                                        .getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setGenomeAdaptive(crop.getGenomeAdaptive() - level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setGenomeAdaptive(crop.getGenomeAdaptive() - level + traits1.getValue(Integer.class));
                                            }
                                        }


                                    }
                                    break cycle;
                                case GENOME_RESISTANCE:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        int level = geneticTraits.getValue(Integer.class);
                                        genome.addGenome(geneticTraits, cropItem);
                                        crop.setGenomeResistance(level);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        int level = traits.getValue(Integer.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setGenomeResistance(crop.getGenomeResistance() - level + traits
                                                        .getPrev()
                                                        .getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, this.cropItem);
                                                crop.setGenomeResistance(crop.getGenomeResistance() - level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.cropItem);
                                                genome.addGenome(traits1, cropItem);
                                                crop.setGenomeResistance(crop.getGenomeResistance() - level + traits1.getValue(
                                                        Integer.class));
                                            }
                                        }


                                    }
                                    break cycle;
                            }
                        }
                    }
                }
            }
        }
    }

    public void setBeeId(final long beeId) {
        BeeId = beeId;
    }

    public long getBeeId() {
        return BeeId;
    }

    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        List<ItemStack> list = new LinkedList<>(super.getSelfDrops(fortune, wrench));
        if (this.hasDouble) {
            list.add(this.getPickBlock(null, null));
        }
        if (crop != null && crop.getId() != 3) {
            list.add(this.cropItem);
        }
        return list;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (crop != null) {
            BeeId = 0;
            boolean work = false;
            if (this.getWorld().getGameTime() % 20 == 0) {
                if (chunk == null) {
                    this.chunkPos = new ChunkPos(pos);
                    Radiation radiation1 = RadiationSystem.rad_system.getMap().get(chunkPos);
                    if (radiation1 == null) {
                        radiation1 = new Radiation(chunkPos);
                        RadiationSystem.rad_system.addRadiation(radiation1);
                    }
                    this.radLevel = radiation1;
                    ChunkLevel chunkLevel = PollutionManager.pollutionManager.getChunkLevelSoil(chunkPos);
                    if (chunkLevel == null) {
                        chunkLevel = new ChunkLevel(chunkPos, LevelPollution.VERY_LOW, 0);
                        PollutionManager.pollutionManager.addChunkLevelSoil(chunkLevel);
                    }
                    this.chunkLevel = chunkLevel;
                    this.chunk = this.getWorld().getChunk(pos);
                    this.biome = this.getWorld().getBiome(pos).get();
                }
                if (this.getWorld().getGameTime() % 60 == 0) {
                    this.canGrow = CropNetwork.instance.canGrow(level, pos, chunkPos, crop, radLevel, chunk, biome, chunkLevel);
                }
                if (this.canGrow && (this
                        .getWorld()
                        .getGameTime() % 400 != 0 || canGrow())) {

                    if (crop.getTick() < crop.getMaxTick() && this.getWorld().getGameTime() % 400 == 0 && crop.getId() != 3) {
                        int chanceWeed = 100 - crop.getChanceWeed() - (5 * (crop.getSizeSeed() - 1));
                        work = true;
                        if (chanceWeed > 0) {
                            if (this.getWorld().getGameTime() % 400 == 0 && hasWeed && tickPest == 0 &&
                                    WorldBaseGen.random.nextInt(100) < chanceWeed && WorldBaseGen.random.nextInt(100) == 0) {
                                this.crop = CropInit.weed_seed.copy();
                                pestUse = 0;
                                this.cropItem = CropInit.weed_seed.getStack();
                                this.genome = new Genome(this.cropItem);
                                this.setActive(crop.getName().toLowerCase()+"_"+this.crop.getStage());
                            }
                        }
                    }
                    if (this.radLevel.getLevel().ordinal() > EnumLevelRadiation.HIGH.ordinal() && this.crop.getId() != 3) {
                        canAdaptationCrop();
                    }
                    if (tickPest >= 20) {
                        tickPest -= 20;
                    } else {
                        tickPest = 0;
                    }
                    if (crop.getTick() < crop.getMaxTick()) {
                        int stage = crop.getStage();
                        crop.addTick((int) (biomeCoef*20 * crop.getGrowthSpeed() * (this.humus ? 1.25 : 1)));
                        boolean needUpdate = stage != crop.getStage();
                        if (needUpdate) {
                            this.setActive(crop.getName().toLowerCase()+"_"+this.crop.getStage());
                        }
                    }
                }
            }
        }
        if (this.getWorld().getGameTime() % 200 == 0 && pestUse > 0) {
            pestUse--;
        }
        if (this
                .getWorld()
                .getGameTime() % 400 == 0  && hasWeed&& tickPest == 0 && this.crop == null && !hasDouble && WorldBaseGen.random.nextInt(200) == 0) {
            this.crop = CropInit.weed_seed.copy();
            pestUse = 0;
            this.cropItem = CropInit.weed_seed.getStack();
            this.genome = new Genome(this.cropItem);
            this.setActive(crop.getName().toLowerCase()+"_"+this.crop.getStage());
        }
        if (hasDouble && this
                .getWorld()
                .getGameTime() % 10 == 0 && !this.cropMap.isEmpty() && WorldBaseGen.random.nextInt(50) == 0) {
            List<TileEntityCrop> crops = this.cropMap.values().stream()
                    .filter(cropTile -> cropTile.crop != null && cropTile.crop.getTick() == cropTile.crop.getMaxTick())
                    .toList();

            if (crops.size() < 2) {
                if (this
                        .getWorld()
                        .getGameTime() % 400 == 0 && hasWeed && tickPest == 0 && this.crop == null && WorldBaseGen.random.nextInt(200) == 0) {
                    this.hasDouble = false;
                    this.crop = CropInit.weed_seed.copy();
                    pestUse = 0;
                    this.cropItem = CropInit.weed_seed.getStack();
                    this.setActive(crop.getName().toLowerCase()+"_"+this.crop.getStage());
                }
                return;
            }
            boolean can = false;
            Map<ICrop, TileEntityCrop> cropMap1 = crops.stream()
                    .collect(Collectors.toMap(
                            cropTile -> cropTile.crop,
                            cropTile -> cropTile,
                            (existing, replacement) -> existing
                    ));


            List<List<ICrop>> combinations = new ArrayList<>();
            for (int i = 0; i < crops.size() - 1; i++) {
                for (int j = i + 1; j < crops.size(); j++) {
                    combinations.add(Arrays.asList(crops.get(i).crop, crops.get(j).crop));
                }
            }

            for (int i = 0; i < crops.size() - 2; i++) {
                for (int j = i + 1; j < crops.size() - 1; j++) {
                    for (int k = j + 1; k < crops.size(); k++) {
                        combinations.add(Arrays.asList(crops.get(i).crop, crops.get(j).crop, crops.get(k).crop));
                    }
                }
            }


            for (int i = 0; i < crops.size() - 3; i++) {
                for (int j = i + 1; j < crops.size() - 2; j++) {
                    for (int k = j + 1; k < crops.size() - 1; k++) {
                        for (int l = k + 1; l < crops.size(); l++) {
                            combinations.add(Arrays.asList(
                                    crops.get(i).crop,
                                    crops.get(j).crop,
                                    crops.get(k).crop,
                                    crops.get(l).crop
                            ));
                        }
                    }
                }
            }
            for (List<ICrop> pair : combinations) {
                boolean isEquals = true;
                for (ICrop crop1 : pair) {
                    isEquals = pair.get(0).getId() == crop1.getId();
                }
                ICrop newCrop;
                int chance = 0;
                for (ICrop crop1 : pair) {
                    int additionChance = 0;
                    if (crop1.isBeeCombine()) {
                        TileEntityCrop crop2 = cropMap1.get(crop1);
                        cycle:
                        for (List<TileEntityApiary> apiaries : crop2.getChunkPosListMap().values()) {
                            for (TileEntityApiary apiary : apiaries) {
                                if (contains(apiary.getPos()) && apiary.getQueen() != null && (apiary.ill * 1D / apiary.bees < 0.2)) {
                                    additionChance += apiary.getQueen().getChance();
                                    break cycle;
                                }
                            }
                        }

                    }
                    chance = Math.max(chance, crop1.getChance() + additionChance);
                }
                if (WorldBaseGen.random.nextInt(100) > chance && WorldBaseGen.random.nextInt(100) != 0) {
                    continue;
                }
                if (isEquals) {
                    newCrop = CropNetwork.instance.getCrop(pair.get(0).getId()).copy();
                    if (!CropNetwork.instance.canPlantCrop(
                            newCrop.getStackForDrop(),
                            level,
                            pos,
                            downState,
                            biome
                    )) {
                        continue;
                    }
                } else {
                    newCrop = CropNetwork.instance.canCropCombine(pair);
                    if (newCrop == null || !CropNetwork.instance.canPlantCrop(
                            newCrop.getStackForDrop(),
                            level,
                            pos,
                            downState,
                            biome
                    )) {
                        continue;
                    }
                }
                final int size = pair.size();
                Genome combinedGenome = new Genome(newCrop.getStackForDrop().copy());
                for (int i = 0; i < size; i++) {
                    if (i - 1 == 0 && i + 1 == size) {
                        continue;
                    }
                    int nextIndex = (i + 1) % size;


                    Genome currentGenome = cropMap1.get(pair.get(i)).getGenome();
                    Genome nextGenome = cropMap1.get(pair.get(nextIndex)).getGenome();


                    int adaptive = currentGenome.hasGenome(EnumGenetic.GENOME_ADAPTIVE)
                            ? currentGenome.getLevelGenome(EnumGenetic.GENOME_ADAPTIVE, Integer.class)
                            : 5;
                    int resistance = nextGenome.hasGenome(EnumGenetic.GENOME_RESISTANCE)
                            ? nextGenome.getLevelGenome(EnumGenetic.GENOME_RESISTANCE, Integer.class)
                            : 5;

                    addGenome(
                            new ArrayList<>(currentGenome.getGeneticTraitsMap().values()),
                            combinedGenome,
                            adaptive,
                            resistance
                    );
                }
                this.hasDouble = false;

                this.cropItem = combinedGenome.getStack();
                newCrop.setStack(cropItem);
                this.crop = newCrop;
                this.biomeCoef = crop.canGrowInBiome(biome,level) ? 1 : 0.5;
                this.genome = combinedGenome;
                this.genome.loadCrop(this.crop);
                this.setActive(crop.getName().toLowerCase()+"_"+this.crop.getStage());
                can = true;
                break;
            }
            if (!can) {
                if (this
                        .getWorld()
                        .getGameTime() % 400 == 0 && hasWeed && tickPest == 0 && this.crop == null && WorldBaseGen.random.nextInt(200) == 0) {
                    this.hasDouble = false;
                    this.crop = CropInit.weed_seed.copy();
                    pestUse = 0;
                    this.cropItem = CropInit.weed_seed.getStack();
                    this.setActive(crop.getName().toLowerCase()+"_"+this.crop.getStage());
                }
            }
        }
    }

    public void addGenome(List<GeneticTraits> genericTraits, Genome genome2, int geneticAdaptive, int geneticResistance) {
        for (GeneticTraits geneticTraits : genericTraits) {
            if (WorldBaseGen.random.nextInt(100) <= geneticAdaptive) {
                if (WorldBaseGen.random.nextInt(100) > geneticResistance) {
                    if (!genome2.hasGenome(geneticTraits.getGenetic())) {
                        genome2.addGenome(geneticTraits, cropItem);
                    }
                }

            }
        }
    }

    private boolean canGrow() {
        if (!this.cropMap.isEmpty()) {
            List<TileEntityCrop> crops = this.cropMap
                    .values()
                    .stream().parallel()
                    .filter(tileEntityCrop -> tileEntityCrop.crop != null && tileEntityCrop.crop.getId() != 3)
                    .toList();
            for (TileEntityCrop crop1 : crops) {
                if (crop.compatibilityWithCrop(crop1.getCrop())) {
                    conflictCrop(crop1.getCrop(), crop1.getGenome());
                }
            }
        }
        return true;
    }

    private void conflictCrop(ICrop crop, Genome genome) {
        if (!WorldBaseGen.random.nextBoolean()) {
            return;
        }

        int party = WorldBaseGen.random.nextInt(2);

        int adaptive1 = getGenomeLevel(genome, EnumGenetic.GENOME_ADAPTIVE);
        int adaptive2 = getGenomeLevel(this.genome, EnumGenetic.GENOME_ADAPTIVE);
        int resistance1 = getGenomeLevel(genome, EnumGenetic.GENOME_RESISTANCE);
        int resistance2 = getGenomeLevel(this.genome, EnumGenetic.GENOME_RESISTANCE);

        List<GeneticTraits> traitsList = party == 0
                ? new ArrayList<>(genome.getGeneticTraitsMap().values())
                : new ArrayList<>(this.genome.getGeneticTraitsMap().values());

        Genome sourceGenome = party == 0 ? genome : this.genome;
        Genome targetGenome = party == 0 ? this.genome : genome;
        ICrop sourceCrop = party == 0 ? crop : this.crop;
        ICrop targetCrop = party == 0 ? this.crop : crop;
        int sourceResistance = party == 0 ? resistance1 : resistance2;
        int targetResistance = party == 0 ? resistance2 : resistance1;
        int sourceAdaptive = party == 0 ? adaptive2 : adaptive1;
        int targetAdaptive = party == 0 ? adaptive1 : adaptive2;

        int max = 0;
        for (GeneticTraits trait : traitsList) {
            if (max == 2) {
                break;
            }

            if (WorldBaseGen.random.nextInt(sourceResistance) < WorldBaseGen.random.nextInt(sourceAdaptive)) {
                if (sourceGenome.hasGenome(trait.getGenetic()) && !targetGenome.hasGenome(trait.getGenetic())) {
                    sourceGenome.removeGenome(trait.getGenetic(), sourceGenome.getStack());
                    GeneticsManager.instance.deleteGenomeCrop(sourceCrop, trait);
                    max++;

                    if (WorldBaseGen.random.nextInt(targetResistance) < WorldBaseGen.random.nextInt(targetAdaptive)) {
                        targetGenome.addGenome(trait, targetGenome.getStack());
                        GeneticsManager.instance.addGenomeCrop(targetCrop, trait);
                    }
                }
            }
        }
    }

    private int getGenomeLevel(Genome genome, EnumGenetic genetic) {
        return genome.hasGenome(genetic)
                ? genome.getLevelGenome(genetic, Integer.class)
                : 5;
    }


    @Override
    public void updateEntityClient() {
        super.updateEntityClient();

    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        return this.getWorld().isClientSide || this.rightClick(player, hand);
    }



    @Override
    public boolean onSneakingActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.cropItem.isEmpty() && crop != null && !this.getWorld().isClientSide && crop.getId() != 3) {
            if (this.crop != null && this.crop.getTick() == this.crop.getMaxTick() && this.crop.getId() != 3) {
                harvest(true);
            }
            ModUtils.dropAsEntity(level, pos, this.cropItem, 1);
            this.cropItem = ItemStack.EMPTY;
            this.crop = null;
            this.genome = null;
            this.setActive("");
        }
        return super.onSneakingActivated(player, hand, side, vec3);
    }



    private boolean rightClick(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);


        if (stack.isEmpty() && this.hasDouble && this.crop == null) {
            handleDoubleCropRemoval(player);
            return true;
        }


        if (stack.is(this.getPickBlock(player, null).getItem()) && !this.hasDouble && this.crop == null) {
            stack.shrink(1);
            this.hasDouble = true;
            this.setActive(true);
            return true;
        }


        if (stack.getItem() instanceof HoeItem && !this.hasDouble && this.crop != null && this.crop.getId() == 3) {
            resetCrop();
            return true;
        }


        if (stack.getItem() == IUItem.fertilizer.getItem() && !this.hasDouble && this.crop != null && this.crop.getTick() < this.crop.getMaxTick() && this.crop.getId() != 3) {
            fertilizeCrop(stack);
            return true;
        }


        if ((stack.getItem() instanceof ICropItem || IUCore.cropMap.containsKey(stack.getItem())) && !this.hasDouble && this.crop == null && CropNetwork.instance.canPlantCrop(
                stack,
                level,
                pos,
                downState,
                biome
        )) {
            plantNewCrop(stack);
            return true;
        }


        if (this.crop != null && this.crop.getTick() == this.crop.getMaxTick() && this.crop.getId() != 3) {
            harvest(true);
            return true;
        }

        return false;
    }


    private void handleDoubleCropRemoval(Player player) {
        ItemStack stack1 = this.getPickBlock(player, null);
        this.hasDouble = false;
        this.setActive("");
        ModUtils.dropAsEntity(level, pos, stack1);
    }

    public void resetCrop() {
        this.crop = null;
        this.cropItem = ItemStack.EMPTY;
        this.genome = null;
        this.setActive("");
    }


    public void fertilizeCrop(ItemStack stack) {
        stack.shrink(1);
        this.crop.addTick((int) (this.crop.getMaxTick() * 0.2));
        this.pestUse++;
        event();
        if (this.pestUse > 40) {
            handlePestResistance();
        } else {
            this.setActive(crop.getName().toLowerCase()+"_"+this.crop.getStage());
        }
    }


    private void handlePestResistance() {
        int resistance = this.crop.getPestResistance();
        boolean pestResistance = WorldBaseGen.random.nextInt(100) < (100 - resistance);
        if (!pestResistance && hasWeed) {
            this.crop = CropInit.weed_seed.copy();
            this.pestUse = 0;
            this.cropItem = CropInit.weed_seed.getStack();
            this.genome = new Genome(this.cropItem);
            this.setActive(crop.getName().toLowerCase()+"_0");
        } else {
            this.setActive(crop.getName().toLowerCase()+"_"+this.crop.getStage());
        }
    }





    public void plantNewCrop(ItemStack stack) {
        if(IUCore.cropMap.containsKey(stack.getItem())){
            this.cropItem = IUCore.cropMap.get(stack.getItem()).getStack().copy();
            this.cropItem.setCount(1);
            stack.shrink(1);
            this.genome = new Genome(this.cropItem);
            this.crop = CropNetwork.instance.getCropFromStack(this.cropItem).copy();
            this.biomeCoef = crop.canGrowInBiome(biome, level) ? 1 : 0.5;
            this.genome.loadCrop(this.crop);
            this.setActive(crop.getName().toLowerCase() + "_0");
        }else{
            this.cropItem = stack.copy();
            this.cropItem.setCount(1);
            stack.shrink(1);
            this.genome = new Genome(this.cropItem);
            this.crop = CropNetwork.instance.getCropFromStack(this.cropItem).copy();
            this.biomeCoef = crop.canGrowInBiome(biome, level) ? 1 : 0.5;
            this.genome.loadCrop(this.crop);
            this.setActive(crop.getName().toLowerCase() + "_0");
        }
    }




    public SoundType getBlockSound(Entity entity) {
        return SoundType.CROP;
    }


    public AABB getPhysicsBoundingBox() {
        return new AABB(
                0.20000000298023224,
                -0.0625,
                0.20000000298023224,
                0.800000011920929,
                0.8500000238418579,
                0.800000011920929
        );
    }


    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCrop.crop;
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.crop.getBlock();
    }

    public List<AABB> getAabbs(boolean forCollision) {
        List<AABB> ret = new ArrayList<>();
        if (forCollision) {
            ret.add(new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        } else {
            ret.add(new AABB(
                    0.20000000298023224,
                    -0.0625,
                    0.20000000298023224,
                    0.800000011920929,
                    0.8500000238418579,
                    0.800000011920929
            ));
        }

        return ret;
    }

    public boolean isHasDouble() {
        return hasDouble;
    }

    public ICrop getCrop() {
        return this.crop;
    }



    public boolean wrenchCanRemove(Player player) {
        return false;
    }


}
