package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steamturbine.BlockEntitySteamTurbineRod;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamTurbineRod extends ContainerMenuFullInv<BlockEntitySteamTurbineRod> {

    public ContainerMenuSteamTurbineRod(
            final BlockEntitySteamTurbineRod tileEntity1,
            Player entityPlayer
    ) {
        super(entityPlayer, tileEntity1);

        addSlotToContainer(new SlotInvSlot((tileEntity1).getSlot(), 0, 80, 11));
        addSlotToContainer(new SlotInvSlot((tileEntity1).getSlot(), 1, 56, 35));
        addSlotToContainer(new SlotInvSlot((tileEntity1).getSlot(), 2, 80, 59));
        addSlotToContainer(new SlotInvSlot((tileEntity1).getSlot(), 3, 104, 35));
    }


}
