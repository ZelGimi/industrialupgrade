package com.denfop.container;

import com.denfop.tiles.creative.TileEntityCreativeSteamStorage;
import com.denfop.tiles.mechanism.steam.TileSteamStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerCreativeSteamTank extends ContainerFullInv<TileEntityCreativeSteamStorage> {

    public ContainerCreativeSteamTank(Player entityPlayer, TileEntityCreativeSteamStorage tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
