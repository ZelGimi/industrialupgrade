package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityElectricSqueezer;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuElectricSqueezer extends ContainerMenuFullInv<BlockEntityElectricSqueezer> {

    public ContainerMenuElectricSqueezer(Player var1, BlockEntityElectricSqueezer tileEntity1) {
        super(var1, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 0, 120, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 120, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 40, 45));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 172, 21 + i * 18
            ));
        }
    }

}
