package com.denfop.api.space.colonies;

import net.minecraft.nbt.NBTTagCompound;

public class OxygenFactory extends Building implements IOxygenFactory {

    private final int max;
    private final int generation;
    private final int people;
    private final int energy;

    public OxygenFactory(final String name, final IColony colonie) {
        super(name, colonie);
        this.max = 5000;
        this.generation = 80;
        this.people = 0;
        this.energy = 10;
        this.getColony().addNeededWorkers(this.getPeople());
        this.getColony().addConsumeEnergy(this.getEnergy());
        this.getColony().addMaxOxygen(this.getMax());
        this.getColony().getOxygenFactory().add(this);
    }

    public OxygenFactory(final String name, final IColony colonie, int people) {
        super(name, colonie);
        this.max = 5000;
        this.generation = 60;
        this.people = people;
        this.energy = 10;
        this.getColony().addNeededWorkers(this.getPeople());
        this.getColony().addConsumeEnergy(this.getEnergy());
        this.getColony().addMaxOxygen(this.getMax());
        this.getColony().getOxygenFactory().add(this);
    }

    public OxygenFactory(NBTTagCompound tag, final IColony colonie) {
        super(tag.getString("name"), colonie);
        this.max = tag.getInteger("max");
        this.generation = tag.getInteger("generation");
        this.people = tag.getInteger("people");
        this.energy = tag.getInteger("energy");
        this.getColony().addNeededWorkers(this.getPeople());
        this.getColony().addConsumeEnergy(this.getEnergy());
        this.getColony().addMaxOxygen(this.getMax());
        this.getColony().getOxygenFactory().add(this);
    }

    @Override
    public int getMax() {
        return this.max;
    }

    @Override
    public int getGeneration() {
        return this.generation;
    }

    @Override
    public int getEnergy() {
        return this.energy;
    }

    @Override
    public boolean needWorkers() {
        return this.getPeople() > 0;
    }

    @Override
    public int getPeople() {
        return this.people;
    }

    @Override
    public void work() {
        if (this.getColony().getOxygen() < this.getColony().getMaxOxygen()) {
            if (this.getColony().getEnergy() > this.getEnergy()) {
                int temp = Math.min(this.getGeneration(), this.getColony().getMaxOxygen() - this.getColony().getEnergy());
                this.getColony().addOxygen(temp);
            } else {
                if (!this.getColony().getProblems().contains(EnumProblems.ENERGY)) {
                    this.getColony().getProblems().add(EnumProblems.ENERGY);
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        super.writeTag(tag);
        tag.setInteger("max", this.max);
        tag.setInteger("people", this.people);
        tag.setInteger("energy", this.energy);
        tag.setInteger("generation", this.generation);
        tag.setString("type", "oxygenfactory");
        return tag;
    }

    @Override
    public void remove() {
        this.getColony().removeNeededWorkers(this.getPeople());
        this.getColony().removeConsumeEnergy(this.getEnergy());
        this.getColony().decreaseOxygen(this.getMax());
        this.getColony().getOxygenFactory().remove(this);
    }

}
