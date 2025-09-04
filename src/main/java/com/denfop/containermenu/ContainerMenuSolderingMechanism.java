package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPrimalSolderingMechanism;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSolderingMechanism
        extends ContainerMenuFullInv<BlockEntityPrimalSolderingMechanism> {

    public ContainerMenuSolderingMechanism(Player entityPlayer, BlockEntityPrimalSolderingMechanism tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerMenuSolderingMechanism(
            Player entityPlayer,
            BlockEntityPrimalSolderingMechanism tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 0, 21, 17));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).solderingIronSlot, 0, 94, 46));
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
