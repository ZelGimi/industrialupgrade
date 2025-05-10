package com.denfop.tiles.mechanism.steamturbine;

import com.denfop.api.steam.ISteamBlade;
import com.denfop.invslot.InvSlot;
import net.minecraft.core.BlockPos;

import java.util.List;

public interface IRod extends com.denfop.api.steam.IRod {

    BlockPos getBlockPos();

    InvSlot getSlot();

    List<ISteamBlade> getRods();

    void updateBlades();
}
