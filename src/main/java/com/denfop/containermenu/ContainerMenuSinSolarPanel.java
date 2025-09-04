package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntitySintezator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSinSolarPanel extends ContainerMenuFullInv<BlockEntitySintezator> {

    public final BlockEntitySintezator tileentity;

    public ContainerMenuSinSolarPanel(Player entityPlayer, BlockEntitySintezator tileEntity1) {
        super(entityPlayer, tileEntity1, 178, 174);
        this.tileentity = tileEntity1;

        for (int j = 0; j < 9; ++j) {

            this.addSlotToContainer(new SlotInvSlot(this.tileentity.inputslot, j, 9 + j * 18, 60));
        }
        for (int j = 0; j < 4; ++j) {

            this.addSlotToContainer(new SlotInvSlot(this.tileentity.inputslotA, j, 95 + j * 18, 16));
        }
    }


}
