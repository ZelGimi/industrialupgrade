package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntitySocketFactory;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSocket extends ContainerFullInv<TileEntitySocketFactory> {

    public ContainerSocket(TileEntitySocketFactory var1, EntityPlayer var11) {
        super(var11,var1);
        for(int i =0;i < 2;i++){
            this.addSlotToContainer(new SlotInvSlot(var1.inputSlotA, i, 40 + i * 18, 17));
        }
        for(int i =2;i < 4;i++){
            this.addSlotToContainer(new SlotInvSlot(var1.inputSlotA, i, 22 + (i-2) * 18, 35));
        }
        for(int i =4;i < 6;i++){
            this.addSlotToContainer(new SlotInvSlot(var1.inputSlotA, i, 40 + (i-4) * 18, 53));
        }
        this.addSlotToContainer(new SlotInvSlot(var1.outputSlot, 0, 120, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(var1.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
