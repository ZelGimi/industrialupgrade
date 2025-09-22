package com.denfop.tiles.crop;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.agriculture.CropInit;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.EnumSoil;
import com.denfop.api.agriculture.ICrop;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.EnumGenetic;
import com.denfop.api.agriculture.genetics.GeneticTraits;
import com.denfop.api.agriculture.genetics.GeneticsManager;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.pollution.ChunkLevel;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.blocks.FluidName;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerMultiCrop;
import com.denfop.gui.GuiMultiCrop;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

import static com.denfop.api.agriculture.genetics.GeneticsManager.enumGeneticListMap;
import static com.denfop.api.agriculture.genetics.GeneticsManager.geneticTraitsMap;
import static com.denfop.api.agriculture.genetics.Genome.geneticBiomes;

public class TileEntityMultiCrop extends TileEntityInventory {

    public final Inventory downBlockSlot;
    public final Inventory upBlockSlot;
    public final Fluids.InternalFluidTank fluidPestTank;
    public final Fluids.InternalFluidTank fluidWaterTank;
    public final InventoryOutput outputSlot;
    public final Inventory fertilizerSlot;
    private final Fluids fluids;
    public ICrop[] crop;
    public Genome[] genome;
    public int[] tickPest;
    public int[] pestUse;
    public EnumSoil[] enumSoils;
    public boolean[] place;
    public int[] tickSoil;
    public int[] maxTickSoil;
    private Radiation radLevel;
    private ChunkPos chunkPos;
    private Chunk chunk;
    private Biome biome;
    private ChunkLevel chunkLevel;

    public TileEntityMultiCrop(int col) {
        tickPest = new int[col];
        pestUse = new int[col];
        enumSoils = new EnumSoil[col];
        crop = new ICrop[col];
        genome = new Genome[col];
        place = new boolean[col];
        tickSoil = new int[col];
        maxTickSoil = new int[col];
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidWaterTank = fluids.addTankInsert("waterTank", 16000, Fluids.fluidPredicate(FluidRegistry.WATER));
        this.fluidPestTank = fluids.addTankInsert("pestTank", 16000, Fluids.fluidPredicate(FluidName.fluidweed_ex.getInstance()));
        this.outputSlot = new InventoryOutput(this, 9);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1 * col / 2D));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1 * col / 2D));
        this.fertilizerSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.fertilizer;
            }

            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.FERTILIZER;
            }
        };
        this.downBlockSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, col) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return EnumSoil.contain(stack);
            }

            @Override
            public int getInventoryStackLimit() {
                return 1;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (content.isEmpty()) {
                    enumSoils[index] = null;
                    place[index] = false;
                } else {
                    enumSoils[index] = EnumSoil.get(content);
                }
            }

            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.BLOCKS;
            }
        };
        this.upBlockSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, col) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ICropItem && canPlace(((ICropItem) stack.getItem()).getCrop(stack.getItemDamage(),
                        stack), index);
            }

            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.CROP;
            }

            @Override
            public int getInventoryStackLimit() {
                return 1;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (content.isEmpty()) {
                    crop[index] = null;
                    genome[index] = null;
                    place[index] = false;
                } else {
                    genome[index] = new Genome(content);
                    crop[index] = CropNetwork.instance.getCropFromStack(content).copy();
                    genome[index].loadCrop(crop[index]);
                    place[index] = true;
                }
            }
        };

    }

    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            tickSoil = (int[]) DecoderHandler.decode(customPacketBuffer);
            maxTickSoil = (int[]) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer buffer = super.writeContainerPacket();
        try {
            EncoderHandler.encode(buffer, tickSoil);
            EncoderHandler.encode(buffer, maxTickSoil);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buffer;
    }

    @Override
    public ContainerMultiCrop getGuiContainer(final EntityPlayer var1) {
        return new ContainerMultiCrop(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiMultiCrop(getGuiContainer(var1));
    }

    public boolean canPlace(ICrop crop, int index) {
        EnumSoil soil = crop.getSoil();
        return soil == this.enumSoils[index];
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setIntArray("tickSoil", tickSoil);
        nbtTagCompound.setIntArray("maxTickSoil", maxTickSoil);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        tickSoil = nbtTagCompound.getIntArray("tickSoil");
        if (tickSoil.length == 0) {
            tickSoil = new int[crop.length];
        }
        maxTickSoil = nbtTagCompound.getIntArray("maxTickSoil");
        if (maxTickSoil.length == 0) {
            maxTickSoil = new int[crop.length];
        }
    }

    public int getTickPest(int i) {
        return tickPest[i];
    }

    public void setTickPest(int i) {
        tickPest[i] = 7000;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            for (int i = 0; i < crop.length; i++) {
                if (downBlockSlot.get(i).isEmpty()) {
                    enumSoils[i] = null;
                    place[i] = false;
                } else {
                    enumSoils[i] = EnumSoil.get(downBlockSlot.get(i));
                }
                final ItemStack content = upBlockSlot.get(i);
                if (content.isEmpty()) {
                    crop[i] = null;
                    genome[i] = null;
                    place[i] = false;
                } else {
                    genome[i] = new Genome(content);
                    crop[i] = CropNetwork.instance.getCropFromStack(content).copy();
                    genome[i].loadCrop(crop[i]);
                    crop[i].setTick(tickSoil[i]);
                    place[i] = true;
                }
            }
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
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getWorldTime() % 20 == 0) {
            for (int i = 0; i < downBlockSlot.size(); i++) {
                ICrop crop = this.crop[i];
                if (crop == null || crop.getId() == 3) {
                    this.tickSoil[i] = 0;
                    this.maxTickSoil[i] = 0;
                }
                if (crop != null) {
                    if (crop.getId() == 3) {
                        this.upBlockSlot.put(i, ItemStack.EMPTY);
                        continue;
                    }
                    this.tickSoil[i] = crop.getTick();
                    this.maxTickSoil[i] = crop.getMaxTick();
                    if (CropNetwork.instance.canMultiGrow(
                            world,
                            pos,
                            chunkPos,
                            crop,
                            radLevel,
                            chunk,
                            biome,
                            chunkLevel
                    ) && place[i] && canUseWater(crop)) {
                        if (tickPest[i] == 0 && fluidPestTank.getFluidAmount() > 0) {
                            this.setTickPest(i);
                            this.fluidPestTank.drain(1, true);
                        }
                        tickPest[i] -= 20;
                        if (crop.getTick() < crop.getMaxTick() && this
                                .getWorld()
                                .getWorldTime() % 200 == 0 && crop.getId() != 3 && tickPest[i] == 0) {
                            int chanceWeed = 100 - crop.getChanceWeed() - (5 * (crop.getSizeSeed() - 1));
                            if (chanceWeed > 0) {
                                if (this.getWorld().getWorldTime() % 200 == 0 &&
                                        WorldBaseGen.random.nextInt(100) < chanceWeed && WorldBaseGen.random.nextInt(100) == 0) {
                                    this.crop[i] = CropInit.weed_seed.copy();
                                    pestUse[i] = 0;
                                    this.upBlockSlot.put(i, CropInit.weed_seed.getStack());
                                    continue;
                                }
                            }
                        }
                        if (this.radLevel.getLevel().ordinal() > EnumLevelRadiation.HIGH.ordinal() && crop.getId() != 3) {
                            canAdaptationCrop(i);
                        }
                        if (crop.getTick() < crop.getMaxTick()) {
                            crop.addTick((int) (20 * crop.getGrowthSpeed()));
                            this.useWater(crop);
                            if (pestUse[i] < 40 && !fertilizerSlot.get().isEmpty()) {
                                pestUse[i]++;
                                crop.addTick((int) (crop.getMaxTick() * 0.2));
                                fertilizerSlot.get().shrink(1);
                            }
                            if (crop.getTick() >= crop.getMaxTick()) {
                                if (crop.getId() != 3) {
                                    outputSlot.addAll(crop.getDrops());
                                    if (WorldBaseGen.random.nextInt(100) < 10) {
                                        outputSlot.add(ModUtils.setSize(this.upBlockSlot.get(i).copy(), 1));
                                    }
                                    this.crop[i].setGeneration(crop.getGeneration() + 1);
                                    this.crop[i].setTick(0);
                                    this.canAdaptationCrop(i);
                                }
                            }
                        } else {
                            if (crop.getId() != 3) {
                                outputSlot.addAll(crop.getDrops());
                                if (WorldBaseGen.random.nextInt(100) < 10) {
                                    outputSlot.add(ModUtils.setSize(this.upBlockSlot.get(i).copy(), 1));
                                }
                                this.crop[i].setGeneration(crop.getGeneration() + 1);
                                this.crop[i].setTick(0);
                                this.canAdaptationCrop(i);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean canUseWater(ICrop crop) {
        return crop.getWaterRequirement() == 0 || this.fluidWaterTank.getFluidAmount() >= crop.getWaterRequirement();
    }

    private void useWater(ICrop crop) {
        this.fluidWaterTank.drain(crop.getWaterRequirement(), true);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
    }

    private void canAdaptationCrop(int i) {
        if (WorldBaseGen.random.nextInt(100) != 0) {
            return;
        }
        Genome genome = this.genome[i];
        ICrop crop = this.crop[i];
        final ItemStack cropItem = upBlockSlot.get(i);
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
                        genome.addGenome(geneticTraits, upBlockSlot.get(i));
                    } else {
                        boolean needRemove =
                                WorldBaseGen.random.nextInt(geneticResistance) > WorldBaseGen.random.nextInt(geneticAdaptive);
                        boolean canUpgrade =
                                WorldBaseGen.random.nextInt(geneticAdaptive) > WorldBaseGen.random.nextInt(geneticResistance);
                        if ((needRemove || !canUpgrade) && hasGenome) {
                            GeneticTraits geneticTraits = genome.removeGenome(enumGenetic, upBlockSlot.get(i));
                            GeneticsManager.instance.deleteGenomeCrop(crop, geneticTraits);
                        } else if (canUpgrade) {
                            switch (enumGenetic) {
                                case SUN:
                                    if (!hasGenome) {
                                        genome.addGenome(genetic.get(0), upBlockSlot.get(i));
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setAirRequirements(traits.getPrev().getValue(LevelPollution.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setAirRequirements(LevelPollution.LOW);
                                            }

                                        } else {
                                            GeneticTraits traits = genome.getGenome(enumGenetic);
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setSoilRequirements(traits.getPrev().getValue(LevelPollution.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setSoilRequirements(LevelPollution.LOW);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setPestResistance(crop.getPestResistance() - level + traits
                                                        .getPrev()
                                                        .getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setPestResistance(crop.getPestResistance() - level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.addChanceWeed(-level + traits.getPrev().getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.addChanceWeed(-level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setLight(traits.getPrev().getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setLight(crop.getDefaultLightLevel());
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setRadiationRequirements(traits
                                                        .getPrev()
                                                        .getValue(EnumLevelRadiation.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setRadiationRequirements(EnumLevelRadiation.LOW);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.addSizeSeed(-level + traits.getPrev().getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.addSizeSeed(-level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setYield(crop.getYield() - level + traits.getPrev().getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setYield(crop.getYield() - level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setChance(crop.getChance() - level + traits
                                                        .getPrev()
                                                        .getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setChance(crop.getChance() - level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setGrowthSpeed(traits
                                                        .getPrev()
                                                        .getValue(Double.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setGrowthSpeed(1);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setWeatherResistance(traits.getPrev().getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setWeatherResistance(crop.getDefaultWeatherResistance());
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setGenomeAdaptive(crop.getGenomeAdaptive() - level + traits
                                                        .getPrev()
                                                        .getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setGenomeAdaptive(crop.getGenomeAdaptive() - level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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
                                                genome.removeGenome(traits, cropItem);
                                                genome.addGenome(traits.getPrev(), cropItem);
                                                crop.setGenomeResistance(crop.getGenomeResistance() - level + traits
                                                        .getPrev()
                                                        .getValue(Integer.class));
                                            } else {
                                                genome.removeGenome(traits, cropItem);
                                                crop.setGenomeResistance(crop.getGenomeResistance() - level);
                                            }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, cropItem);
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

}
