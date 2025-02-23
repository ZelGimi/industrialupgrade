package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.api.IColonyBuilding;

public interface IProtectionBuilding extends IColonyBuilding {

    int getProtection();

    short needWorkers();

    void addWorkers(int addWorkers);

    int getWorkers();

    void removeWorkers(int addWorkers);

}
