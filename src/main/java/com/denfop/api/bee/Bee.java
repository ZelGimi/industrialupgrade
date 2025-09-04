package com.denfop.api.bee;

import com.denfop.api.crop.ICrop;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.INetworkObject;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;

import java.util.List;

public interface Bee extends INetworkObject {

    int getMaxSwarm();


    Bee copy();

    ICrop getCropFlower();

    List<Bee> getUnCompatibleBees();

    void setUnCompatibleBees(List<Bee> bees);

    boolean isSun();

    boolean isNight();

    int getWeatherResistance();

    int getChance();

    void setChance(final int chance);

    int getId();

    List<ResourceKey<Biome>> getBiomes();


    boolean canWorkInBiome(ResourceKey<Biome> biomeName);

    boolean canWorkInBiome(Biome biomeName, Level level);

    void addBiome(ResourceKey<Biome> biomeName);


    int getOffspring();


    AABB getSizeTerritory();


    int getTickLifecycles();


    double getMaxMortalityRate();

    int getTickBirthRate();

    List<Product> getProduct();

    void addPercentProduct(ICrop crop, double percent);

    void removeAllPercent(double range);

    String getName();


    void readPacket(CustomPacketBuffer buffer);

}
