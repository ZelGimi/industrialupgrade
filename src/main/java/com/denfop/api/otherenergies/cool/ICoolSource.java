package com.denfop.api.otherenergies.cool;

public interface ICoolSource extends ICoolEmitter {

    double getOfferedCool();

    boolean isAllowed();

    void setAllowed(boolean allowed);

}
