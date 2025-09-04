package com.denfop.containermenu;


import com.denfop.blockentity.base.BlockEntityDoubleMolecular;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class ContainerMenuBaseDoubleMolecular extends ContainerMenuBase<BlockEntityDoubleMolecular> {

    public ContainerMenuBaseDoubleMolecular(Player entityPlayer, BlockEntityDoubleMolecular tileEntity1) {
        super(tileEntity1, null);
        this.player = entityPlayer;
        this.inventory = entityPlayer.getInventory();

        addSlotToContainer(
                new SlotInvSlot(tileEntity1.inputSlot, 1, 39, 57));


        addSlotToContainer(
                new SlotInvSlot(tileEntity1.inputSlot, 0, 19, 57));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.outputSlot, 0, 29, 97));

        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(
                        new Slot(entityPlayer.getInventory(), k + i * 9 + 9, 27 + k * 21, 128 + i * 21));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(entityPlayer.getInventory(), j, 27 + j * 21, 195));
        }
    }


}
