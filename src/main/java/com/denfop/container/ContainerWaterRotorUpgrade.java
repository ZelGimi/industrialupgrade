package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityWaterRotorModifier;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWaterRotorUpgrade extends ContainerFullInv<TileEntityWaterRotorModifier> {

    public ContainerWaterRotorUpgrade(TileEntityWaterRotorModifier tileEntityRotorModifier, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntityRotorModifier, 206);
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                0, 78, 7
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                1, 35, 50
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                2, 121, 50
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                3, 78, 93
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.rotor_slot,
                0, 78, 50
        ));
    }


}
