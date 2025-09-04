package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntitySteamGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamGenerator extends ContainerMenuFullInv<BlockEntitySteamGenerator> {

    public ContainerMenuSteamGenerator(Player entityPlayer, BlockEntitySteamGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
    }


}
