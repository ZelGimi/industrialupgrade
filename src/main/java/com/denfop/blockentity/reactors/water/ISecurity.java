package com.denfop.blockentity.reactors.water;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.utils.Timer;

public interface ISecurity extends MultiBlockElement {

    void setActive(String s);

    EnumTypeSecurity getSecurity();

    void setSecurity(EnumTypeSecurity typeSecurity);

    public Timer getYellow_timer();


    public Timer getRed_timer();

}
