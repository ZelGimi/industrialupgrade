package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steamturbine.controller.BlockEntityBaseSteamTurbineController;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuBaseSteamTurbineController extends ContainerMenuFullInv<BlockEntityBaseSteamTurbineController> {

    public ContainerMenuBaseSteamTurbineController(
            BlockEntityBaseSteamTurbineController tile,
            Player var1
    ) {
        super(var1, tile);
    }

}
