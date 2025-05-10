package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityPerAlloySmelter;
import net.minecraft.world.entity.player.Player;

public class ContainerPerAlloySmelter extends ContainerFullInv<TileEntityPerAlloySmelter> {


    public ContainerPerAlloySmelter(
            Player entityPlayer,
            TileEntityPerAlloySmelter tileEntity1
    ) {
        super(entityPlayer, tileEntity1);
        for (int i = 0; i < 5; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    i, 5 + i * 18, 35
            ));
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 8 + i * 18
            ));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.input_slot,
                0, -20, 84
        ));
        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                0, 127, 35
        ));
    }


}
