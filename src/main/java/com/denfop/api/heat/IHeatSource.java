package com.denfop.api.heat;

public interface IHeatSource extends IHeatEmitter {

    double getOfferedHeat();

    void drawHeat(double var1);

    boolean isAllowed();

    boolean setAllowed(boolean allowed);

}
