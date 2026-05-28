package com.denfop.api.storage;

import com.denfop.api.storage.autocrafting.PatternStack;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IMechanismInterface extends Interface {

    BlockEntity getBlockEntity(Direction direction);

    InterfaceSide getSides(PatternStack patternStack);
}
