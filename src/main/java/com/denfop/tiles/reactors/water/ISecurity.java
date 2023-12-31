package com.denfop.tiles.reactors.water;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.api.reactors.EnumTypeSecurity;
import com.denfop.utils.Timer;

public interface ISecurity extends IMultiElement {

   void setActive(String s);

   void setSecurity(EnumTypeSecurity typeSecurity);

    EnumTypeSecurity getSecurity();

    public Timer getYellow_timer() ;



    public Timer getRed_timer();
}
