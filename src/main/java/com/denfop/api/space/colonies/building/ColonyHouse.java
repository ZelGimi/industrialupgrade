package com.denfop.api.space.colonies.building;

import com.denfop.api.space.colonies.Building;
import com.denfop.api.space.colonies.Colony;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.building.IBuildingHouse;
import com.denfop.api.space.colonies.enums.EnumHouses;
import com.denfop.api.space.colonies.enums.EnumHousesLevel;
import com.denfop.api.space.colonies.enums.EnumProblems;
import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.world.WorldBaseGen;
import net.minecraft.nbt.CompoundTag;

public class ColonyHouse extends Building implements IBuildingHouse {

    private final EnumHouses houses;
    private byte freeWorkers;
    private byte workers;
    private byte timeToDecrease;
    private byte timeToIncrease;
    private byte peoples;

    public ColonyHouse(EnumHouses houses, IColony colonie, boolean simulate) {
        super(colonie);
        this.houses = houses;
        this.peoples = (byte) ((byte) houses.getMax() / 2 + WorldBaseGen.random.nextInt(houses.getMax() / 2 + 1));
        this.timeToDecrease = 3;
        this.timeToIncrease = 20;
        this.freeWorkers = peoples;
        this.workers = 0;
        if (!simulate)
            this.getColony().addBuilding(this);
    }

    public ColonyHouse(final CompoundTag tag, IColony colonie) {
        super(colonie);
        int id = tag.getByte("id");
        this.houses = EnumHouses.getID(id);
        this.peoples = (byte) (tag.getByte("people"));
        this.freeWorkers = (byte) (tag.getByte("freeWorkers"));
        this.workers = (byte) (tag.getByte("workers"));
        this.timeToDecrease = (byte) (tag.getByte("timeToDecrease"));
        this.timeToIncrease = (byte) (tag.getByte("timeToIncrease"));
        this.getColony().addBuilding(this);


    }

    public ColonyHouse(CustomPacketBuffer packetBuffer, Colony colonie) {
        super(colonie);
        int id = packetBuffer.readByte();
        this.houses = EnumHouses.getID(id);
        this.peoples = packetBuffer.readByte();
        this.freeWorkers = packetBuffer.readByte();
        this.workers = packetBuffer.readByte();
        this.timeToDecrease = packetBuffer.readByte();
        this.timeToIncrease = packetBuffer.readByte();
        this.getColony().addBuilding(this);
    }

    public EnumHouses getHouses() {
        return houses;
    }

    @Override
    public EnumHousesLevel getLevel() {
        return this.houses.getLevel();
    }

    @Override
    public int getPeople() {
        return this.peoples;
    }

    @Override
    public int getEnergy() {
        return this.houses.getEnergy();
    }

    @Override
    public int getMaxPeople() {
        return this.houses.getMax();
    }

    @Override
    public void addPeople(int peoples) {
        if (peoples > 0) {
            int prev = this.peoples;
            this.peoples += (byte) peoples;
            if (this.peoples > this.getMaxPeople())
                this.peoples = (byte) this.getMaxPeople();
            prev = this.peoples - prev;
            this.freeWorkers += (byte) (prev);
            this.getColony().addFreeWorkers(prev);
        } else {
            peoples = Math.abs(peoples);
            this.peoples -= (byte) peoples;
            while (peoples > 0) {
                int number = WorldBaseGen.random.nextInt(this.freeWorkers + this.workers);
                if (number < this.freeWorkers) {
                    this.freeWorkers -= 1;
                    this.getColony().addFreeWorkers(-1);
                } else {
                    this.workers -= 1;
                }
                peoples -= 1;
            }
        }
    }

    @Override
    public int getWorkers() {
        return workers;
    }

    @Override
    public int getFreeWorkers() {
        return freeWorkers;
    }

    @Override
    public void removeFreeWorkers(final int workers) {
        this.freeWorkers -= (byte) workers;
        this.workers += (byte) workers;
    }

    @Override
    public CustomPacketBuffer writePacket(final CustomPacketBuffer customPacketBuffer) {
        super.writePacket(customPacketBuffer);
        customPacketBuffer.writeByte(this.houses.ordinal());
        customPacketBuffer.writeByte(this.peoples);
        customPacketBuffer.writeByte(this.freeWorkers);
        customPacketBuffer.writeByte(this.workers);
        customPacketBuffer.writeByte(this.timeToDecrease);
        customPacketBuffer.writeByte(this.timeToIncrease);
        return customPacketBuffer;
    }

    @Override
    public CompoundTag writeTag(final CompoundTag tag) {
        super.writeTag(tag);
        tag.putByte("id", (byte) this.houses.ordinal());
        tag.putByte("people", (byte) (this.peoples));
        tag.putByte("workers", (byte) (this.workers));
        tag.putByte("freeWorkers", (byte) (this.freeWorkers));
        tag.putByte("timeToDecrease", (byte) (this.timeToDecrease));
        tag.putByte("timeToDecrease", (byte) (this.timeToIncrease));
        return tag;
    }

    @Override
    public int getMinLevelColony() {
        return this.houses.getLevel().getLevel();
    }

    @Override
    public void work() {

        boolean problem = false;
        if (this.getColony().getEnergy() >= this.getEnergy()) {
            this.getColony().useEnergy(this.getEnergy());
        } else {
            if (!this.getColony().getProblems().contains(EnumProblems.ENERGY)) {
                this.getColony().getProblems().add(EnumProblems.ENERGY);
            }
            problem = true;

        }
        if (this.houses.getConsumeOxygen() * this.peoples > this.getColony().getOxygen()) {
            if (!this.getColony().getProblems().contains(EnumProblems.OXYGEN)) {
                this.getColony().getProblems().add(EnumProblems.OXYGEN);
            }
            this.getColony().useOxygen(this.getColony().getOxygen());
            problem = true;
        } else {
            this.getColony().useOxygen((int) (this.houses.getConsumeOxygen() * this.peoples));
        }
        if (this.peoples > this.getColony().getFood()) {
            if (!this.getColony().getProblems().contains(EnumProblems.FOOD)) {
                this.getColony().getProblems().add(EnumProblems.FOOD);
            }
            this.getColony().useFood(this.getColony().getFood());
            problem = true;
        } else {
            this.getColony().useFood(this.peoples);
        }
        if (!problem) {
            this.timeToDecrease = 3;
            if (this.getPeople() < this.getMaxPeople() && this.getColony().canUseFood(this.houses) && this.getColony().canUseOxygen(this.houses)) {
                if (timeToIncrease == 0) {
                    this.addPeople(1);
                    this.timeToIncrease = 20;
                } else {
                    timeToIncrease--;
                }
            }
        } else {
            this.timeToDecrease--;
            timeToIncrease = 20;
            if (this.timeToDecrease == 0 && this.getPeople() > 0) {
                int temp = this.getWorkers();
                this.addPeople(-1);
                int temp1 = this.getWorkers();
                this.getColony().removeWorkers(temp - temp1);
                this.timeToDecrease = 3;
            }
        }
    }

    @Override
    public EnumTypeBuilding getTypeBuilding() {
        return EnumTypeBuilding.HOUSES;
    }

    @Override
    public byte getId() {
        return 0;
    }

}
