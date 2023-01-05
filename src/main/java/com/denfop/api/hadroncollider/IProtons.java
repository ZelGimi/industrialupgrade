package com.denfop.api.hadroncollider;

public interface IProtons {

    void addCoodination(int x, int y, int z);

    void addPercent(int procent);

    TypeProtons getType();

    int getTick();

    int setTick(int tick);

    int getPercent();

}
