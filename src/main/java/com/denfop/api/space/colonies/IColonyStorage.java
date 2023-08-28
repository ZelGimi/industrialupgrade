package com.denfop.api.space.colonies;

public interface IColonyStorage {

    IStorage getStorage();

    int getEnergy();

    int getPeoples();

    boolean getWork();

    void setWork(boolean setWork);

}
