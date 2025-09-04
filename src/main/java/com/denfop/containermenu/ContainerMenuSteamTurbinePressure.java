package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steamturbine.pressure.BlockEntityBaseSteamTurbinePressure;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamTurbinePressure extends ContainerMenuFullInv<BlockEntityBaseSteamTurbinePressure> {

    public ContainerMenuSteamTurbinePressure(BlockEntityBaseSteamTurbinePressure tileEntityBaseCompressor, Player var1) {
        super(var1, tileEntityBaseCompressor);
    }

}
