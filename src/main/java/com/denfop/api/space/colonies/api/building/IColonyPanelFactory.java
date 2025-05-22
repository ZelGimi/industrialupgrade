package com.denfop.api.space.colonies.api.building;

import com.denfop.api.space.colonies.enums.EnumTypeSolarPanel;

public interface IColonyPanelFactory extends IGenerator {


    EnumTypeSolarPanel getType();

}
