package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileSteamPump;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamPump extends ContainerFullInv<TileSteamPump> {

    public ContainerSteamPump(EntityPlayer entityPlayer, TileSteamPump tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
