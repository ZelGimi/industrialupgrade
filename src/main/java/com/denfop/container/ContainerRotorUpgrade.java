package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityRotorModifier;
import net.minecraft.world.entity.player.Player;

public class ContainerRotorUpgrade extends ContainerFullInv<TileEntityRotorModifier> {

    public ContainerRotorUpgrade(TileEntityRotorModifier tileEntityRotorModifier, Player entityPlayer) {
        super(entityPlayer, tileEntityRotorModifier);
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                0, 80, 9
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                1, 53, 36
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                2, 107, 36
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.slot,
                3, 80, 63
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityRotorModifier.rotor_slot,
                0, 80, 36
        ));
    }


}
