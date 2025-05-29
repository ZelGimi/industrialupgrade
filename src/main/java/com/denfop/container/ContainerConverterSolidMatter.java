package com.denfop.container;

import com.denfop.tiles.base.TileConverterSolidMatter;
import net.minecraft.world.entity.player.Player;

public class ContainerConverterSolidMatter extends ContainerFullInv<TileConverterSolidMatter> {

    public ContainerConverterSolidMatter(Player entityPlayer, TileConverterSolidMatter tileEntity) {
        super(entityPlayer, tileEntity, 193);
        for (int i = 0; i < 8; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity.MatterSlot, i, 182, 5 + 18 * i));
        }


        addSlotToContainer(new SlotInvSlot(tileEntity.inputSlot, 0, 12, 19));
        addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 80, 48));
        for (int i = 0; i < 3; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity.upgradeSlot, i, 12, 49 + i * 18));
        }

    }


}
