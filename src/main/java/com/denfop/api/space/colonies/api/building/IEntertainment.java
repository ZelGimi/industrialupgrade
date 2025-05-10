package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.api.IColonyBuilding;
import com.denfop.api.space.colonies.enums.EnumEntertainment;

public interface IEntertainment extends IColonyBuilding {

    int getWorkers();

    int needWorkers();

    void addWorkers(int workers);

    void removeWorkers(int remove);

    EnumEntertainment getType();

}
