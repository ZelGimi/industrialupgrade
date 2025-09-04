package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.steam.BlockEntitySteamBioGenerator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuBioGenerator extends ContainerMenuFullInv<BlockEntitySteamBioGenerator> {

    public ContainerMenuBioGenerator(Player entityPlayer, BlockEntitySteamBioGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 43, 40));


    }


}
