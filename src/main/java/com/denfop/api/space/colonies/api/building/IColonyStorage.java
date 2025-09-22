package com.denfop.api.space.colonies.api.building;

public interface IColonyStorage {

    IStorage getStorage();

    int getEnergy();

    int getPeoples();

    boolean getWork();

    void setWork(boolean setWork);

    int getWorkers();

    int needWorkers();

    void addWorkers(int workers);

    void removeWorkers(int remove);

}
