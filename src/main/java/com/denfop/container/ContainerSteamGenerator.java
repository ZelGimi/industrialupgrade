package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntitySteamGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamGenerator extends ContainerFullInv<TileEntitySteamGenerator> {

    public ContainerSteamGenerator(EntityPlayer entityPlayer, TileEntitySteamGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
    }


}
