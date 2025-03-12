package com.denfop.api.space.colonies;

import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.IColonyBuilding;
import com.denfop.api.space.colonies.api.building.IBuildingHouse;
import com.denfop.api.space.colonies.api.building.IColonyMiningFactory;
import com.denfop.api.space.colonies.api.building.IColonyStorage;
import com.denfop.api.space.colonies.api.building.IEntertainment;
import com.denfop.api.space.colonies.api.building.IFactory;
import com.denfop.api.space.colonies.api.building.IGenerator;
import com.denfop.api.space.colonies.api.building.IOxygenFactory;
import com.denfop.api.space.colonies.api.building.IProtectionBuilding;
import com.denfop.api.space.colonies.api.building.IStorage;
import com.denfop.api.space.colonies.building.ColonyEntertainment;
import com.denfop.api.space.colonies.building.ColonyHouse;
import com.denfop.api.space.colonies.building.ColonyPanelFactory;
import com.denfop.api.space.colonies.building.Factory;
import com.denfop.api.space.colonies.building.FluidFactory;
import com.denfop.api.space.colonies.building.ItemFactory;
import com.denfop.api.space.colonies.building.OxygenFactory;
import com.denfop.api.space.colonies.building.ProtectionBuilding;
import com.denfop.api.space.colonies.building.StorageBuilding;
import com.denfop.api.space.colonies.enums.EnumHouses;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Colony implements IColony {

    private final IBody body;
    private UUID fakeplayer;
    private List<IColonyBuilding> list;
    private List<IBuildingHouse> buildingHouseList;
    private List<IFactory> factories;
    private List<IColonyMiningFactory> buildingMiningList;
    private List<IGenerator> generators;
    private List<IOxygenFactory> oxygenFactoriesList;
    private List<EnumProblems> enumProblemsList;
    private List<IStorage> storageList;
    private int energy;
    private int food;
    private int maxenergy = 0;
    private int oxygen = 0;
    private int maxoxygen = 0;
    private int freeWorkers = 0;

    private int workers = 0;
    private short needWorkers = 0;
    private byte toDelete;
    private int protection;
    private List<IProtectionBuilding> protections;
    private List<IEntertainment> entertainments;
    private List<IColonyStorage> colonyStorages;
    private short entertainment;
    private short level;
    private int experience;
    private int generationEnergy;
    private int generationFood;
    private int generationOxygen;
    private int usingEnergy;
    private int usingFood;
    private int usingOxygen;

    private byte timeUsingResetEnergy;
    private byte timeUsingResetFood;
    private byte timeUsingResetOxygen;
    private byte timeResetEnergy;
    private byte timeResetFood;
    private byte timeResetOxygen;
    private boolean auto;
    private short timeToSend = 600;
    public Colony(IBody body, UUID player) {
        this.body = body;
        this.list = new ArrayList<>();
        this.toDelete = 120;
        this.fakeplayer = player;
        this.level = 1;
        this.experience = 0;
        this.enumProblemsList = new LinkedList<>();
        this.storageList = new LinkedList<>();
        this.colonyStorages= new LinkedList<>();
        this.buildingHouseList = new LinkedList<>();
        this.buildingMiningList = new LinkedList<>();
        this.oxygenFactoriesList = new LinkedList<>();
        this.protections = new LinkedList<>();
        this.factories = new LinkedList<>();
        this.generators = new LinkedList<>();
        this.entertainments = new LinkedList<>();
        this.food = 300;
        this.energy = 300;
    }

    public Colony(CustomPacketBuffer packetBuffer) {
        this.enumProblemsList = new LinkedList<>();
        this.storageList = new LinkedList<>();
        this.buildingHouseList = new LinkedList<>();
        this.buildingMiningList = new LinkedList<>();
        this.oxygenFactoriesList = new LinkedList<>();
        this.entertainments = new LinkedList<>();
        this.factories = new LinkedList<>();
        this.colonyStorages= new LinkedList<>();
        this.generators = new LinkedList<>();
        this.protections = new LinkedList<>();
        this.body = SpaceNet.instance.getBodyFromName(packetBuffer.readString());
        this.level = packetBuffer.readShort();
        this.experience = packetBuffer.readInt();
        this.fakeplayer = packetBuffer.readUniqueId();
        this.energy = packetBuffer.readInt();
        this.oxygen = packetBuffer.readInt();
        this.food = packetBuffer.readInt();
        this.needWorkers = packetBuffer.readShort();
        this.workers = packetBuffer.readInt();
        this.toDelete = packetBuffer.readByte();
        int size = packetBuffer.readInt();
        generationOxygen = packetBuffer.readInt();
        generationEnergy = packetBuffer.readInt();
        generationFood = packetBuffer.readInt();
        usingOxygen = packetBuffer.readInt();
        usingEnergy = packetBuffer.readInt();
        usingFood = packetBuffer.readInt();
        auto = packetBuffer.readBoolean();
        int size1 = packetBuffer.readByte();
        this.enumProblemsList = new ArrayList<>();
        for (int i = 0; i < size1;i++){
            this.enumProblemsList.add(EnumProblems.values()[packetBuffer.readByte()]);
        }
        list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            byte type = packetBuffer.readByte();
            switch (type) {
                case 5:
                    new OxygenFactory(packetBuffer, this);
                    break;
                case 7:
                    new StorageBuilding(packetBuffer, this);
                    break;
                case 0:
                    new ColonyHouse(packetBuffer, this);
                    break;
                case 1:
                    new ColonyPanelFactory(packetBuffer, this);
                    break;
                case 2:
                    new Factory(packetBuffer, this);
                    break;
                case 6:
                    new ProtectionBuilding(packetBuffer, this);
                    break;
                case 3:
                    new FluidFactory(packetBuffer, this);
                    break;
                case 4:
                    new ItemFactory(packetBuffer, this);
                    break;
                case 8:
                    new ColonyEntertainment(packetBuffer, this);
                    break;
            }
        }
    }

    public Colony(NBTTagCompound tag, UUID fakeplayer) {
        this.body = SpaceNet.instance.getBodyFromName(tag.getString("name"));
        this.fakeplayer = fakeplayer;
        this.enumProblemsList = new LinkedList<>();
        this.storageList = new LinkedList<>();
        this.buildingHouseList = new LinkedList<>();
        this.buildingMiningList = new LinkedList<>();
        this.oxygenFactoriesList = new LinkedList<>();
        this.factories = new LinkedList<>();
        this.generators = new LinkedList<>();
        this.entertainments = new LinkedList<>();
        this.protections = new LinkedList<>();
        this.colonyStorages= new LinkedList<>();
        NBTTagList list1 = tag.getTagList("building", 10);
        list = new LinkedList<>();
        for (int i = 0; i < list1.tagCount(); i++) {
            NBTTagCompound nbt = list1.getCompoundTagAt(i);
            byte type = nbt.getByte("type");
            switch (type) {
                case 5:
                    new OxygenFactory(nbt, this);
                    break;
                case 7:
                    new StorageBuilding(nbt, this);
                    break;
                case 0:
                    new ColonyHouse(nbt, this);
                    break;
                case 1:
                    new ColonyPanelFactory(nbt, this);
                    break;
                case 2:
                    new Factory(nbt, this);
                    break;
                case 6:
                    new ProtectionBuilding(nbt, this);
                    break;
                case 3:
                    new FluidFactory(nbt, this);
                    break;
                case 4:
                    new ItemFactory(nbt, this);
                    break;
                case 8:
                    new ColonyEntertainment(nbt, this);
                    break;
            }
        }
        this.list = new ArrayList<>(list);
        this.energy = tag.getInteger("energy");
        this.oxygen = tag.getInteger("oxygen");
        this.food = tag.getInteger("food");
        this.needWorkers = tag.getShort("needWorkers");
        this.workers = tag.getInteger("workers");
        this.level = tag.getShort("level");
        this.experience = tag.getInteger("experience");
        this.toDelete = tag.getByte("toDelete");
        this.auto = tag.getBoolean("auto");
        this.timeToSend = tag.getShort("timeToSend");

    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeString(body.getName());
        customPacketBuffer.writeShort(this.level);
        customPacketBuffer.writeInt(this.experience);
        customPacketBuffer.writeUniqueId(fakeplayer);
        customPacketBuffer.writeInt(energy);
        customPacketBuffer.writeInt(oxygen);
        customPacketBuffer.writeInt(food);
        customPacketBuffer.writeShort(needWorkers);
        customPacketBuffer.writeInt(workers);
        customPacketBuffer.writeByte(toDelete);
        customPacketBuffer.writeInt(list.size());
        customPacketBuffer.writeInt(generationOxygen);
        customPacketBuffer.writeInt(generationEnergy);
        customPacketBuffer.writeInt(generationFood);
        customPacketBuffer.writeInt(usingOxygen);
        customPacketBuffer.writeInt(usingEnergy);
        customPacketBuffer.writeInt(usingFood);
        customPacketBuffer.writeBoolean(auto);
        customPacketBuffer.writeByte(this.enumProblemsList.size());
        for (EnumProblems problems : enumProblemsList)
            customPacketBuffer.writeByte((byte)problems.ordinal());
        for (IColonyBuilding building : list) {
            building.writePacket(customPacketBuffer);
        }
        return customPacketBuffer;
    }
    public List<ItemStack> getStacksFromStorage(){
        List<ItemStack> itemStackList = new LinkedList<>();
        for (IStorage storage : storageList){
            itemStackList.addAll(storage.getStacks());
        }
        return  new ArrayList<>(itemStackList);
    }
    public List<FluidStack> getFluidsFromStorage(){
        List<FluidStack> itemStackList = new LinkedList<>();
        for (IStorage storage : storageList){
            itemStackList.addAll(storage.getFluidStacks());
        }
        return  new ArrayList<>(itemStackList);
    }
    @Override
    public boolean isAuto() {
        return this.auto;
    }

    @Override
    public void setAuto(final boolean auto) {
        this.auto = auto;
    }

    @Override
    public int getProtection() {
        return this.protection;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getExperience() {
        return experience;
    }

    @Override
    public int getAvailableBuilding() {
        return getMaxBuilding() - list.size();
    }

    @Override
    public int getMaxBuilding() {
        return (int) (8 + (this.level-1) * 2.5);
    }

    @Override
    public int getMaxExperience() {
        return 900 + 400 * (level - 1);
    }
    byte tick = 0;

    @Override
    public boolean matched(final IBody body) {
        return this.body == body;
    }

    @Override
    public IBody getBody() {
        return this.body;
    }

    @Override
    public UUID getFakePlayer() {
        return this.fakeplayer;
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
    public int getTimeToDelete() {
        return this.toDelete;
    }

    @Override
    public int getMaxOxygen() {
        return this.maxoxygen;
    }


    @Override
    public void update() {
        if (!this.enumProblemsList.isEmpty()) {
            if (toDelete > 0) {
                toDelete -= 1;
            }

            enumProblemsList.clear();
        } else {
            if (toDelete < 120) {
                toDelete = 120;
                this.tick = 0;
            }
            if (this.level < 200 && this.tick == 10){
                this.tick = 0;
                this.experience+=this.workers;
                if (this.experience >= this.getMaxExperience()){
                    this.level++;
                    this.experience = 0;
                }
            }
        }
        tick++;
        if (this.tick > 10){
            this.tick = 0;
        }
        if (this.auto){
            this.timeToSend--;
            if (this.timeToSend == 0){
                timeToSend = 600;
                SpaceNet.instance.getColonieNet().sendResourceToPlanet(getFakePlayer(),body);
            }
        }else{
            this.timeToSend = 600;
        }
        if (needWorkers > 0) {
            if (freeWorkers > 0) {
                for (IBuildingHouse buildingHouse : buildingHouseList) {
                    if (buildingHouse.getFreeWorkers() > 0) {
                        int free = buildingHouse.getFreeWorkers();

                        for (IFactory factory : factories) {
                            int needWorkers = factory.needWorkers();
                            if (needWorkers <= 0) {
                                continue;
                            }
                            final int addWorkers = Math.min(needWorkers, free);
                            factory.addWorkers(addWorkers);
                            this.needWorkers -= (short) addWorkers;
                            this.workers += addWorkers;
                            this.freeWorkers -= addWorkers;
                            buildingHouse.removeFreeWorkers(addWorkers);
                            free -= addWorkers;
                            if (!(this.needWorkers > 0 && free > 0)) {
                                break;
                            }
                        }
                        if ((needWorkers > 0 && free > 0)) {
                            for (IColonyMiningFactory factory : buildingMiningList) {
                                int needWorkers = factory.needWorkers();
                                if (needWorkers <= 0) {
                                    continue;
                                }
                                final int addWorkers = Math.min(needWorkers, free);
                                factory.addWorkers(addWorkers);
                                this.workers += addWorkers;
                                this.needWorkers -= addWorkers;
                                this.freeWorkers -= addWorkers;
                                buildingHouse.removeFreeWorkers(addWorkers);
                                free -= addWorkers;
                                if (!(this.needWorkers > 0 && free > 0)) {
                                    break;
                                }
                            }
                        }
                        if ((needWorkers > 0 && free > 0)) {
                            for (IOxygenFactory factory : oxygenFactoriesList) {
                                int needWorkers = factory.needWorkers();
                                if (needWorkers <= 0) {
                                    continue;
                                }
                                final int addWorkers = Math.min(needWorkers, free);
                                factory.addWorkers(addWorkers);
                                this.workers += addWorkers;
                                this.needWorkers -= addWorkers;
                                this.freeWorkers -= addWorkers;
                                buildingHouse.removeFreeWorkers(addWorkers);
                                free -= addWorkers;
                                if (!(this.needWorkers > 0 && free > 0)) {
                                    break;
                                }
                            }
                        }
                        if ((needWorkers > 0 && free > 0)) {
                            for (IProtectionBuilding factory : protections) {
                                int needWorkers = factory.needWorkers();
                                if (needWorkers <= 0) {
                                    continue;
                                }
                                final int addWorkers = Math.min(needWorkers, free);
                                factory.addWorkers(addWorkers);
                                this.workers += addWorkers;
                                this.needWorkers -= addWorkers;
                                this.freeWorkers -= addWorkers;
                                buildingHouse.removeFreeWorkers(addWorkers);
                                free -= addWorkers;
                                if (!(this.needWorkers > 0 && free > 0)) {
                                    break;
                                }
                            }
                        }
                        if ((needWorkers > 0 && free > 0)) {
                            for (IGenerator factory : generators) {
                                int needWorkers = factory.needWorkers();
                                if (needWorkers <= 0) {
                                    continue;
                                }
                                final int addWorkers = Math.min(needWorkers, free);
                                factory.addWorkers(addWorkers);
                                this.workers += addWorkers;
                                this.needWorkers -= addWorkers;
                                this.freeWorkers -= addWorkers;
                                buildingHouse.removeFreeWorkers(addWorkers);
                                free -= addWorkers;
                                if (!(this.needWorkers > 0 && free > 0)) {
                                    break;
                                }
                            }
                        }
                        if ((needWorkers > 0 && free > 0)) {
                            for (IEntertainment factory : entertainments) {
                                int needWorkers = factory.needWorkers();
                                if (needWorkers <= 0) {
                                    continue;
                                }
                                final int addWorkers = Math.min(needWorkers, free);
                                factory.addWorkers(addWorkers);
                                this.workers += addWorkers;
                                this.needWorkers -= addWorkers;
                                this.freeWorkers -= addWorkers;
                                buildingHouse.removeFreeWorkers(addWorkers);
                                free -= addWorkers;
                                if (!(this.needWorkers > 0 && free > 0)) {
                                    break;
                                }
                            }
                        }
                        if ((needWorkers > 0 && free > 0)) {
                            for (IColonyStorage factory : colonyStorages) {
                                int needWorkers = factory.needWorkers();
                                if (needWorkers <= 0) {
                                    continue;
                                }
                                final int addWorkers = Math.min(needWorkers, free);
                                factory.addWorkers(addWorkers);
                                this.workers += addWorkers;
                                this.needWorkers -= addWorkers;
                                this.freeWorkers -= addWorkers;
                                buildingHouse.removeFreeWorkers(addWorkers);
                                free -= addWorkers;
                                if (!(this.needWorkers > 0 && free > 0)) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (this.needWorkers > 0) {
                this.enumProblemsList.add(EnumProblems.WORKERS);
            }
            return;
        }
        entertainment = 0;
        protection = 0;
        for (IProtectionBuilding protectionBuilding : this.protections) {
            protectionBuilding.work();
        }
        for (IEntertainment entertainment : this.entertainments) {
            entertainment.work();
        }
        for (IColonyBuilding building : this.list) {
            if (!building.isIgnore()) {
                building.work();
            }
        }
        if (this.list.size() * 2 > protection){
            this.enumProblemsList.add(EnumProblems.PROTECTION);
        }

    }

    public List<IEntertainment> getEntertainments() {
        return entertainments;
    }
    public double getPercentEntertainment(){
        if (level < 7)
            return 1;
        if (this.entertainment == 0)
            return 0.8;
        return  Math.min (1.5,Math.max (0.8,(this.workers + this.freeWorkers) * 1D / this.entertainment));
    }
    @Override
    public void useEnergy(final int energy) {
        if (this.timeUsingResetEnergy != this.tick){
            this.timeUsingResetEnergy = this.tick;
            this.usingEnergy = 0;
        }
        this.usingEnergy += energy;
        this.energy -= energy;
    }

    public List<IColonyBuilding> getColonyBuilding() {
        return list;
    }


    @Override
    public void useOxygen(final int oxygen) {
        if (this.timeUsingResetOxygen != this.tick){
            this.timeUsingResetOxygen = this.tick;
            this.usingOxygen = 0;
        }
        this.usingOxygen += oxygen;
        this.oxygen -= (short) oxygen;
    }


    @Override
    public void addOxygen(final int oxygen) {
        if (this.timeResetOxygen != this.tick){
            this.timeResetOxygen = this.tick;
            this.generationOxygen = 0;
        }
        this.generationOxygen += oxygen;
        this.oxygen += oxygen;
        if (this.oxygen >= this.maxoxygen) {
            this.oxygen = this.maxoxygen;
        }
    }


    public List<IBuildingHouse> getBuildingHouseList() {
        return buildingHouseList;
    }

    public List<IColonyMiningFactory> getBuildingMiningList() {
        return buildingMiningList;
    }

    public byte getToDelete() {
        return toDelete;
    }

    public int getWorkers() {
        return workers;
    }

    public int getFreeWorkers() {
        return freeWorkers;
    }

    public List<IFactory> getFactories() {
        return factories;
    }

    public List<IGenerator> getGenerators() {
        return generators;
    }

    public short getNeedWorkers() {
        return needWorkers;
    }

    public int getMaxenergy() {
        return maxenergy;
    }

    public int getMaxoxygen() {
        return maxoxygen;
    }

    public List<EnumProblems> getEnumProblemsList() {
        return enumProblemsList;
    }

    public List<IColonyBuilding> getList() {
        return list;
    }

    public List<IOxygenFactory> getOxygenFactoriesList() {
        return oxygenFactoriesList;
    }

    public List<IProtectionBuilding> getProtections() {
        return protections;
    }

    public int getGenerationEnergy() {
        return generationEnergy;
    }

    public int getGenerationFood() {
        return generationFood;
    }

    public int getGenerationOxygen() {
        return generationOxygen;
    }

    @Override
    public void addEnergy(final int energy) {
        if (this.timeResetEnergy != this.tick){
            this.timeResetEnergy = this.tick;
            this.generationEnergy = 0;
        }
        this.generationEnergy += energy;
        this.energy += energy;
        if (this.energy >= this.maxenergy) {
            this.energy = this.maxenergy;
        }
    }

    @Override
    public void addMaxOxygen(final int oxygen) {
        this.maxoxygen += oxygen;
    }

    @Override
    public void addMaxEnergy(final int energy) {
        this.maxenergy += energy;
    }


    @Override
    public NBTTagCompound writeNBT(final NBTTagCompound tag) {
        tag.setString("name", this.body.getName());
        tag.setInteger("workers", (short) this.workers);
        tag.setShort("needWorkers", this.needWorkers);
        tag.setInteger("energy", this.energy);
        tag.setInteger("food", this.food);
        tag.setInteger("oxygen", this.oxygen);
        tag.setByte("toDelete", toDelete);
        tag.setShort("level", level);
        tag.setBoolean("auto", auto);
        tag.setShort("timeToSend", timeToSend);
        tag.setInteger("experience", experience);
        NBTTagList tagList = new NBTTagList();
        for (IColonyBuilding iColonyBuilding : this.list) {
            tagList.appendTag(iColonyBuilding.writeTag(new NBTTagCompound()));
        }
        tag.setTag("building", tagList);
        return tag;
    }

    @Override
    public List<IColonyBuilding> getBuildingList() {
        return this.list;
    }

    @Override
    public void addBuilding(final IColonyBuilding building) {
        this.list.add(building);
        switch (building.getTypeBuilding()) {
            case HOUSES:
                this.freeWorkers += ((IBuildingHouse) building).getFreeWorkers();
                buildingHouseList.add((IBuildingHouse) building);
                break;
            case FABRIC:
                if (building instanceof IFactory) {
                    this.needWorkers += ((IFactory) building).needWorkers();
                    this.factories.add((IFactory) building);
                } else if (building instanceof IColonyMiningFactory) {
                    this.needWorkers += ((IColonyMiningFactory) building).needWorkers();
                    this.buildingMiningList.add((IColonyMiningFactory) building);
                }
                break;
            case OXYGEN:
                this.needWorkers += ((IOxygenFactory) building).needWorkers();
                this.oxygenFactoriesList.add((IOxygenFactory) building);
                break;
            case GENERATORS:
                this.needWorkers += ((IGenerator) building).needWorkers();
                this.generators.add((IGenerator) building);
                break;
            case PROTECTION:
                this.needWorkers += ((IProtectionBuilding) building).needWorkers();
                this.protections.add((IProtectionBuilding) building);
                break;
            case ENTERTAINMENT:
                this.needWorkers += ((IEntertainment) building).needWorkers();
                this.entertainments.add((IEntertainment) building);
                break;
            case STORAGE:
                this.needWorkers += ((IColonyStorage) building).needWorkers();
                this.colonyStorages.add((IColonyStorage) building);
                break;
        }
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
    public int getNeededWorkers() {
        return needWorkers;
    }

    @Override
    public int getMaxWorkers() {
        return workers;
    }

    @Override
    public void removeWorkers(int i) {
        for (IFactory factory : factories) {
            int needWorkers = factory.getWorkers();
            if (needWorkers <= 0) {
                continue;
            }
            final int addWorkers = Math.min(needWorkers, i);
            factory.removeWorkers(addWorkers);
            this.needWorkers += addWorkers;
            this.workers -= addWorkers;
            i -= addWorkers;
            if ((i == 0)) {
                break;
            }
        }
        if ((i > 0)) {
            for (IColonyMiningFactory factory : buildingMiningList) {
                int needWorkers = factory.getWorkers();
                if (needWorkers <= 0) {
                    continue;
                }
                final int addWorkers = Math.min(needWorkers, i);
                factory.removeWorkers(addWorkers);
                this.needWorkers += addWorkers;
                this.workers -= addWorkers;
                i -= addWorkers;
                if ((i == 0)) {
                    break;
                }
            }
        }
        if ((i > 0)) {
            for (IOxygenFactory factory : oxygenFactoriesList) {
                int needWorkers = factory.getWorkers();
                if (needWorkers <= 0) {
                    continue;
                }
                final int addWorkers = Math.min(needWorkers, i);
                factory.removeWorkers(addWorkers);
                this.needWorkers += addWorkers;
                this.workers -= addWorkers;
                i -= addWorkers;
                if ((i == 0)) {
                    break;
                }
            }
        }
        if ((i > 0)) {
            for (IGenerator factory : generators) {
                int needWorkers = factory.getWorkers();
                if (needWorkers <= 0) {
                    continue;
                }
                final int addWorkers = Math.min(needWorkers, i);
                factory.removeWorkers(addWorkers);
                this.needWorkers += addWorkers;
                this.workers -= addWorkers;
                i -= addWorkers;
                if ((i == 0)) {
                    break;
                }
            }
        }
        if ((i > 0)) {
            for (IProtectionBuilding factory : protections) {
                int needWorkers = factory.getWorkers();
                if (needWorkers <= 0) {
                    continue;
                }
                final int addWorkers = Math.min(needWorkers, i);
                factory.removeWorkers(addWorkers);
                this.needWorkers += addWorkers;
                this.workers -= addWorkers;
                i -= addWorkers;
                if ((i == 0)) {
                    break;
                }
            }
        }
        if ((i > 0)) {
            for (IColonyStorage factory : colonyStorages) {
                int needWorkers = factory.getWorkers();
                if (needWorkers <= 0) {
                    continue;
                }
                final int addWorkers = Math.min(needWorkers, i);
                factory.removeWorkers(addWorkers);
                this.needWorkers += addWorkers;
                this.workers -= addWorkers;
                i -= addWorkers;
                if ((i == 0)) {
                    break;
                }
            }
        }
        if ((i > 0)) {
            for (IEntertainment factory : entertainments) {
                int needWorkers = factory.getWorkers();
                if (needWorkers <= 0) {
                    continue;
                }
                final int addWorkers = Math.min(needWorkers, i);
                factory.removeWorkers(addWorkers);
                this.needWorkers += addWorkers;
                this.workers -= addWorkers;
                i -= addWorkers;
                if ((i == 0)) {
                    break;
                }
            }
        }
    }

    public int getUsingEnergy() {
        return usingEnergy;
    }

    public int getUsingFood() {
        return usingFood;
    }

    public int getUsingOxygen() {
        return usingOxygen;
    }

    public List<IColonyStorage> getColonyStorages() {
        return colonyStorages;
    }

    @Override
    public int getFood() {
        return food;
    }

    public void useFood(int food) {
        if (this.timeUsingResetFood != this.tick){
            this.timeUsingResetFood = this.tick;
            this.usingFood = 0;
        }
        this.usingFood += food;
        this.food -= food;
    }

    public void addFood(int food) {
        if (this.timeResetFood != this.tick){
            this.timeResetFood = this.tick;
            this.generationFood = 0;
        }
        this.generationFood += food;
        this.food += food;
        if (this.food >= (this.workers + this.freeWorkers) * 15) {
            this.food = (this.workers + this.freeWorkers) * 15;
        }
    }

    @Override
    public void addProtection(final int protection) {
        this.protection += protection;
    }

    @Override
    public boolean consumeEnergy(final int energy) {
        if (this.energy < energy) {
            this.energy = 0;
            return false;
        } else {
            this.energy -= energy;
            return true;
        }
    }

    @Override
    public void addEntertainment(final short entertainment) {
        this.entertainment = entertainment;
    }

    @Override
    public short getEntertainment() {
        return entertainment;
    }

    @Override
    public boolean canUseFood(final EnumHouses houses) {
        int prev = this.generationFood - this.usingFood;
        return (prev - 1)>= 0;
    }

    @Override
    public boolean canUseOxygen(final EnumHouses houses) {
        int prev = this.generationOxygen - this.usingOxygen;
        return (prev - houses.getConsumeOxygen()) >= 0;
    }

    @Override
    public void addFreeWorkers(final int prev) {
        this.freeWorkers += prev;
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
