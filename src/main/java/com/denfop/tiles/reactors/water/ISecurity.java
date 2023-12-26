package com.denfop.tiles.reactors.water;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.api.reactors.EnumTypeSecurity;

public interface ISecurity extends IMultiElement {

   void setActive(String s);

   void setSecurity(EnumTypeSecurity typeSecurity);

    EnumTypeSecurity getSecurity();
}
