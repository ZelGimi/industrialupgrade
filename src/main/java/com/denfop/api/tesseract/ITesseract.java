package com.denfop.api.tesseract;

import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.invslot.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface ITesseract {

    Channel getChannel(int channel);

    World getLevel();

    BlockPos getBlockPos();

    List<Channel> getChannels();

    String getPlayer();

    Energy getEnergy();

    Fluids.InternalFluidTank getTank();

    Inventory getSlotItem();

}
