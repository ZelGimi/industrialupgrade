package com.denfop.api;

public interface ITemperatureMechanism {

    boolean process(ITemperature tile);

    void transfer(ITemperature receiver, ITemperature extract);

    void work(ITemperature tile);

    boolean hasHeaters(ITemperature tile);
}
