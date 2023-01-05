package com.denfop.container;

import com.denfop.tiles.mechanism.energy.TileEntityEnergySubstitute;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerSubstitute extends ContainerFullInv<TileEntityEnergySubstitute> {

    public ContainerSubstitute(TileEntityEnergySubstitute tileEntity1, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntity1, 179);
        for (int i = 0; i < 16; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.slot, i, 9 + (18 * (i % 4)), 17 + (18 * (i / 4))));

        }
    }

    @Override
    public List<String> getNetworkedFields() {
        final List<String> list = super.getNetworkedFields();
        list.add("size");
        return list;
    }

}
