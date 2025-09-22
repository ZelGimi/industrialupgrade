package com.denfop.container;

import com.denfop.tiles.mechanism.steam.TileSteamSharpener;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamSharpener extends ContainerFullInv<TileSteamSharpener> {

    public ContainerSteamSharpener(EntityPlayer var1, TileSteamSharpener tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 60, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 110, 44));

    }

}
