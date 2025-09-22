package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamDryer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamDryer extends ContainerFullInv<TileEntitySteamDryer> {

    public ContainerSteamDryer(EntityPlayer entityPlayer, TileEntitySteamDryer tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 100, 40));


    }


}
