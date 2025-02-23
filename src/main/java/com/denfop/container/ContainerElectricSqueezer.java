package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityElectricSqueezer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerElectricSqueezer extends ContainerFullInv<TileEntityElectricSqueezer> {

    public ContainerElectricSqueezer(EntityPlayer var1, TileEntityElectricSqueezer tileEntity1) {
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
