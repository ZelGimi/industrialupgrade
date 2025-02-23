package com.denfop.api.space.colonies.api;

import com.denfop.api.space.IBody;
import com.denfop.api.space.colonies.api.building.IBuildingHouse;
import com.denfop.api.space.colonies.api.building.IColonyMiningFactory;
import com.denfop.api.space.colonies.api.building.IColonyStorage;
import com.denfop.api.space.colonies.api.building.IEntertainment;
import com.denfop.api.space.colonies.api.building.IFactory;
import com.denfop.api.space.colonies.api.building.IGenerator;
import com.denfop.api.space.colonies.api.building.IOxygenFactory;
import com.denfop.api.space.colonies.api.building.IProtectionBuilding;
import com.denfop.api.space.colonies.api.building.IStorage;
import com.denfop.api.space.colonies.enums.EnumHouses;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.UUID;

public interface IColony {

    CustomPacketBuffer writePacket();

    boolean isAuto();

    List<ItemStack> getStacksFromStorage();

    List<FluidStack> getFluidsFromStorage();
    void setAuto(boolean auto);
    int getProtection();

    int getLevel();

    int getExperience();

    int getAvailableBuilding();

    int getMaxBuilding();

    int getMaxExperience();

    void addBuilding(IColonyBuilding buildingHouse);

    boolean matched(IBody body);

    IBody getBody();

    UUID getFakePlayer();

    List<EnumProblems> getProblems();

    int getEnergy();

    int getMaxEnergy();

    int getOxygen();

    int getTimeToDelete();

    int getMaxOxygen();

    List<IBuildingHouse> getBuildingHouseList();

    List<IColonyMiningFactory> getBuildingMiningList();

    byte getToDelete();

    int getWorkers();

    int getFreeWorkers();

    List<IFactory> getFactories();

    List<IGenerator> getGenerators();

    short getNeedWorkers();

    int getMaxenergy();

    int getMaxoxygen();

    List<EnumProblems> getEnumProblemsList();

    List<IColonyBuilding> getList();

    List<IOxygenFactory> getOxygenFactoriesList();

    List<IProtectionBuilding> getProtections();

    int getGenerationEnergy();

    int getGenerationFood();

    int getGenerationOxygen();

    void update();

    void useEnergy(int energy);

    List<IColonyStorage> getColonyStorages();

    int getUsingOxygen();

    int getUsingFood();

    int getUsingEnergy();

    double getPercentEntertainment();

    List<IColonyBuilding> getColonyBuilding();

    List<IEntertainment> getEntertainments();

    void useOxygen(int oxygen);

    void addOxygen(int oxygen);


    void addEnergy(int energy);

    void addMaxOxygen(int oxygen);

    void addMaxEnergy(int energy);


    NBTTagCompound writeNBT(NBTTagCompound tag);

    List<IColonyBuilding> getBuildingList();

    List<IStorage> getStorageList();

    void addStorage(IStorage storage);


    int getNeededWorkers();

    int getMaxWorkers();

    void removeWorkers(int i);

    int getFood();

    void useFood(int food);

    void addFood(int food);

    void addProtection(int protection);

    boolean consumeEnergy(int energy);

    void addEntertainment(short entertainment);

    short getEntertainment();

    boolean canUseFood(EnumHouses houses);

    boolean canUseOxygen(EnumHouses houses);

    void addFreeWorkers(int prev);

}
