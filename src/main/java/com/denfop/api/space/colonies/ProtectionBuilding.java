package com.denfop.api.space.colonies;

import net.minecraft.nbt.NBTTagCompound;

public class ProtectionBuilding extends Building implements IProtectionBuilding {

    private final int protection;

    public ProtectionBuilding(final String name, final IColony colonie) {
        super(name, colonie);
        this.protection = 50;
        this.getColony().addProtection(this.getProtection());
    }

    public ProtectionBuilding(final NBTTagCompound tag, final IColony colonie) {
        super(tag.getString("name"), colonie);
        this.protection = 50;
        this.getColony().addProtection(this.getProtection());
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        super.writeTag(tag);
        tag.setString("type", "protection");
        return tag;
    }

    @Override
    public void work() {

    }

    @Override
    public void remove() {

    }

    @Override
    public int getProtection() {
        return this.protection;
    }

}
