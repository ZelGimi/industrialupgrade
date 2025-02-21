package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.enums.EnumTypeSolarPanel;
import com.denfop.api.space.colonies.api.IColonyBuilding;

public interface IColonyPanelFactory extends IGenerator  {




    EnumTypeSolarPanel getType();

}
