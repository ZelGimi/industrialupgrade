package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamPressureConverter;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamPressureConverter extends ContainerMenuFullInv<BlockEntitySteamPressureConverter> {

    public ContainerMenuSteamPressureConverter(
            Player entityPlayer,
            BlockEntitySteamPressureConverter tileEntityBaseHeatMachine
    ) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);


    }


}
