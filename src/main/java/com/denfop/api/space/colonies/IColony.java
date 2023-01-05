package com.denfop.api.space.colonies;

import com.denfop.api.space.IBody;
import com.denfop.api.space.fakebody.FakePlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface IColony {

    int getProtection();

    void addProtection(int protection);

    void removeProtection(int protection);

    int getLevel();

    void update(EnumTypeUpdate typeUpdate);

    boolean matched(IBody body);

    IBody getBody();

    FakePlayer getFakePlayer();

    void setFakePlayer(FakePlayer fakePlayer);

    List<EnumProblems> getProblems();

    int getEnergy();

    int getMaxEnergy();

    int getOxygen();

    int getNeededWorkers();

    void setNeededWorkers(int workers);

    void addNeededWorkers(int workers);

    void removeNeededWorkers(int workers);

    List<IBuildingHouse> getHouse();

    List<IBuildingMining> getMining();

    List<IOxygenFactory> getOxygenFactory();

    int getTimeToDelete();

    void setTimeToDelete(int time);

    void addTimeToDelete(int time);

    int getMaxOxygen();

    int getMaxWorkers();

    void addWorkers(int workers);

    void removeWorkers(int workers);

    void update();

    void useEnergy(int energy);

    void decreaseEnergy(int energy);

    void useOxygen(int oxygen);

    void decreaseOxygen(int oxygen);

    void addOxygen(int oxygen);

    int getDemandedEnergy();

    int getDemandedOxygen();

    void addEnergy(int energy);

    void addMaxOxygen(int oxygen);

    void addMaxEnergy(int energy);

    void addConsumeEnergy(int energy);

    void removeConsumeEnergy(int energy);

    int getConsume();

    NBTTagCompound writeNBT(NBTTagCompound tag);

    List<IColonyBuilding> getBuildingList();

    void addBuilding(IColonyBuilding building);

    List<IStorage> getStorageList();

    void addStorage(IStorage storage);

    void removeStorage(IStorage storage);

    void addNeedProtection(int protection);

    void removeNeedProtection(int protection);

}
