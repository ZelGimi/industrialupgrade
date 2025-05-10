package com.denfop.api.space.research.api;

import com.denfop.api.space.EnumLevels;
import com.denfop.api.space.IBody;
import com.denfop.api.space.fakebody.SpaceOperation;

import java.util.Map;
import java.util.UUID;

public interface IResearchTable {

    Map<IBody, SpaceOperation> getSpaceBody();

    UUID getPlayer();


    EnumLevels getLevelTable();

    void setLevel(EnumLevels level);

}
