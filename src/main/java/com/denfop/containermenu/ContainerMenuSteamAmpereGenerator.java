package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamAmpereGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamAmpereGenerator extends ContainerMenuFullInv<BlockEntitySteamAmpereGenerator> {

    public ContainerMenuSteamAmpereGenerator(Player entityPlayer, BlockEntitySteamAmpereGenerator tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
