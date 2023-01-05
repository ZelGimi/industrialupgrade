package com.denfop.api.space.colonies;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

public class ColonyHouse extends Building implements IBuildingHouse {

    private final EnumHouses houses;
    private int timeToDecrease;
    private int peoples;

    public ColonyHouse(final String name, EnumHouses houses, IColony colonie) {
        super(name, colonie);
        this.houses = houses;
        Random rand = new Random();
        this.peoples = rand.nextInt(houses.getMax() + 1);
        this.timeToDecrease = 60;
        this.getColony().addWorkers(this.getWorkers());
        this.getColony().addConsumeEnergy(this.getEnergy());
    }

    public ColonyHouse(final NBTTagCompound tag, IColony colonie) {
        super(tag.getString("name"), colonie);
        int id = tag.getInteger("id");
        this.houses = EnumHouses.getID(id);
        this.peoples = tag.getInteger("people");
        this.timeToDecrease = tag.getInteger("timeToDecrease");
        this.getColony().addWorkers(this.getWorkers());
        this.getColony().addConsumeEnergy(this.getEnergy());

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
    public void setPeoples(int peoples) {
        this.peoples = peoples;
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
        assert this.peoples + peoples <= this.getMaxPeople();
        this.peoples += peoples;
    }

    @Override
    public int getWorkers() {
        return (int) (this.peoples * 0.7);
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        super.writeTag(tag);
        tag.setString("type", "house");
        tag.setInteger("id", this.houses.ordinal());
        tag.setInteger("people", this.peoples);
        tag.setInteger("timeToDecrease", this.timeToDecrease);
        return tag;
    }

    @Override
    public void work() {

        boolean problem = false;
        int max = this.getColony().getMaxWorkers();
        int need = this.getColony().getNeededWorkers();
        if (this.getColony().getEnergy() >= this.getEnergy()) {
            this.getColony().useEnergy(this.getEnergy());
            this.getColony().getProblems().remove(EnumProblems.ENERGY);
            this.timeToDecrease = 60;
        } else {
            if (!this.getColony().getProblems().contains(EnumProblems.ENERGY)) {
                this.getColony().getProblems().add(EnumProblems.ENERGY);
            }
            problem = true;
            this.timeToDecrease--;
            if (this.timeToDecrease == 0 && this.getPeople() > 0) {
                int temp = this.getWorkers();
                this.addPeople(-1);
                int temp1 = this.getWorkers();
                this.getColony().removeWorkers(temp - temp1);
                this.timeToDecrease = 60;
            }
        }
        if (this.houses.getConsumeOxygen() < this.getColony().getOxygen()) {
            if (!this.getColony().getProblems().contains(EnumProblems.OXYGEN)) {
                this.getColony().getProblems().add(EnumProblems.OXYGEN);
            }
            if (!problem) {
                this.timeToDecrease--;
                if (this.timeToDecrease == 0 && this.getPeople() > 0) {
                    int temp = this.getWorkers();
                    this.addPeople(-1);
                    int temp1 = this.getWorkers();
                    this.getColony().removeWorkers(temp - temp1);
                    this.timeToDecrease = 60;
                }
                problem = true;
            }
        } else {
            this.getColony().useOxygen(this.houses.getConsumeOxygen());
        }
        if (!problem) {
            if (need * 1.5 > max) {
                if (this.getPeople() < this.getMaxPeople()) {
                    int temp = this.getWorkers();
                    this.addPeople(1);
                    int temp1 = this.getWorkers();
                    this.getColony().addWorkers(temp1 - temp);
                    this.addPeople(1);
                }
            }
        }
    }

    @Override
    public void remove() {
        this.getColony().removeWorkers(this.getWorkers());
        this.getColony().removeConsumeEnergy(this.getEnergy());
    }

}
