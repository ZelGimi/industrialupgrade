package com.denfop.api.cool;

public interface ICoolSource extends ICoolEmitter {

    double getOfferedCool();

    void drawCool(double var1);

    boolean isAllowed();

    void setAllowed(boolean allowed);

}
