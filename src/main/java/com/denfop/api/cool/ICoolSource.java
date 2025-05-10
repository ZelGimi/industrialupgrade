package com.denfop.api.cool;

public interface ICoolSource extends ICoolEmitter {

    double getOfferedCool();

    boolean isAllowed();

    void setAllowed(boolean allowed);

}
