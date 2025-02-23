package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamBioGenerator;
import com.denfop.tiles.mechanism.steam.TileEntitySteamSqueezer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBioGenerator extends ContainerFullInv<TileEntitySteamBioGenerator> {

    public ContainerBioGenerator(EntityPlayer entityPlayer, TileEntitySteamBioGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 43, 40));


    }


}
