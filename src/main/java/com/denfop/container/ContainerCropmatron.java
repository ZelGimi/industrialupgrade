package com.denfop.container;


import com.denfop.tiles.mechanism.TileEntityCropmatron;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerCropmatron extends ContainerFullInv<TileEntityCropmatron> {

    public ContainerCropmatron(EntityPlayer player, TileEntityCropmatron base) {
        super(player, base, 192);
        this.addSlotToContainer(new com.denfop.container.SlotInvSlot(base.dischargeSlot, 0, 134, 80));

        int i;
        for (i = 0; i < base.fertilizerSlot.size(); ++i) {
            this.addSlotToContainer(new SlotInvSlot(base.fertilizerSlot, i, 8 + i * 18, 80));
        }

        this.addSlotToContainer(new SlotInvSlot(base.exInputSlot, 0, 49, 27));
        this.addSlotToContainer(new SlotInvSlot(base.exOutputSlot, 0, 67, 27));
        this.addSlotToContainer(new SlotInvSlot(base.wasserinputSlot, 0, 57, 56));
        this.addSlotToContainer(new SlotInvSlot(base.wasseroutputSlot, 0, 75, 56));

        for (i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotInvSlot(base.upgradeSlot, i, 152, 26 + i * 18));
        }

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("waterTank");
        ret.add("exTank");
        return ret;
    }

}
