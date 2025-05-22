package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.api.IColonyBuilding;
import com.denfop.api.space.colonies.enums.EnumMiningFactory;

public interface IColonyMiningFactory extends IColonyBuilding {

    int getEnergy();

    EnumMiningFactory getFactory();

    int getWorkers();

    int needWorkers();

    void addWorkers(int workers);

    void removeWorkers(int remove);

}
