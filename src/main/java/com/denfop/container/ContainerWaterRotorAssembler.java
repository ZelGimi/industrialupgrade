package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityWaterRotorAssembler;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerWaterRotorAssembler extends ContainerFullInv<TileEntityWaterRotorAssembler> {

    public ContainerWaterRotorAssembler(
            final TileEntityWaterRotorAssembler tileEntity1,
            EntityPlayer entityPlayer
    ) {
        super(entityPlayer, tileEntity1, 255);

        addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 0, 88, 11));
        addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 1, 28, 71));
        addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 2, 88, 131));
        addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 3, 148, 71));
        addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 4, 88, 71));
        addSlotToContainer(new SlotInvSlot((tileEntity1).outputSlot, 0, 183, 148));
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        ret.add("inputSlotA");
        ret.add("progress");
        ret.add("guiProgress");
        return ret;
    }

}
