package com.denfop.api.space.colonies;

public interface IColonyPanelFactory extends IColonyBuilding {

    int getGeneration();

    int getPeople();

    EnumTypeSolarPanel getType();

}
