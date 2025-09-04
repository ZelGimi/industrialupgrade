package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steamturbine.BlockEntitySteamTurbineControllerRod;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamTurbineControllerRod extends ContainerMenuFullInv<BlockEntitySteamTurbineControllerRod> {

    public ContainerMenuSteamTurbineControllerRod(
            BlockEntitySteamTurbineControllerRod tile,
            Player var1
    ) {
        super(var1, tile, 200, 166);
    }

}
