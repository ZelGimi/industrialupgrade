package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityIndustrialOrePurifier;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuIndustrialOrePurifier extends ContainerMenuFullInv<BlockEntityIndustrialOrePurifier> {

    public ContainerMenuIndustrialOrePurifier(Player var1, BlockEntityIndustrialOrePurifier tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 108, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 62, 44));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 10 + i * 18
            ));
        }
    }

}
