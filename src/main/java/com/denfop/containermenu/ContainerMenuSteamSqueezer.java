package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamSqueezer;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSteamSqueezer extends ContainerMenuFullInv<BlockEntitySteamSqueezer> {

    public ContainerMenuSteamSqueezer(Player entityPlayer, BlockEntitySteamSqueezer tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 43, 40));


    }


}
