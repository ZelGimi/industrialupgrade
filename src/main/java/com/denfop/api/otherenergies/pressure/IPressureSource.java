package com.denfop.api.otherenergies.pressure;

public interface IPressureSource extends IPressureEmitter {

    double getOfferedPressure();

    void drawPressure(double var1);

    boolean isAllowed();

    boolean setAllowed(boolean allowed);

}
