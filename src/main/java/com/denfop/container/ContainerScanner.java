package com.denfop.container;


import com.denfop.tiles.base.TileEntityScanner;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerScanner extends ContainerElectricMachine<TileEntityScanner> {

    public ContainerScanner(EntityPlayer player, TileEntityScanner tileEntity1) {
        super(player, tileEntity1, 166, 8, 43);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot, 0, 55, 35));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.diskSlot, 0, 152, 65));
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("state");
        ret.add("progress");
        ret.add("patternEu");
        ret.add("patternUu");
        return ret;
    }

}
