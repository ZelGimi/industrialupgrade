package com.denfop.tiles.mechanism.steamturbine;

import com.denfop.api.steam.ISteamBlade;
import com.denfop.invslot.Inventory;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface IRod extends com.denfop.api.steam.IRod {

    BlockPos getBlockPos();

    Inventory getSlot();

    List<ISteamBlade> getRods();

    void updateBlades();

}
