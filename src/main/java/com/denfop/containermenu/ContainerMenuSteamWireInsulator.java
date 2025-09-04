package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamWireInsulator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamWireInsulator extends ContainerMenuFullInv<BlockEntitySteamWireInsulator> {

    public ContainerMenuSteamWireInsulator(Player var1, BlockEntitySteamWireInsulator tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 30, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 1, 50, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 110, 44));

    }

}
