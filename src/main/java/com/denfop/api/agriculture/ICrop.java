package com.denfop.api.agriculture;

import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.INetworkObject;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public interface ICrop extends INetworkObject {

    int getYield();

    void setYield(int yield);

    ICrop copy();

    boolean isSun();

    void setSun(final boolean sun);

    void setTick(int progress);
    boolean isNight();

    void setNight(final boolean night);

    int getGeneration();

    void setGeneration(final int generation);

    EnumLevelRadiation getRadiationRequirements();

    void setRadiationRequirements(final EnumLevelRadiation radiationRequirements);

    LevelPollution getAirRequirements();

    void setAirRequirements(final LevelPollution airRequirements);

    ItemStack getStack();


    boolean isIgnoreSoil();

    void setIgnoreSoil(final boolean ignoreSoil);

    boolean isBeeCombine();

    void setBeeCombine(final boolean beeCombine);

    int getGenomeAdaptive();

    int getDefaultPestResistance();

    int getDefaultWeatherResistance();

    void setGenomeAdaptive(final int genomeAdaptive);

    int getGenomeResistance();

    void setGenomeResistance(final int genomeResistance);

    boolean isCombine();

    int getChance();

    void setChance(final int chance);

    List<ICrop> getCropCombine();

    ItemStack  getStackForDrop();

    int getDefaultWaterRequirement();

    int getWeatherResistance();


    void setWeatherResistance(int resistance);

    int getId();

    int getWaterRequirement();

    void setWaterRequirement(int waterRequirement);

    int getLightLevel();

    int getPestResistance();

    void setPestResistance(int resistance);

    LevelPollution getSoilRequirements();

    void setSoilRequirements(LevelPollution pollution);

    List< ResourceKey<Biome> > getBiomes();

    boolean isCombineWithCrops(List<ICrop> crops);

    int getTick();

    int getMaxTick();

    void addTick(int tick);

    List<ItemStack> getDrops();

    double getGrowthSpeed();

    void setGrowthSpeed(double speed);

    int getSizeSeed();

    void addSizeSeed(int col);

    int getChanceWeed();

    void addChanceWeed(int col);

    int getDefaultLightLevel();

    boolean canGrowInBiome(Biome biomeName, Level level);
    boolean canGrowInBiome( ResourceKey<Biome> biomeName);

    void addBiome( ResourceKey<Biome> biomeName);

    void removeBiome( ResourceKey<Biome>  biomeName);

    void setLight(int lightLevel);

    boolean compatibilityWithCrop(ICrop crop);


    List<ItemStack> getDrop();

    void setDrop(List<ItemStack> drop);

    ResourceLocation getTexture();

    ResourceLocation getTexture(int stage);

    ResourceLocation getTextureTop(int stage);

    List<ResourceLocation> getTextures();

    String getName();

    EnumSoil getSoil();

    int getStage();

    int getMaxStage();

    void readPacket(CustomPacketBuffer buffer);

    void setStack(ItemStack cropItem);

    int getRender();

    List<ResourceLocation> getTopTexture();

    void setStage(int i);

}
