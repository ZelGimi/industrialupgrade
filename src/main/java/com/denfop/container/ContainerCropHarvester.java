package com.denfop.container;


import com.denfop.tiles.base.TileEntityCropHarvester;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCropHarvester extends ContainerFullInv<TileEntityCropHarvester> {

    public ContainerCropHarvester(EntityPlayer player, TileEntityCropHarvester base) {
        super(player, base, 166);
        this.addSlotToContainer(new com.denfop.container.SlotInvSlot(base.dischargeSlot, 0, 16, 53));

        int i;
        for (i = 0; i < base.contentSlot.size() / 5; ++i) {
            for (int x = 0; x < 5; ++x) {
                this.addSlotToContainer(new SlotInvSlot(base.contentSlot, x + i * 5, 48 + x * 18, 17 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 8 + i * 18));
        }

    }


}
