package com.denfop.container;

import com.denfop.tiles.base.TileEntityElectrolyzer;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerElectrolyzer extends ContainerFullInv<TileEntityElectrolyzer> {

    public ContainerElectrolyzer(EntityPlayer entityPlayer, TileEntityElectrolyzer tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot[0], 0, 14, 63));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 76, 63));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 108, 63));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot[0], 0, 54, 16));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot[1], 0, 130, 16));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tileEntity1).upgradeSlot, i, 152, 8 + i * 18));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.cathodeslot, 0, 54, 34));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.anodeslot, 0, 130, 34));

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        ret.add("fluidTank");
        return ret;
    }

}
