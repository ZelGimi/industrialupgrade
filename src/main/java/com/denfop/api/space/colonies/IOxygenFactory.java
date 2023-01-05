package com.denfop.api.space.colonies;

public interface IOxygenFactory {

    int getMax();

    int getGeneration();

    int getEnergy();

    boolean needWorkers();

    int getPeople();

}
