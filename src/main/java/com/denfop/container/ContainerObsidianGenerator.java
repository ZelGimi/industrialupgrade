package com.denfop.container;

import com.denfop.tiles.base.TileBaseObsidianGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerObsidianGenerator extends ContainerFullInv<TileBaseObsidianGenerator> {

    public ContainerObsidianGenerator(EntityPlayer entityPlayer, TileBaseObsidianGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 131, 34));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot1, 0, 55, 65));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 15, 10));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 15, 34));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 8 + i * 18
            ));
        }
    }


}
