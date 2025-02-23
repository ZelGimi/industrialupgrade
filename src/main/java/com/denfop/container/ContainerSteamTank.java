package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileSteamStorage;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamTank extends ContainerFullInv<TileSteamStorage> {

    public ContainerSteamTank(EntityPlayer entityPlayer, TileSteamStorage tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
