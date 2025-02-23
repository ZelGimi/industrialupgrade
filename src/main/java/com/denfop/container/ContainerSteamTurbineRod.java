package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityWaterRotorAssembler;
import com.denfop.tiles.mechanism.steamturbine.TileEntitySteamTurbineRod;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamTurbineRod extends ContainerFullInv<TileEntitySteamTurbineRod> {

    public ContainerSteamTurbineRod(
            final TileEntitySteamTurbineRod tileEntity1,
            EntityPlayer entityPlayer
    ) {
        super(entityPlayer, tileEntity1, 255);

        addSlotToContainer(new SlotInvSlot((tileEntity1).getSlot(), 0, 88, 11));
        addSlotToContainer(new SlotInvSlot((tileEntity1).getSlot(), 1, 28, 71));
        addSlotToContainer(new SlotInvSlot((tileEntity1).getSlot(), 2, 88, 131));
        addSlotToContainer(new SlotInvSlot((tileEntity1).getSlot(), 3, 148, 71));
    }


}
