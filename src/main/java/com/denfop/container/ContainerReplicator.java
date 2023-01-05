package com.denfop.container;


import com.denfop.tiles.mechanism.TileEntityBaseReplicator;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerReplicator extends ContainerElectricMachine<TileEntityBaseReplicator> {

    public ContainerReplicator(EntityPlayer player, TileEntityBaseReplicator tileEntity1) {
        super(player, tileEntity1, 184, 152, 83);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 91, 59));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot, 0, 8, 27));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.cellSlot, 0, 8, 72));

        for (int i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("fluidTank");
        ret.add("uuProcessed");
        ret.add("pattern");
        ret.add("mode");
        ret.add("index");
        ret.add("maxIndex");
        ret.add("patternUu");
        ret.add("patternEu");
        return ret;
    }

}
