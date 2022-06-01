package com.denfop.api.space.colonies;

import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.FakePlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class Colony implements IColony {

    private final IBody body;
    private final int level;
    private FakePlayer fakeplayer;
    private List<IColonyBuilding> list;
    private List<IBuildingHouse> buildingHouseList;
    private List<IBuildingMining> buildingMiningList;
    private List<IOxygenFactory> oxygenFactoriesList;
    private List<EnumProblems> enumProblemsList;
    private List<IStorage> storageList;
    private int energy = 0;
    private int maxenergy = 0;
    private int oxygen = 0;
    private int maxoxygen = 0;
    private int workers = 0;
    private int needworkers = 0;
    private int consume = 0;
    private int toDelete;
    private int protection;
    private int needprotection;

    public Colony(IBody body, FakePlayer player) {
        this.level = 1;
        this.body = body;
        this.list = new ArrayList<>();
        this.toDelete = 600;
        this.fakeplayer = player;
        this.enumProblemsList = new ArrayList<>();
        this.storageList = new ArrayList<>();
        this.buildingHouseList = new ArrayList<>();
        this.buildingMiningList = new ArrayList<>();
        this.oxygenFactoriesList = new ArrayList<>();
    }

    public Colony(NBTTagCompound tag, FakePlayer fakeplayer) {
        this.level = tag.getInteger("level");
        this.body = SpaceNet.instance.getBodyFromName(tag.getString("name"));
        this.fakeplayer = fakeplayer;
        int size = tag.getInteger("col");
        for (int i = 0; i < size; i++) {
            NBTTagCompound nbt = tag.getCompoundTag("" + i);
            String type = nbt.getString("type");
            switch (type) {
                case "oxygenfactory":
                    new OxygenFactory(nbt, this);
                    break;
                case "storage":
                    new StorageBuilding(nbt, this);
                    break;
                case "house":
                    new ColonyHouse(nbt, this);
                    break;
                case "solar":
                    new ColonyPanelFactory(nbt, this);
                    break;
                case "factory":
                    new Factory(nbt, this);
                    break;
                case "protection":
                    new ProtectionBuilding(nbt, this);
                    break;
                case "fluidfactory":
                    new FluidFactory(nbt, this);
                    break;
            }
        }
    }

    @Override
    public int getProtection() {
        return this.protection;
    }

    @Override
    public void addProtection(final int protection) {
        this.protection += protection;
    }

    @Override
    public void removeProtection(final int protection) {
        this.protection -= protection;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void update(final EnumTypeUpdate typeUpdate) {

    }

    @Override
    public boolean matched(final IBody body) {
        return this.body == body;
    }

    @Override
    public IBody getBody() {
        return this.body;
    }

    @Override
    public FakePlayer getFakePlayer() {
        return this.fakeplayer;
    }

    @Override
    public void setFakePlayer(final FakePlayer fakePlayer) {
        this.fakeplayer = fakePlayer;
    }

    @Override
    public List<EnumProblems> getProblems() {
        return this.enumProblemsList;
    }

    @Override
    public int getEnergy() {
        return this.energy;
    }

    @Override
    public int getMaxEnergy() {
        return this.maxenergy;
    }

    @Override
    public int getOxygen() {
        return this.oxygen;
    }

    @Override
    public int getNeededWorkers() {
        return this.needworkers;
    }

    @Override
    public void setNeededWorkers(final int workers) {
        this.needworkers = workers;
    }

    @Override
    public void addNeededWorkers(final int workers) {
        this.needworkers += workers;
    }

    @Override
    public void removeNeededWorkers(final int workers) {
        this.needworkers -= workers;
    }

    @Override
    public List<IBuildingHouse> getHouse() {
        return this.buildingHouseList;
    }

    @Override
    public List<IBuildingMining> getMining() {
        return this.buildingMiningList;
    }

    @Override
    public List<IOxygenFactory> getOxygenFactory() {
        return this.oxygenFactoriesList;
    }

    @Override
    public int getTimeToDelete() {
        return this.toDelete;
    }

    @Override
    public void setTimeToDelete(final int time) {
        this.toDelete = time;
    }

    @Override
    public void addTimeToDelete(final int time) {
        this.toDelete += time;
    }

    @Override
    public int getMaxOxygen() {
        return this.maxoxygen;
    }

    @Override
    public int getMaxWorkers() {
        return this.workers;
    }

    @Override
    public void addWorkers(final int workers) {
        this.workers += workers;
    }

    @Override
    public void removeWorkers(final int workers) {
        this.workers -= workers;
    }

    @Override
    public void update() {
        if (this.enumProblemsList.size() > 0) {
            if (toDelete == 0) {
                SpaceNet.instance.getColonieNet().removeColony(this, this.getFakePlayer());
            } else {
                toDelete -= 1;
            }
        } else {
            toDelete = 600;
        }

        if (this.workers < this.needworkers) {
            if (!this.enumProblemsList.contains(EnumProblems.WORKERS)) {
                this.enumProblemsList.add(EnumProblems.WORKERS);
            }
            return;
        }
        for (IColonyBuilding building : this.list) {
            building.work();
        }
    }

    @Override
    public void useEnergy(final int energy) {
        this.energy -= energy;
    }

    @Override
    public void decreaseEnergy(final int energy) {
        this.maxenergy -= energy;
    }

    @Override
    public void useOxygen(final int oxygen) {
        this.oxygen -= oxygen;
    }

    @Override
    public void decreaseOxygen(final int oxygen) {
        this.maxoxygen -= oxygen;
    }

    @Override
    public void addOxygen(final int oxygen) {
        this.oxygen += oxygen;
    }

    @Override
    public int getDemandedEnergy() {
        return this.maxenergy - this.energy;
    }

    @Override
    public int getDemandedOxygen() {
        return this.maxoxygen - this.oxygen;
    }

    @Override
    public void addEnergy(final int energy) {
        this.energy += energy;
    }

    @Override
    public void addMaxOxygen(final int oxygen) {
        this.oxygen += oxygen;
    }

    @Override
    public void addMaxEnergy(final int energy) {
        this.maxenergy += energy;
    }

    @Override
    public void addConsumeEnergy(final int energy) {
        this.consume += energy;
    }

    @Override
    public void removeConsumeEnergy(final int energy) {
        this.consume -= energy;
    }

    @Override
    public int getConsume() {
        return this.consume;
    }


    @Override
    public NBTTagCompound writeNBT(final NBTTagCompound tag) {
        tag.setString("name", this.body.getName());
        tag.setInteger("level", this.level);
        tag.setInteger("col", this.list.size());
        for (int i = 0; i < this.list.size(); i++) {
            tag.setTag("" + i, this.list.get(i).writeTag(new NBTTagCompound()));
        }
        return tag;
    }

    @Override
    public List<IColonyBuilding> getBuildingList() {
        return this.list;
    }

    @Override
    public void addBuilding(final IColonyBuilding building) {
        this.list.add(building);
    }


    @Override
    public List<IStorage> getStorageList() {
        return this.storageList;
    }

    @Override
    public void addStorage(final IStorage storage) {
        this.storageList.add(storage);
    }

    @Override
    public void removeStorage(final IStorage storage) {
        this.storageList.remove(storage);
    }

    @Override
    public void addNeedProtection(final int protection) {
        this.needprotection += protection;
    }

    @Override
    public void removeNeedProtection(final int protection) {
        this.needprotection -= protection;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Colony colony = (Colony) o;
        return this.fakeplayer.equals(colony.fakeplayer) && this.body.equals(colony.body);
    }


}
