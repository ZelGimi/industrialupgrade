package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileEntitySteamBioGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerBioGenerator extends ContainerFullInv<TileEntitySteamBioGenerator> {

    public ContainerBioGenerator(Player entityPlayer, TileEntitySteamBioGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 43, 40));


    }


}
