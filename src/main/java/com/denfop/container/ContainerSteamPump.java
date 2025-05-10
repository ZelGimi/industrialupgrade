package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileSteamPump;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamPump extends ContainerFullInv<TileSteamPump> {

    public ContainerSteamPump(Player entityPlayer, TileSteamPump tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
