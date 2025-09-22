package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityPositronConverter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPositronsConverter extends ContainerFullInv<TileEntityPositronConverter> {

    public ContainerPositronsConverter(TileEntityPositronConverter TileEntityPositronConverter, EntityPlayer var1) {
        super(TileEntityPositronConverter, var1);
        this.addSlotToContainer(new SlotInvSlot(TileEntityPositronConverter.inputSlotA, 0, 25, 25));
        this.addSlotToContainer(new SlotInvSlot(TileEntityPositronConverter.inputSlotA, 1, 55, 25));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
