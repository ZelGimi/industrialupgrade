package com.denfop.tiles.crop;


import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.agriculture.CropInit;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.EnumSoil;
import com.denfop.api.agriculture.ICrop;
import com.denfop.api.agriculture.ICropItem;
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
import com.denfop.blocks.BlockCrop;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.state.TileEntityBlockStateContainer;
import com.denfop.events.client.GlobalRenderManager;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.proxy.ClientProxy;
import com.denfop.render.base.BakedBlockModel;
import com.denfop.render.crop.CropRenderState;
import com.denfop.render.crop.TileEntityDoubleCropRender;
import com.denfop.render.oilquarry.DataBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.bee.TileEntityApiary;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.denfop.api.agriculture.genetics.GeneticsManager.enumGeneticListMap;
import static com.denfop.api.agriculture.genetics.GeneticsManager.geneticTraitsMap;
import static com.denfop.api.agriculture.genetics.Genome.geneticBiomes;
import static com.denfop.render.crop.CropRender.renderStateProperty;

public class TileEntityCrop extends TileEntityBlock implements ICropTile {

    @SideOnly(Side.CLIENT)
    public DataBlock upDataBlock;
    public Block downBlock = null;
    public boolean hasDouble = false;
    Map<BlockPos, TileEntityCrop> cropMap = new HashMap<>();
    boolean added = false;
    private ICrop crop = null;
    private long BeeId = 0;
    private Genome genome = null;
    private ItemStack cropItem = ItemStack.EMPTY;
    private CropRenderState cropRenderState;
    private Radiation radLevel;
    private ChunkPos chunkPos;
    private Chunk chunk;
    private Biome biome;
    private int tickPest = 0;
    private IBlockState downState;
    private ChunkLevel chunkLevel;
    private int pestUse;
    private AxisAlignedBB axisAlignedBB;
    private List<ChunkPos> chunkPositions = new ArrayList<>();
    private Map<ChunkPos, List<TileEntityApiary>> chunkPosListMap = new HashMap<>();
    @SideOnly(Side.CLIENT)
    private Function render;
    private boolean humus;
    private boolean canGrow;

    public TileEntityCrop() {


    }

    public ItemStack getCropItem() {
        return cropItem;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCrop.crop;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.crop;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        hasDouble = nbt.getBoolean("hasDouble");
        pestUse = nbt.getByte("pestUse");
        tickPest = nbt.getShort("tickPest");
        if (nbt.hasKey("crop_id")) {
            final int id = nbt.getInteger("crop_id");
            crop = CropNetwork.instance.getCrop(id).copy();
            crop.setTick(nbt.getInteger("tick"));
            crop.setGeneration(nbt.getInteger("generation"));
            this.cropItem = new ItemStack(nbt.getCompoundTag("stack_crop"));
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

    public IBlockState getDownState() {
        return downState;
    }

    public Biome getBiome() {
        return biome;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("hasDouble", hasDouble);
        nbt.setByte("pestUse", (byte) pestUse);
        nbt.setShort("tickPest", (short) tickPest);
        if (this.crop != null) {
            nbt.setInteger("tick", crop.getTick());
            nbt.setInteger("generation", crop.getGeneration());
            nbt.setInteger("crop_id", crop.getId());
            nbt.setTag("stack_crop", this.cropItem.serializeNBT());
        }
        return nbt;
    }

    public void event() {
        if (!world.isRemote) {
            world.playEvent(2005, pos, 0);
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("hasDouble")) {
            try {
                hasDouble = (boolean) DecoderHandler.decode(is);
                this.updateRenderState();
                this.rerender();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("tick")) {
            try {
                int stage = crop.getStage();
                this.crop.setTick((int) DecoderHandler.decode(is));
                this.updateRenderState();
                if (stage != crop.getStage() && !crop.getTopTexture().isEmpty()) {
                    this.cropRenderState.setNeedTwoTexture(true);
                    if (upDataBlock == null) {
                        IBlockState blockState1 = this.block
                                .getDefaultState()
                                .withProperty(
                                        this.block.typeProperty,
                                        this.block.typeProperty.getState(this.teBlock, this.active)
                                )
                                .withProperty(
                                        BlockTileEntity.facingProperty,
                                        this.getFacing()
                                );
                        blockState1 = getExtendedState((TileEntityBlockStateContainer.PropertiesStateInstance) blockState1);
                        upDataBlock = new DataBlock(blockState1);
                        final IBakedModel model = ((ClientProxy) IUCore.proxy).cropRender.createModel(cropRenderState);
                        upDataBlock.setState(model);
                    } else {
                        IBlockState blockState1 = this.block
                                .getDefaultState()
                                .withProperty(
                                        this.block.typeProperty,
                                        this.block.typeProperty.getState(this.teBlock, this.active)
                                )
                                .withProperty(
                                        BlockTileEntity.facingProperty,
                                        this.getFacing()
                                );
                        blockState1 = getExtendedState((TileEntityBlockStateContainer.PropertiesStateInstance) blockState1);
                        upDataBlock.setBlockState(blockState1);
                        final IBakedModel model = ((ClientProxy) IUCore.proxy).cropRender.createModel(cropRenderState);
                        upDataBlock.setState(model);
                    }
                    this.cropRenderState.setNeedTwoTexture(false);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("cropItem")) {
            try {
                this.cropItem = (ItemStack) DecoderHandler.decode(is);
                if (!this.cropItem.isEmpty()) {
                    crop = ((ICropItem) cropItem.getItem()).getCrop(cropItem.getItemDamage(), cropItem).copy();
                    this.genome = new Genome(cropItem);
                    this.genome.loadCrop(crop);
                } else {
                    crop = null;
                }
                this.updateRenderState();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("texture")) {
            if (crop != null && !crop.getTopTexture().isEmpty()) {
                this.cropRenderState.setNeedTwoTexture(true);
                if (upDataBlock == null) {
                    IBlockState blockState1 = this.block
                            .getDefaultState()
                            .withProperty(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, this.active))
                            .withProperty(
                                    BlockTileEntity.facingProperty,
                                    this.getFacing()
                            );
                    blockState1 = getExtendedState((TileEntityBlockStateContainer.PropertiesStateInstance) blockState1);
                    upDataBlock = new DataBlock(blockState1);
                    final IBakedModel model = ((ClientProxy) IUCore.proxy).cropRender.createModel(cropRenderState);
                    upDataBlock.setState(model);
                } else {
                    IBlockState blockState1 = this.block
                            .getDefaultState()
                            .withProperty(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, this.active))
                            .withProperty(
                                    BlockTileEntity.facingProperty,
                                    this.getFacing()
                            );
                    blockState1 = getExtendedState((TileEntityBlockStateContainer.PropertiesStateInstance) blockState1);
                    upDataBlock.setBlockState(blockState1);
                    final IBakedModel model = ((ClientProxy) IUCore.proxy).cropRender.createModel(cropRenderState);
                    upDataBlock.setState(model);
                }
                this.cropRenderState.setNeedTwoTexture(false);
            }
        }
        if (name.equals("crop")) {
            try {
                if (crop != null) {
                    this.crop.readPacket((CustomPacketBuffer) DecoderHandler.decode(is));
                    this.updateRenderState();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("cropItem1")) {
            this.cropItem = ItemStack.EMPTY;
            crop = null;
            this.updateRenderState();
        }
    }

    public int getTickPest() {
        return tickPest;
    }

    public void setTickPest() {
        tickPest = 7000;
    }

    public boolean canPlace(TileEntityBlock te, BlockPos pos, World world) {
        EnumSoil[] soil = EnumSoil.values();
        downState = world.getBlockState(pos.down());
        downBlock = downState.getBlock();
        for (EnumSoil soil1 : soil) {
            if ((soil1.getState() == downState && !soil1.isIgnore()) || (soil1.getBlock() == downBlock && soil1.isIgnore()) || (downBlock == IUItem.humus)) {
                return true;
            }
        }
        return false;
    }

    public int getLightOpacity() {
        return 0;
    }

    public void onNetworkUpdate(String field) {
        this.updateRenderState();
        this.rerender();
        super.onNetworkUpdate(field);
    }

    public EnumPlantType getPlantType() {
        return EnumPlantType.Crop;
    }

    public Genome getGenome() {
        return genome;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (!this.getWorld().isRemote && added) {
            CropNetwork.instance.removeCropFromWorld(this);
            this.added = false;
        }
        if (this.getWorld().isRemote) {
            GlobalRenderManager.removeRender(this.getWorld(), pos);
        }
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
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
            this.chunk = this.getWorld().getChunkFromBlockCoords(pos);
            this.biome = this.getWorld().getBiome(pos);
            this.cropMap.clear();
            for (EnumFacing facing1 : ModUtils.horizontalFacings) {
                final BlockPos pos1 = pos.offset(facing1);
                TileEntity tile = this.getWorld().getTileEntity(pos1);
                if (tile instanceof TileEntityCrop) {
                    cropMap.put(pos1, (TileEntityCrop) tile);
                }
            }
            if (!added) {
                added = true;
                CropNetwork.instance.addNewCropToWorld(this);
            }
            this.axisAlignedBB = new AxisAlignedBB(
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
                chunkPosListMap.put(chunkPos, BeeNetwork.instance.getApiaryFromChunk(world, chunkPos));
            }
            sendUpdatePacket("texture", 0);
            sendUpdatePacket("hasDouble", hasDouble);
        }

        if (this.getWorld().isRemote) {
            this.render = createFunction();
            GlobalRenderManager.addRender(this.getWorld(), pos, render);

            this.updateRenderState();
        }
        if (downState == null) {
            downState = world.getBlockState(pos.down());
            downBlock = downState.getBlock();
        }
        this.humus = downBlock == IUItem.humus;
        this.world.setBlockState(pos.down(), downState);
    }

    @SideOnly(Side.CLIENT)
    public Function createFunction() {
        Function function = o -> {
            if (this.upDataBlock != null && this.crop != null && this.crop.getRender() == 3) {
                GL11.glPushMatrix();
                GL11.glTranslated(this.getPos().getX(), this.getPos().getY() + 1, this.getPos().getZ());

                RenderHelper.enableStandardItemLighting();
                GlStateManager.depthMask(true);
                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                TileEntityDoubleCropRender.render((BakedBlockModel) this.upDataBlock.getState(),
                        this.upDataBlock.getBlockState(), null
                );

                GL11.glPopMatrix();
            }
            return 0;
        };
        return function;
    }

    @Override
    public CustomPacketBuffer writePacket() {


        CustomPacketBuffer customPacketBuffer = super.writePacket();
        customPacketBuffer.writeBoolean(this.hasDouble);
        customPacketBuffer.writeBoolean(crop != null);
        if (crop != null) {
            customPacketBuffer.writeItemStack(this.cropItem);
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
            try {
                this.cropItem = customPacketBuffer.readItemStack();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            int id = customPacketBuffer.readInt();
            this.crop = CropNetwork.instance.getCrop(id).copy();
            this.crop.readPacket(customPacketBuffer);
            this.updateRenderState();
            this.rerender();
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
    public void onNeighborChange(final Block neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (this.pos.down().equals(neighborPos)) {
            if (crop != null) {
                if (!this.cropItem.isEmpty() && crop.getId() != 3) {
                    ModUtils.dropAsEntity(world, pos, cropItem, 1);
                    this.cropItem = ItemStack.EMPTY;
                    this.crop = null;
                    new PacketUpdateFieldTile(this, "cropItem1", "");
                }
            }
            world.setBlockState(pos, net.minecraft.init.Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
            ModUtils.dropAsEntity(world, pos, new ItemStack(IUItem.crop), hasDouble ? 2 : 1);
        }
        for (EnumFacing facing1 : ModUtils.horizontalFacings) {
            if (pos.offset(facing1).equals(neighborPos)) {
                if (cropMap.containsKey(neighborPos)) {
                    cropMap.remove(neighborPos);
                } else {
                    TileEntity tile = this.getWorld().getTileEntity(neighborPos);
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
        new PacketUpdateFieldTile(this, "tick", 0);
        if (dropInWorld) {
            for (ItemStack stack1 : crop.getDrops()) {
                ModUtils.dropAsEntity(world, pos, stack1, this.crop.getYield());
            }
            if (WorldBaseGen.random.nextInt(100) < 25) {
                ModUtils.dropAsEntity(world, pos, this.cropItem, WorldBaseGen.random.nextInt(crop.getSizeSeed() + 1));
            }
        }
        if (this.crop.getId() != 3) {
            this.canAdaptationCrop();
        }
        return crop.getDrops();
    }

    public AxisAlignedBB getAxisAlignedBB() {
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
                                        List<Biome> biomeList = geneticBiomes.get(geneticTraits);
                                        if (!crop.canGrowInBiome(biomeList.get(0))) {
                                            biomeList.forEach(crop::addBiome);
                                            genome.addGenome(geneticTraits, cropItem);
                                        }
                                    } else {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        GeneticTraits geneticTraits1 = genome.removeGenome(enumGenetic, cropItem);
                                        GeneticsManager.instance.deleteGenomeCrop(crop, geneticTraits1);
                                        List<Biome> biomeList = geneticBiomes.get(geneticTraits);
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

    public long getBeeId() {
        return BeeId;
    }

    public void setBeeId(final long beeId) {
        BeeId = beeId;
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
            if (this.getWorld().getWorldTime() % 20 == 0) {
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
                    this.chunk = this.getWorld().getChunkFromBlockCoords(pos);
                    this.biome = this.getWorld().getBiome(pos);
                }
                if (this.getWorld().getWorldTime() % 60 == 0) {
                    this.canGrow = CropNetwork.instance.canGrow(world, pos, chunkPos, crop, radLevel, chunk, biome, chunkLevel);
                }
                if (this.canGrow && (this
                        .getWorld()
                        .getWorldTime() % 400 != 0 || canGrow())) {

                    if (crop.getTick() < crop.getMaxTick() && this.getWorld().getWorldTime() % 400 == 0 && crop.getId() != 3) {
                        int chanceWeed = 100 - crop.getChanceWeed() - (5 * (crop.getSizeSeed() - 1));
                        work = true;
                        if (chanceWeed > 0) {
                            if (this.getWorld().getWorldTime() % 400 == 0 && tickPest == 0 &&
                                    WorldBaseGen.random.nextInt(100) < chanceWeed && WorldBaseGen.random.nextInt(100) == 0) {
                                this.crop = CropInit.weed_seed.copy();
                                pestUse = 0;
                                this.cropItem = CropInit.weed_seed.getStack();
                                this.genome = new Genome(this.cropItem);
                                new PacketUpdateFieldTile(this, "cropItem", this.cropItem);
                                new PacketUpdateFieldTile(this, "crop", this.crop);
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
                        crop.addTick((int) (20 * crop.getGrowthSpeed() * (this.humus ? 1.25 : 1)));
                        boolean needUpdate = stage != crop.getStage();
                        if (needUpdate) {
                            new PacketUpdateFieldTile(this, "tick", this.crop.getTick());
                        }
                    }
                }
            }
        }
        if (this.getWorld().getWorldTime() % 200 == 0 && pestUse > 0) {
            pestUse--;
        }
        if (this
                .getWorld()
                .getWorldTime() % 400 == 0 && tickPest == 0 && this.crop == null && !hasDouble && WorldBaseGen.random.nextInt(200) == 0) {
            this.crop = CropInit.weed_seed.copy();
            pestUse = 0;
            this.cropItem = CropInit.weed_seed.getStack();
            this.genome = new Genome(this.cropItem);
            new PacketUpdateFieldTile(this, "cropItem", this.cropItem);
            new PacketUpdateFieldTile(this, "crop", this.crop);
        }
        if (hasDouble && this
                .getWorld()
                .getWorldTime() % 40 == 0 && !this.cropMap.isEmpty() && WorldBaseGen.random.nextInt(50) == 0) {
            List<TileEntityCrop> crops = this.cropMap.values().stream()
                    .filter(cropTile -> cropTile.crop != null && cropTile.crop.getTick() == cropTile.crop.getMaxTick())
                    .collect(Collectors.toList());

            if (crops.size() < 2) {
                if (this
                        .getWorld()
                        .getWorldTime() % 400 == 0 && tickPest == 0 && this.crop == null && WorldBaseGen.random.nextInt(200) == 0) {
                    this.hasDouble = false;
                    new PacketUpdateFieldTile(this, "hasDouble", this.hasDouble);
                    this.crop = CropInit.weed_seed.copy();
                    pestUse = 0;
                    this.cropItem = CropInit.weed_seed.getStack();
                    new PacketUpdateFieldTile(this, "cropItem", this.cropItem);
                    new PacketUpdateFieldTile(this, "crop", this.crop);
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
                            world,
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
                            world,
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
                new PacketUpdateFieldTile(this, "hasDouble", this.hasDouble);

                this.cropItem = combinedGenome.getStack();
                newCrop.setStack(cropItem);
                this.crop = newCrop;
                this.genome = combinedGenome;
                this.genome.loadCrop(this.crop);
                new PacketUpdateFieldTile(this, "cropItem", this.cropItem);
                new PacketUpdateFieldTile(this, "crop", this.crop);
                can = true;
                break;
            }
            if (!can) {
                if (this
                        .getWorld()
                        .getWorldTime() % 400 == 0 && tickPest == 0 && this.crop == null && WorldBaseGen.random.nextInt(200) == 0) {
                    this.hasDouble = false;
                    new PacketUpdateFieldTile(this, "hasDouble", this.hasDouble);
                    this.crop = CropInit.weed_seed.copy();
                    pestUse = 0;
                    this.cropItem = CropInit.weed_seed.getStack();
                    new PacketUpdateFieldTile(this, "cropItem", this.cropItem);
                    new PacketUpdateFieldTile(this, "crop", this.crop);
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
                    .collect(
                            Collectors.toList());
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


    public boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return this.getWorld().isRemote || this.rightClick(player, hand);
    }


    @Override
    public boolean onSneakingActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.cropItem.isEmpty() && crop != null && !this.getWorld().isRemote && crop.getId() != 3) {
            if (this.crop != null && this.crop.getTick() == this.crop.getMaxTick() && this.crop.getId() != 3) {
                harvest(true);
            }
            ModUtils.dropAsEntity(world, pos, this.cropItem, 1);
            this.cropItem = ItemStack.EMPTY;
            this.crop = null;
            this.genome = null;
            new PacketUpdateFieldTile(this, "cropItem1", "");
        }
        return super.onSneakingActivated(player, hand, side, hitX, hitY, hitZ);
    }

    private boolean rightClick(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);


        if (stack.isEmpty() && this.hasDouble && this.crop == null) {
            handleDoubleCropRemoval(player);
            return true;
        }


        if (stack.isItemEqual(this.getPickBlock(player, null)) && !this.hasDouble && this.crop == null) {
            stack.shrink(1);
            this.hasDouble = true;
            sendUpdatePacket("hasDouble", this.hasDouble);
            return true;
        }


        if (stack.getItem() instanceof ItemHoe && !this.hasDouble && this.crop != null && this.crop.getId() == 3) {
            resetCrop();
            sendUpdatePacket("cropItem1", "");
            return true;
        }


        if (stack.getItem() == IUItem.fertilizer && !this.hasDouble && this.crop != null && this.crop.getTick() < this.crop.getMaxTick() && this.crop.getId() != 3) {
            fertilizeCrop(stack);
            return true;
        }


        if (stack.getItem() instanceof ICropItem && !this.hasDouble && this.crop == null && CropNetwork.instance.canPlantCrop(
                stack,
                world,
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


    private void handleDoubleCropRemoval(EntityPlayer player) {
        ItemStack stack1 = this.getPickBlock(player, null);
        this.hasDouble = false;
        ModUtils.dropAsEntity(world, pos, stack1);
        sendUpdatePacket("hasDouble", this.hasDouble);
    }

    public void resetCrop() {
        this.crop = null;
        this.cropItem = ItemStack.EMPTY;
        this.genome = null;
    }


    public void fertilizeCrop(ItemStack stack) {
        stack.shrink(1);
        final int stage = this.crop.getStage();
        this.crop.addTick((int) (this.crop.getMaxTick() * 0.2));
        this.pestUse++;
        event();
        if (this.pestUse > 40) {
            handlePestResistance();
        } else {
            checkAndUpdateCropStage(stage);
        }
    }


    private void handlePestResistance() {
        int resistance = this.crop.getPestResistance();
        boolean pestResistance = WorldBaseGen.random.nextInt(100) < (100 - resistance);
        if (!pestResistance) {
            this.crop = CropInit.weed_seed.copy();
            this.pestUse = 0;
            this.cropItem = CropInit.weed_seed.getStack();
            this.genome = new Genome(this.cropItem);
            sendUpdatePacket("cropItem", this.cropItem);
            sendUpdatePacket("crop", this.crop);
        } else {
            checkAndUpdateCropStage(this.crop.getStage());
        }
    }


    private void checkAndUpdateCropStage(int previousStage) {
        boolean needUpdate = previousStage != this.crop.getStage();
        if (needUpdate) {
            sendUpdatePacket("tick", this.crop.getTick());
        }
    }


    public void plantNewCrop(ItemStack stack) {
        this.cropItem = stack.copy();
        this.cropItem.setCount(1);
        stack.shrink(1);
        this.genome = new Genome(this.cropItem);
        this.crop = CropNetwork.instance.getCropFromStack(this.cropItem).copy();
        this.genome.loadCrop(this.crop);
        sendUpdatePacket("cropItem", this.cropItem);
        sendUpdatePacket("crop", this.crop);
    }


    public void sendUpdatePacket(String fieldName, Object value) {
        new PacketUpdateFieldTile(this, fieldName, value);
    }


    public SoundType getBlockSound(Entity entity) {
        return SoundType.PLANT;
    }


    public AxisAlignedBB getPhysicsBoundingBox() {
        return new AxisAlignedBB(
                0.20000000298023224,
                -0.0625,
                0.20000000298023224,
                0.800000011920929,
                0.8500000238418579,
                0.800000011920929
        );
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        List<AxisAlignedBB> ret = new ArrayList<>();
        if (forCollision) {
            ret.add(new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        } else {
            ret.add(new AxisAlignedBB(
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


    public TileEntityBlockStateContainer.PropertiesStateInstance getExtendedState(TileEntityBlockStateContainer.PropertiesStateInstance state) {
        state = super.getExtendedState(state);
        CropRenderState renderState = this.cropRenderState;
        if (renderState != null) {
            state = state.withProperties(renderStateProperty, renderState);
        }

        return state;
    }

    private void updateRenderState() {
        this.cropRenderState = new CropRenderState(this.crop, hasDouble, false);
    }

    public boolean wrenchCanRemove(EntityPlayer player) {
        return false;
    }


}
