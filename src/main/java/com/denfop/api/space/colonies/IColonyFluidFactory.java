package com.denfop.api.space.colonies;

import net.minecraftforge.fluids.FluidStack;

public interface IColonyFluidFactory extends IColonyBuilding {

    int getEnergy();

    FluidStack getStack();

    int getNeedPeople();

    int getNeedProtection();

    EnumFluidFactory getFactory();

}
