package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityConverterSolidMatter;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuConverterSolidMatter extends ContainerMenuFullInv<BlockEntityConverterSolidMatter> {

    public ContainerMenuConverterSolidMatter(Player entityPlayer, BlockEntityConverterSolidMatter tileEntity) {
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
