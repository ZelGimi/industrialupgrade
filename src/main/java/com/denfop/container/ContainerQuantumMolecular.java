package com.denfop.container;


import com.denfop.tiles.base.TileQuantumMolecular;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class ContainerQuantumMolecular extends ContainerBase<TileQuantumMolecular> {

    public ContainerQuantumMolecular(Player entityPlayer, TileQuantumMolecular tileEntity1) {
        super(tileEntity1, null);
        this.player = entityPlayer;
        this.inventory = player.getInventory();

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
