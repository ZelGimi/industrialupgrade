package com.denfop.api.space.colonies;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FluidFactory extends Building implements IColonyFluidFactory {

    private final EnumFluidFactory type;

    public FluidFactory(final String name, final IColony colonie, EnumFluidFactory type) {
        super(name, colonie);
        this.type = type;
        this.getColony().addConsumeEnergy(this.getEnergy());
        this.getColony().addNeededWorkers(this.getNeedPeople());
        this.getColony().addNeedProtection(this.getNeedProtection());
    }

    public FluidFactory(final NBTTagCompound tag, final IColony colonie) {
        super(tag.getString("name"), colonie);
        this.type = EnumFluidFactory.getID(tag.getInteger("id"));
    }

    @Override
    public void work() {
        List<IStorage> storageList = this.getColony().getStorageList();
        if (storageList.isEmpty() || this.getColony().getEnergy() < this.getEnergy()) {
            return;
        }
        for (IStorage storage : storageList) {
            if (storage.canAddFluidStack(this.getStack())) {
                this.getColony().useEnergy(this.getEnergy());
                return;
            }
        }
    }

    @Override
    public NBTTagCompound writeTag(final NBTTagCompound tag) {
        super.writeTag(tag);
        tag.setInteger("id", this.type.ordinal());
        tag.setString("type", "fluidfactory");
        return tag;
    }

    @Override
    public void remove() {

    }

    @Override
    public int getEnergy() {
        return this.type.getEnergy();
    }

    @Override
    public FluidStack getStack() {
        return this.type.getStack();
    }

    @Override
    public int getNeedPeople() {
        return this.type.getNeedPeople();
    }

    @Override
    public int getNeedProtection() {
        return this.type.getNeedProtection();
    }

    @Override
    public EnumFluidFactory getFactory() {
        return this.type;
    }

}
