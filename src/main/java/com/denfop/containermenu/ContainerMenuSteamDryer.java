package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamDryer;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamDryer extends ContainerMenuFullInv<BlockEntitySteamDryer> {

    public ContainerMenuSteamDryer(Player entityPlayer, BlockEntitySteamDryer tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 100, 40));


    }


}
