package com.denfop.api.sytem;

public interface ISourceTemperature extends ISource {

    boolean isAllowed();

    boolean setAllowed(boolean allowed);

}
