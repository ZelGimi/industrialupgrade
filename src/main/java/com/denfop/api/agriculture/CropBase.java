package com.denfop.api.agriculture;

import com.denfop.IUItem;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CropBase implements ICrop {

    private final String name;
    private final short id;
    private final EnumSoil soil;
    private final byte defaultWaterRequirement;
    private final byte defaultPestResistance;
    private final byte defaultLightLevel;
    private final byte defaultWeatherResistance;
    private final List<ICrop> unCompatibleCrops;
    private final byte render;
    private ItemStack stack;
    private final boolean isCombine;
    private final List<ICrop> cropCombine;
    private final int maxTick;
    private byte genomeResistance;
    private byte genomeAdaptive;
    private byte chance;
    private boolean isBeeCombine;
    private LevelPollution airRequirements;
    private EnumLevelRadiation radiationRequirements;
    private int generation;
    private boolean sun;
    private boolean night;
    private byte chanceWeed;
    private byte sizeSeed;
    private int tick;
    private boolean ignoreSoil;
    private byte yield;
    private byte weatherResistance;
    private byte waterRequirement;
    private double growthSpeed;
    private byte pestResistance;
    private byte lightLevel;
    private LevelPollution soilRequirements;
    private final List< ResourceKey<Biome> > biomes;
    private final List<ICrop> compatibleCrops;
    private List<ItemStack> drops;
    private ResourceLocation texture;
    private byte stage;
    private byte maxStage;
    private final List<ResourceLocation> textures;
    private final List<ResourceLocation> textures_top;

    public CropBase(
            String name, int id, EnumSoil soil, ItemStack stack, int yield, int weatherResistance, int waterRequirement,
            double growthSpeed,
            int pestResistance,
            int maxStage, int sizeSeed, ResourceLocation texture, boolean isCombine, List<ICrop> cropCombine, int chance,
            boolean isBeeCombine
            , int tick, int chanceWeed, boolean sun, boolean night, List<ItemStack> drop, int lightLevel,
            List<ICrop> unCompatibleCrops, int render
    ) {


        this.yield = (byte) yield;
        this.name = name;
        this.unCompatibleCrops = unCompatibleCrops;
        this.generation = 0;
        this.id = (short) id;
        this.sun = sun;
        this.night = night;
        this.chanceWeed = (byte) chanceWeed;
        this.sizeSeed = (byte) sizeSeed;
        this.weatherResistance = (byte) weatherResistance;
        this.defaultWeatherResistance = (byte) weatherResistance;
        this.waterRequirement = (byte) waterRequirement;
        this.defaultWaterRequirement = (byte) waterRequirement;
        this.growthSpeed = growthSpeed;
        this.pestResistance = (byte) pestResistance;
        this.defaultPestResistance = (byte) pestResistance;
        this.maxStage = (byte) maxStage;
        this.stage = 0;
        this.render = (byte) render;
        this.biomes = new ArrayList<>();
        this.drops = drop;
        this.textures = new ArrayList<>();
        this.textures_top = new ArrayList<>();
        this.soil = soil;
        this.ignoreSoil = false;
        this.stack = stack;
        final CompoundTag nbt = ModUtils.nbt(this.stack);
        nbt.putInt("crop_id",id);
        this.isCombine = isCombine;
        this.cropCombine = cropCombine;
        this.compatibleCrops = cropCombine;
        this.chance = (byte) chance;
        this.isBeeCombine = isBeeCombine;
        this.setTexture(texture, render);
        this.maxTick = tick;
        this.tick = 0;
        this.soilRequirements = LevelPollution.LOW;
        this.airRequirements = LevelPollution.LOW;
        this.radiationRequirements = EnumLevelRadiation.LOW;
        this.genomeResistance = 0;
        this.genomeAdaptive = 0;
        this.lightLevel = (byte) lightLevel;
        this.defaultLightLevel = (byte) lightLevel;
        CropNetwork.instance.addCrop(this);
    }
    public CropBase(
            String name, int id, EnumSoil soil, int yield, int weatherResistance, int waterRequirement,
            double growthSpeed,
            int pestResistance,
            int maxStage, int sizeSeed, ResourceLocation texture, boolean isCombine, List<ICrop> cropCombine, int chance,
            boolean isBeeCombine
            , int tick, int chanceWeed, boolean sun, boolean night, List<ItemStack> drop, int lightLevel,
            List<ICrop> unCompatibleCrops, int render
    ) {
        this.yield = (byte) yield;
        this.name = name;
        this.unCompatibleCrops = unCompatibleCrops;
        this.generation = 0;
        this.id = (short) id;
        this.sun = sun;
        this.night = night;
        this.chanceWeed = (byte) chanceWeed;
        this.sizeSeed = (byte) sizeSeed;
        this.weatherResistance = (byte) weatherResistance;
        this.defaultWeatherResistance = (byte) weatherResistance;
        this.waterRequirement = (byte) waterRequirement;
        this.defaultWaterRequirement = (byte) waterRequirement;
        this.growthSpeed = growthSpeed;
        this.pestResistance = (byte) pestResistance;
        this.defaultPestResistance = (byte) pestResistance;
        this.maxStage = (byte) maxStage;
        this.stage = 0;
        this.render = (byte) render;
        this.biomes = new ArrayList<>();
        this.drops = drop;
        this.textures = new ArrayList<>();
        this.textures_top = new ArrayList<>();
        this.soil = soil;
        this.ignoreSoil = false;
        this.stack = new ItemStack(IUItem.crops.getStack(0));
        final CompoundTag nbt = ModUtils.nbt(this.stack);
        nbt.putInt("crop_id",id);
        this.isCombine = isCombine || !cropCombine.isEmpty();
        this.cropCombine = cropCombine;
        this.compatibleCrops = cropCombine;
        this.chance = (byte) chance;
        this.isBeeCombine = isBeeCombine;
        this.setTexture(texture, render);
        this.maxTick = tick;
        this.tick = 0;
        this.soilRequirements = LevelPollution.LOW;
        this.airRequirements = LevelPollution.LOW;
        this.radiationRequirements = EnumLevelRadiation.LOW;
        this.genomeResistance = 0;
        this.genomeAdaptive = 0;
        this.lightLevel = (byte) lightLevel;
        this.defaultLightLevel = (byte) lightLevel;
        CropNetwork.instance.addCrop(this);
    }

    public CropBase copy() {
        final CropBase cropBase = new CropBase(
                this.name,
                this.id,
                this.soil,
                this.stack.copy(),
                this.yield,
                this.defaultWeatherResistance,
                this.defaultWaterRequirement,
                this.growthSpeed,
                this.defaultPestResistance,
                this.maxStage,
                this.sizeSeed,
                this.texture,
                this.isCombine,
                this.cropCombine,
                this.chance,
                this.isBeeCombine,
                this.maxTick,
                this.chanceWeed,
                this.sun,
                this.night,
                this.drops,
                this.defaultLightLevel, this.unCompatibleCrops, this.render
        );
        cropBase.setAirRequirements(LevelPollution.LOW);
        cropBase.setRadiationRequirements(this.radiationRequirements);
        cropBase.setSoilRequirements(this.soilRequirements);
        cropBase.setGeneration(this.getGeneration());
        cropBase.setGenomeAdaptive(this.genomeAdaptive);
        cropBase.setGenomeResistance(this.genomeResistance);
        for ( ResourceKey<Biome>  biome : biomes) {
            cropBase.addBiome(biome);
        }
        return cropBase;
    }

    public int getDefaultPestResistance() {
        return defaultPestResistance;
    }

    public int getDefaultWeatherResistance() {
        return defaultWeatherResistance;
    }

    public int getGenomeAdaptive() {
        return genomeAdaptive;
    }

    public void setGenomeAdaptive(final int genomeAdaptive) {
        this.genomeAdaptive = (byte) genomeAdaptive;
    }

    public int getGenomeResistance() {
        return genomeResistance;
    }

    public void setGenomeResistance(final int genomeResistance) {
        this.genomeResistance = (byte) genomeResistance;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(final int generation) {
        this.generation = generation;
    }

    public EnumLevelRadiation getRadiationRequirements() {
        return radiationRequirements;
    }

    public void setRadiationRequirements(final EnumLevelRadiation radiationRequirements) {
        this.radiationRequirements = radiationRequirements;
    }

    public LevelPollution getAirRequirements() {
        return airRequirements;
    }

    public void setAirRequirements(final LevelPollution airRequirements) {
        this.airRequirements = airRequirements;
    }

    public boolean isBeeCombine() {
        return isBeeCombine;
    }

    public void setBeeCombine(final boolean beeCombine) {
        isBeeCombine = beeCombine;
    }

    public int getTick() {
        return tick;
    }

    public void addTick(int tick) {
        this.tick += tick;
        if (this.tick >= this.maxTick) {
            this.tick = maxTick;
        }

        this.stage = (byte) Math.max((int) Math.ceil(maxStage * (this.tick * 1D / this.maxTick)) - 1, 0);
    }

    public void setTick(int tick) {
        this.tick = tick;
        if (this.tick >= this.maxTick) {
            this.tick = maxTick;
        }

        this.stage = (byte) Math.max((int) Math.ceil(maxStage * (this.tick * 1D / this.maxTick)) - 1, 0);
    }

    public boolean isCombine() {
        return isCombine;
    }

    public int getMaxTick() {
        return maxTick;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(final int chance) {
        this.chance = (byte) chance;
    }

    public List<ICrop> getCropCombine() {
        return cropCombine;
    }

    public ItemStack getStackForDrop() {
        return stack;
    }

    ;

    public ItemStack getStack() {
        return stack.copy();
    }


    public boolean isSun() {
        return sun;
    }

    public void setSun(final boolean sun) {
        this.sun = sun;
    }

    public boolean isNight() {
        return night;
    }

    public void setNight(final boolean night) {
        this.night = night;
    }

    @Override
    public EnumSoil getSoil() {
        return soil;
    }

    public boolean isIgnoreSoil() {
        return ignoreSoil;
    }

    public void setIgnoreSoil(final boolean ignoreSoil) {
        this.ignoreSoil = ignoreSoil;
    }

    public int getId() {
        return id;
    }


    public int getDefaultWaterRequirement() {
        return defaultWaterRequirement;
    }


    public int getSizeSeed() {
        return sizeSeed;
    }

    public void addSizeSeed(int col) {
        this.sizeSeed = (byte) col;
    }

    public int getChanceWeed() {
        return chanceWeed;
    }

    public void addChanceWeed(int col) {
        this.chanceWeed = (byte) col;
    }

    public int getDefaultLightLevel() {
        return defaultLightLevel;
    }

    public String getName() {
        return name;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public int getPestResistance() {
        return pestResistance;
    }

    @Override
    public void setPestResistance(int resistance) {
        this.pestResistance = (byte) resistance;
    }

    public LevelPollution getSoilRequirements() {
        return soilRequirements;
    }

    @Override
    public void setSoilRequirements(LevelPollution pollution) {
        this.soilRequirements = pollution;
    }

    public List< ResourceKey<Biome> > getBiomes() {
        return biomes;
    }

    @Override
    public boolean isCombineWithCrops(final List<ICrop> crops) {

        if (crops.size() != compatibleCrops.size())
            return false;

        cycle1:
        for (ICrop crop : crops) {
            for (ICrop crop1 : compatibleCrops) {
                if (crop1.getId() == crop.getId()) {
                    continue cycle1;
                }
            }

            return false;
        }
        return true;

    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    @Override
    public int getYield() {
        return yield;
    }

    @Override
    public void setYield(int yield) {
        this.yield = (byte) yield;
    }

    @Override
    public int getWeatherResistance() {
        return weatherResistance;
    }

    @Override
    public void setWeatherResistance(int resistance) {
        this.weatherResistance = (byte) resistance;
    }

    @Override
    public int getWaterRequirement() {
        return waterRequirement;
    }

    @Override
    public void setWaterRequirement(int waterRequirement) {
        this.waterRequirement = (byte) waterRequirement;
    }

    @Override
    public double getGrowthSpeed() {
        return growthSpeed;
    }

    @Override
    public void setGrowthSpeed(double speed) {
        this.growthSpeed = (double) speed;
    }

    @Override
    public boolean canGrowInBiome(Biome biomeName, Level level) {
        ResourceKey<Biome> biomeKey = level.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getResourceKey(biomeName).get();
        return biomes.contains(biomeKey);
    }
    @Override
    public boolean canGrowInBiome(ResourceKey<Biome> biomeName) {
        return biomes.contains(biomeName);
    }
    @Override
    public void addBiome( ResourceKey<Biome>  biomeName) {
        if (!biomes.contains(biomeName)) {
            biomes.add(biomeName);
        }
    }

    @Override
    public void removeBiome( ResourceKey<Biome>  biomeName) {
        biomes.remove(biomeName);
    }

    @Override
    public void setLight(int lightLevel) {
        this.lightLevel = (byte) lightLevel;
    }

    @Override
    public boolean compatibilityWithCrop(ICrop crop) {
        return unCompatibleCrops.contains(crop);
    }

    @Override
    public List<ItemStack> getDrop() {
        return drops;
    }

    @Override
    public void setDrop(List<ItemStack> drop) {
        this.drops = drop;
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    public void setTexture(ResourceLocation texture, final int render) {
        this.texture = texture;
        textures.clear();
        textures_top.clear();
        for (int i = 0; i < maxStage; i++) {
            textures.add(new ResourceLocation(this.texture.getNamespace(), this.texture.getPath() + "_" + i));
            if (render >= 3) {
                textures_top.add(new ResourceLocation(
                        this.texture.getNamespace(),
                        this.texture.getPath() + "_top_" + i
                ));
            }
        }

    }

    @Override
    public ResourceLocation getTexture(int stage) {
        return textures.get(stage);
    }

    @Override
    public ResourceLocation getTextureTop(int stage) {
        return textures_top.get(stage);
    }

    public List<ResourceLocation> getTextures() {
        return textures;
    }

    @Override
    public int getStage() {
        return (int) (this.getMaxStage() *(getTick() * 1F/this.getMaxTick()));
    }

    @Override
    public int getMaxStage() {
        return maxStage - 1;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CropBase cropBase = (CropBase) o;
        return id == cropBase.id && stage == cropBase.stage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stage);
    }

    @Override
    public String toString() {
        return "CropBase{" +
                "yield=" + yield +
                ", weatherResistance=" + weatherResistance +
                ", waterRequirement=" + waterRequirement +
                ", growthSpeed=" + growthSpeed +
                ", pestResistance=" + pestResistance +
                ", lightLevel=" + lightLevel +
                ", soilRequirements=" + soilRequirements +
                ", biomes=" + biomes +
                ", compatibleCrops=" + compatibleCrops +
                ", drops=" + drops +
                ", texture=" + texture +
                ", stage=" + stage +
                ", maxStage=" + maxStage +
                '}';
    }

    public void readPacket(CustomPacketBuffer buffer) {
        this.yield = buffer.readByte();
        this.tick = buffer.readInt();
        this.generation = buffer.readInt();
        this.stage = buffer.readByte();
        this.maxStage = buffer.readByte();
    }

    @Override
    public void setStack(final ItemStack cropItem) {
        this.stack = cropItem;
    }

    @Override
    public int getRender() {
        return this.render;
    }

    @Override
    public List<ResourceLocation> getTopTexture() {
        return textures_top;
    }

    @Override
    public void setStage(final int stage) {
        this.stage = (byte) stage;
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer buffer = new CustomPacketBuffer();


        buffer.writeByte(this.yield);
        buffer.writeInt(this.tick);
        buffer.writeInt(this.generation);

        buffer.writeByte(this.stage);
        buffer.writeByte(this.maxStage);
        return buffer;
    }


}
