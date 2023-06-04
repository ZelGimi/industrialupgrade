package com.denfop.api;

public interface IStorage {

    double getEUCapacity();


    int getTier();

    byte getRedstoneMode();

    boolean shouldEmitRedstone();

    boolean shouldEmitEnergy();

    double getEUStored();

    double getOutput();

}
