package com.denfop.api;

public interface Storage {

    double getEUCapacity();


    int getTier();

    byte getRedstoneMode();

    boolean shouldEmitRedstone();

    boolean shouldEmitEnergy();

    double getEUStored();

    double getOutput();

}
