package com.denfop.api.space.research;

import com.denfop.api.space.EnumLevels;
import com.denfop.api.space.IBody;
import com.denfop.api.space.fakebody.FakePlayer;
import com.denfop.api.space.fakebody.SpaceOperation;

import java.util.List;
import java.util.Map;

public interface IResearchTable {

    Map<IBody, SpaceOperation> getSpaceBody();

    FakePlayer getPlayer();

    List<SpaceOperation> getSpaceFakeBody();

    IContainerBlock getContainerBlock();

    void setContainerBlock(IContainerBlock containerBlock);

    EnumLevels getLevel();

    void setLevel(EnumLevels level);

}
