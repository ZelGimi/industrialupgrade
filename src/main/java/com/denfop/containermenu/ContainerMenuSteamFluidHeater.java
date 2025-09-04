package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamFluidHeater;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamFluidHeater extends ContainerMenuFullInv<BlockEntitySteamFluidHeater> {

    public ContainerMenuSteamFluidHeater(Player entityPlayer, BlockEntitySteamFluidHeater tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
