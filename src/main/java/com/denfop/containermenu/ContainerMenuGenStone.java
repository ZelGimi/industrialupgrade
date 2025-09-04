package com.denfop.containermenu;


import com.denfop.blockentity.mechanism.BlockEntityBaseGenStone;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGenStone extends ContainerMenuFullInv<BlockEntityBaseGenStone> {

    public ContainerMenuGenStone(Player entityPlayer, BlockEntityBaseGenStone tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 56 - 48, 53, 152, 8);
    }

    public ContainerMenuGenStone(
            Player entityPlayer, BlockEntityBaseGenStone tileEntity1, int height, int dischargeX, int dischargeY,
            int upgradeX, int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 1,
                    38, 22
            ));
        }

        if (tileEntity1.inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0,
                    38, 50
            ));
        }
        if (tileEntity1.outputSlot != null) {
            for (int i = 0; i < tileEntity1.outputSlot.size(); i++) {
                int count = i / 3;
                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, i, 90 + (i - (3 * count)) * 18, 9 + count * 18));


            }
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, upgradeX, upgradeY + i * 18 + 1
            ));
        }
    }


}
