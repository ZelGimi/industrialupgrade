package com.denfop.api.pressure;

public interface IPressureSource extends IPressureEmitter {

    double getOfferedPressure();

    void drawPressure(double var1);

    boolean isAllowed();

    boolean setAllowed(boolean allowed);

}
