package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileSteamStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamTank extends ContainerFullInv<TileSteamStorage> {

    public ContainerSteamTank(Player entityPlayer, TileSteamStorage tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
