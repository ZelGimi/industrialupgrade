package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.enums.EnumTypeFactory;
import com.denfop.api.space.colonies.enums.EnumTypeOxygenFactory;

public interface IOxygenFactory {

    int getMax();

    int getGeneration();

    int getEnergy();

    int needWorkers();

    int getPeople();

    void addWorkers(int workers);

    void removeWorkers(int remove);

    int getWorkers();

    EnumTypeOxygenFactory getType();

}
