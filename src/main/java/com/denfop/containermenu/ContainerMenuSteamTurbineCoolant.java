package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steamturbine.coolant.BlockEntityBaseSteamTurbineCoolant;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamTurbineCoolant extends ContainerMenuFullInv<BlockEntityBaseSteamTurbineCoolant> {

    public ContainerMenuSteamTurbineCoolant(BlockEntityBaseSteamTurbineCoolant tileEntityMainTank, Player var1) {
        super(var1, tileEntityMainTank);
    }

}
