package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityTripleSolidMixer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerTripleSolidMixer extends ContainerFullInv<TileEntityTripleSolidMixer> {

    public ContainerTripleSolidMixer(EntityPlayer var1, TileEntityTripleSolidMixer tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 108, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 131, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 10, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 1, 36, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 2, 62, 44));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 10 + i * 18
            ));
        }
    }

}
