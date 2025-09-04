package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamPeatGenerator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamPeatGenerator extends ContainerMenuFullInv<BlockEntitySteamPeatGenerator> {

    public ContainerMenuSteamPeatGenerator(Player entityPlayer, BlockEntitySteamPeatGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slot, 0, 25, 53));
    }


}
