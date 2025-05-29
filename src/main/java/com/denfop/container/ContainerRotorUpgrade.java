package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityRotorModifier;
import net.minecraft.world.entity.player.Player;

public class ContainerRotorUpgrade extends ContainerFullInv<TileEntityRotorModifier> {

    public ContainerRotorUpgrade(TileEntityRotorModifier tileEntityRotorModifier, Player entityPlayer) {
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
