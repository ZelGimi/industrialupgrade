package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamTank extends ContainerMenuFullInv<BlockEntitySteamStorage> {

    public ContainerMenuSteamTank(Player entityPlayer, BlockEntitySteamStorage tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

    }


}
