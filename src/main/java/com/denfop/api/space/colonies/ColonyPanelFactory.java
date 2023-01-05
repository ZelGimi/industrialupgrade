package com.denfop.api.space.colonies;

import net.minecraft.nbt.NBTTagCompound;

public class ColonyPanelFactory extends Building implements IColonyPanelFactory {

    private final EnumTypeSolarPanel type;

    public ColonyPanelFactory(final String name, final IColony colonie, EnumTypeSolarPanel type) {
        super(name, colonie);
        this.type = type;
        this.getColony().addNeededWorkers(this.type.getPeople());
        this.getColony().addMaxEnergy(5000);
    }

    public ColonyPanelFactory(final NBTTagCompound tag, final IColony colonie) {
        super(tag.getString("name"), colonie);
        this.type = EnumTypeSolarPanel.getID(tag.getInteger("id"));
        this.getColony().addNeededWorkers(this.type.getPeople());
        this.getColony().addMaxEnergy(5000);
    }

    @Override
    public int getGeneration() {
        return this.type.getGeneration();
    }

    @Override
    public int getPeople() {
        return this.type.getPeople();
    }

    @Override
    public EnumTypeSolarPanel getType() {
        return this.type;
    }

    @Override
    public void work() {
        if (this.getColony().getEnergy() < this.getColony().getMaxEnergy()) {
            int temp = Math.min(this.getGeneration(), this.getColony().getMaxEnergy() - this.getColony().getEnergy());
            this.getColony().addEnergy(temp);
        }
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        super.writeTag(tag);
        tag.setString("type", "solar");
        tag.setInteger("id", this.getType().ordinal());
        return tag;
    }

    @Override
    public void remove() {
        this.getColony().removeNeededWorkers(this.type.getPeople());
        this.getColony().decreaseEnergy(5000);
    }

}
