package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityIndustrialOrePurifier;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerIndustrialOrePurifier extends ContainerFullInv<TileEntityIndustrialOrePurifier> {

    public ContainerIndustrialOrePurifier(EntityPlayer var1, TileEntityIndustrialOrePurifier tileEntity1) {
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
