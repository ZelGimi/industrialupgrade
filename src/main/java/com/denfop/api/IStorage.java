package com.denfop.api;

public interface IStorage {

    double getEUCapacity();

    double getRFCapacity();

    int getTier();

    byte getRedstoneMode();

    boolean shouldEmitRedstone();

    boolean shouldEmitEnergy();

    double getEUStored();

    double getRFStored();

    double getOutput();

}
