package com.denfop.container;

import com.denfop.tiles.base.TileBaseWorldCollector;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWorldCollector extends ContainerFullInv<TileBaseWorldCollector> {

    public ContainerWorldCollector(TileBaseWorldCollector tileEntity1, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntity1, 166);


        addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot,
                0, 56, 17
        ));


        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                0, 116, 35
        ));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 8 + i * 18
            ));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.MatterSlot, 0, 56, 53));

    }


}
