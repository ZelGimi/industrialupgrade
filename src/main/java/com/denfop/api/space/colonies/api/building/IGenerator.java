package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.api.IColonyBuilding;

public interface IGenerator extends IColonyBuilding {

    int getPeople();

    int getEnergy();

    int needWorkers();

    void addWorkers(int workers);

    void removeWorkers(int remove);

    int getWorkers();

}
