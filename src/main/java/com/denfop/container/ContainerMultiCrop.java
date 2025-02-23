package com.denfop.container;

import com.denfop.tiles.crop.TileEntityMultiCrop;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMultiCrop extends ContainerFullInv<TileEntityMultiCrop> {

    public ContainerMultiCrop(TileEntityMultiCrop tileEntityMultiCrop, EntityPlayer var1) {
        super(var1, tileEntityMultiCrop, 178 + 40, 166 + 40);
        int totalSlots = tileEntityMultiCrop.place.length;
        int centerX = (178 + 40 - 18) / 2 - 1;
        int slotSpacing = 40;

        for (int i = 0; i < totalSlots; i++) {

            int xDisplayPosition1 = centerX - ((totalSlots - 1) * slotSpacing) / 2 + i * slotSpacing;

            addSlotToContainer(new SlotInvSlot(tileEntityMultiCrop.upBlockSlot, i, xDisplayPosition1, 16));
            addSlotToContainer(new SlotInvSlot(tileEntityMultiCrop.downBlockSlot, i, xDisplayPosition1, 36));
        }

        int xStart = (178 + 40 - 162) / 2;
        for (int i = 0; i < tileEntityMultiCrop.outputSlot.size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityMultiCrop.outputSlot, i,
                    xStart + i * 18 + 4, 16 + 20 + 40
            ));
        }
        addSlotToContainer(new SlotInvSlot(tileEntityMultiCrop.fertilizerSlot, 0,
                xStart + 4 * 18 , 16 + 20 + 40 + 25
        ));
    }

}
