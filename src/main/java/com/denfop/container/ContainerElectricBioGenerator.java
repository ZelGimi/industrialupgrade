package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityBioGenerator;
import com.denfop.tiles.mechanism.steam.TileEntitySteamBioGenerator;
import com.denfop.tiles.mechanism.steam.TileEntitySteamSqueezer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerElectricBioGenerator extends ContainerFullInv<TileEntityBioGenerator> {

    public ContainerElectricBioGenerator(EntityPlayer entityPlayer, TileEntityBioGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 43, 40));


    }


}
