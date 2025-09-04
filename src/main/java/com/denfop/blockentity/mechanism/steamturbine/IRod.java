package com.denfop.blockentity.mechanism.steamturbine;

import com.denfop.api.steam.ISteamBlade;
import com.denfop.inventory.Inventory;
import net.minecraft.core.BlockPos;

import java.util.List;

public interface IRod extends com.denfop.api.steam.IRod {

    BlockPos getBlockPos();

    Inventory getSlot();

    List<ISteamBlade> getRods();

    void updateBlades();
}
