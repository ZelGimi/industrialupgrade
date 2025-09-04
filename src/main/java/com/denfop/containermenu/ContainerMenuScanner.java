package com.denfop.containermenu;


import com.denfop.blockentity.base.BlockEntityScanner;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuScanner extends ContainerMenuElectricMachine<BlockEntityScanner> {

    public ContainerMenuScanner(Player player, BlockEntityScanner tileEntity1) {
        super(player, tileEntity1, 166, 8, 43);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot, 0, 55, 35));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.diskSlot, 0, 152, 65));
    }


}
