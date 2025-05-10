package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamDryer;
import net.minecraft.world.entity.player.Player;

public class ContainerSteamDryer extends ContainerFullInv<TileEntitySteamDryer> {

    public ContainerSteamDryer(Player entityPlayer, TileEntitySteamDryer tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 100, 40));


    }


}
