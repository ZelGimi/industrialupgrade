package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileSteamPeatGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamPeatGenerator extends ContainerFullInv<TileSteamPeatGenerator> {

    public ContainerSteamPeatGenerator(EntityPlayer entityPlayer, TileSteamPeatGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slot, 0, 25, 53));
    }


}
