package com.denfop.blockentity.lightning_rod;

import com.denfop.api.multiblock.MainMultiBlock;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.Energy;
import net.minecraft.core.BlockPos;

public interface IController extends MainMultiBlock {

    Energy getEnergy();

    BlockPos getBlockAntennaPos();

    ComponentTimer getTimer();

}
