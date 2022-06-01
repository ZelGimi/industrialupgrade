package com.denfop.api.space.colonies;

public interface IFactory extends IColonyBuilding {

    int getWorkers();

    int getEnergy();

    EnumTypeFactory getType();

}
