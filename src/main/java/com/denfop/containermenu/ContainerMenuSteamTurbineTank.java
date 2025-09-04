package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steamturbine.tank.BlockEntityBaseSteamTurbineTank;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamTurbineTank extends ContainerMenuFullInv<BlockEntityBaseSteamTurbineTank> {

    public ContainerMenuSteamTurbineTank(BlockEntityBaseSteamTurbineTank tileEntityMainTank, Player var1) {
        super(var1, tileEntityMainTank);
    }

}
