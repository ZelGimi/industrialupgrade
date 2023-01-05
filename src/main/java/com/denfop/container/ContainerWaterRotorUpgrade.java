package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityWaterRotorModifier;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

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

    @Override
    public List<String> getNetworkedFields() {
        final List<String> ret = super.getNetworkedFields();
        ret.add("rotor_slot");
        ret.add("slot");
        return ret;
    }

}
