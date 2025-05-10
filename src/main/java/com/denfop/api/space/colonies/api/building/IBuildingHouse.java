package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.api.IColonyBuilding;
import com.denfop.api.space.colonies.enums.EnumHouses;
import com.denfop.api.space.colonies.enums.EnumHousesLevel;

public interface IBuildingHouse extends IColonyBuilding {

    EnumHousesLevel getLevel();

    EnumHouses getHouses();

    int getPeople();

    int getEnergy();


    int getFreeWorkers();

    int getMaxPeople();

    void addPeople(int peoples);

    int getWorkers();


    void removeFreeWorkers(int workers);
}
