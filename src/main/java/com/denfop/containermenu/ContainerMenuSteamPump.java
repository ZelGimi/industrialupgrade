package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamPump;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamPump extends ContainerMenuFullInv<BlockEntitySteamPump> {

    public ContainerMenuSteamPump(Player entityPlayer, BlockEntitySteamPump tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
