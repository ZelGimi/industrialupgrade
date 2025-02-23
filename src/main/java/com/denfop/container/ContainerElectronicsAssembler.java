package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityPrimalElectronicsAssembler;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerElectronicsAssembler
        extends ContainerFullInv<TileEntityPrimalElectronicsAssembler> {

    public ContainerElectronicsAssembler(EntityPlayer entityPlayer, TileEntityPrimalElectronicsAssembler tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerElectronicsAssembler(
            EntityPlayer entityPlayer,
            TileEntityPrimalElectronicsAssembler tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 0, 21, 17));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 1, 39, 17));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 2, 57, 17));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 3, 30, 35));
        }
        if ((tileEntity1).outputSlot != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).outputSlot, 0, 135, 26));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 4, 48, 35));
        }

    }


}
