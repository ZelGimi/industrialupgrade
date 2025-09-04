package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamSharpener;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamSharpener extends ContainerMenuFullInv<BlockEntitySteamSharpener> {

    public ContainerMenuSteamSharpener(Player var1, BlockEntitySteamSharpener tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 60, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 110, 44));

    }

}
