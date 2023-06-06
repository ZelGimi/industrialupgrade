package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityCanner;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerCanner extends ContainerFullInv<TileEntityCanner> {

    public ContainerCanner(TileEntityCanner tileEntity1, EntityPlayer player) {
        super(player, tileEntity1, 184);
        if (tileEntity1.outputSlot != null) {
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 119, 17));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 41, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 1, 80, 44));

        for (int i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 26 + i * 18));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.dischargeSlot, 0, 8, 80));

    }


    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("fluidTank");
        ret.add("outputTank");
        ret.add("inputSlotA");
        return ret;
    }

}
