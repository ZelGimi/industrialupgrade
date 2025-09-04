package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamConverter;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamConverter extends ContainerMenuFullInv<BlockEntitySteamConverter> {

    public ContainerMenuSteamConverter(Player entityPlayer, BlockEntitySteamConverter tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
