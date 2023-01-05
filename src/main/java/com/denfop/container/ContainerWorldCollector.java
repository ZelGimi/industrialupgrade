package com.denfop.container;

import com.denfop.tiles.base.TileEntityBaseWorldCollector;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerWorldCollector extends ContainerFullInv<TileEntityBaseWorldCollector> {

    public ContainerWorldCollector(TileEntityBaseWorldCollector tileEntity1, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntity1, 166);


        addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot,
                0, 56, 17
        ));


        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                0, 116, 35
        ));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 8 + i * 18
            ));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.MatterSlot, 0, 56, 53));

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("guiProgress");
        ret.add("enumTypeCollector");
        ret.add("need_matter");
        ret.add("max_matter_energy");
        ret.add("matter_energy");
        return ret;
    }

}
