package com.denfop.api.space.colonies;

import net.minecraft.nbt.NBTTagCompound;

public abstract class Building implements IColonyBuilding {

    private final String name;
    private final IColony colonie;

    public Building(String name, IColony colonie) {
        this.name = name;
        this.colonie = colonie;
        this.getColony().addBuilding(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        tag.setString("name", this.name);
        return tag;
    }

    @Override
    public IColony getColony() {
        return this.colonie;
    }


}
