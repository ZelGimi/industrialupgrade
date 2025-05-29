package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntitySteamGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamGenerator extends ContainerFullInv<TileEntitySteamGenerator> {

    public ContainerSteamGenerator(Player entityPlayer, TileEntitySteamGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
    }


}
