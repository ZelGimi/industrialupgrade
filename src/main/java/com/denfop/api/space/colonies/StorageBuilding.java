package com.denfop.api.space.colonies;

import net.minecraft.nbt.NBTTagCompound;

public class StorageBuilding extends Building implements IColonyStorage {

    boolean work;
    IStorage storage;
    int energy;
    int peoples;

    public StorageBuilding(final String name, final IColony colonie) {
        super(name, colonie);
        this.storage = new Storage(this);
        this.energy = 10;
        this.peoples = 5;
        this.getColony().addStorage(this.storage);
        this.getColony().addNeededWorkers(this.peoples);
        this.getColony().addConsumeEnergy(this.energy);
        this.work = true;
    }

    public StorageBuilding(final NBTTagCompound tag, final IColony colonie) {
        super(tag.getString("name"), colonie);
        this.peoples = tag.getInteger("people");
        this.energy = tag.getInteger("energy");
        this.work = tag.getBoolean("work");
        this.storage = new Storage(tag.getCompoundTag("storage"), this);
        this.getColony().addStorage(this.storage);
        this.getColony().addNeededWorkers(this.peoples);
        this.getColony().addConsumeEnergy(this.energy);
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        super.writeTag(tag);
        tag.setInteger("people", this.peoples);
        tag.setInteger("energy", this.energy);
        tag.setBoolean("work", this.work);
        tag.setString("type", "storage");
        tag.setTag("storage", this.getStorage().writeNBT(new NBTTagCompound()));
        return tag;
    }

    @Override
    public IStorage getStorage() {
        return this.storage;
    }

    @Override
    public int getEnergy() {
        return this.energy;
    }

    @Override
    public int getPeoples() {
        return this.peoples;
    }

    @Override
    public boolean getWork() {
        return this.work;
    }

    @Override
    public void setWork(final boolean setWork) {
        this.work = setWork;
    }

    @Override
    public void work() {
        if (this.getColony().getEnergy() < this.getEnergy()) {
            this.getColony().useEnergy(this.getEnergy());
            this.getColony().getProblems().remove(EnumProblems.ENERGY);
            if (!this.getWork()) {
                this.setWork(true);
            }
        } else {
            if (!this.getColony().getProblems().contains(EnumProblems.ENERGY)) {
                this.getColony().getProblems().add(EnumProblems.ENERGY);
            }
            if (this.getWork()) {
                this.setWork(false);
            }
        }
    }

    @Override
    public void remove() {
        this.getColony().removeStorage(this.storage);
        this.getColony().removeNeededWorkers(this.peoples);
        this.getColony().removeConsumeEnergy(10);
    }

}
