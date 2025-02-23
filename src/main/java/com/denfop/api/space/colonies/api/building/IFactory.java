package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.enums.EnumTypeFactory;
import com.denfop.api.space.colonies.api.IColonyBuilding;

public interface IFactory extends IColonyBuilding {

    int getWorkers();
    int needWorkers();

    void addWorkers(int workers);

    void removeWorkers(int remove);
    int getEnergy();

    EnumTypeFactory getType();



}
