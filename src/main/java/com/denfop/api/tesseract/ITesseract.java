package com.denfop.api.tesseract;

import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.inventory.Inventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ITesseract {

    Channel getChannel(int channel);

    Level getWorld();

    BlockPos getPos();

    List<Channel> getChannels();

    String getPlayer();

    Energy getEnergy();

    Fluids.InternalFluidTank getTank();

    Inventory getSlotItem();

}
