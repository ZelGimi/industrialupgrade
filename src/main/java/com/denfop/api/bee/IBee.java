package com.denfop.api.bee;

import com.denfop.api.agriculture.ICrop;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.INetworkObject;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.biome.Biome;

import java.util.List;

public interface IBee extends INetworkObject {

    int getMaxSwarm();


    IBee copy();

    ICrop getCropFlower();

    List<IBee> getUnCompatibleBees();

    boolean isSun();

    boolean isNight();




    int getWeatherResistance();

    void setUnCompatibleBees(List<IBee> bees);

    int getChance();

    void setChance(final int chance);

    int getId();

    List<Biome> getBiomes();


    boolean canWorkInBiome(Biome biomeName);

    void addBiome(Biome biomeName);



    int getOffspring();


    AxisAlignedBB getSizeTerritory();


    int getTickLifecycles();


    double getMaxMortalityRate();

    int getTickBirthRate();

    List<Product> getProduct();

    void addPercentProduct(ICrop crop, double percent);

    void removeAllPercent(double range);

    String getName();


    void readPacket(CustomPacketBuffer buffer);

}
