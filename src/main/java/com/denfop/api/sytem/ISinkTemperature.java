package com.denfop.api.sytem;

public interface ISinkTemperature extends ISink {

    boolean needTemperature();

    boolean setNeedTemperature(boolean need);

}
