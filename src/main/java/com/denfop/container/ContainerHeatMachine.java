package com.denfop.container;

import com.denfop.tiles.base.TileEntityBaseHeatMachine;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerHeatMachine extends ContainerFullInv<TileEntityBaseHeatMachine> {

    public ContainerHeatMachine(EntityPlayer entityPlayer, TileEntityBaseHeatMachine tileEntityBaseHeatMachine) {
        super(entityPlayer, tileEntityBaseHeatMachine, 166);
        if (tileEntityBaseHeatMachine.hasFluid) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.fluidSlot, 0, 125, 23));
            this.addSlotToContainer(new SlotInvSlot(tileEntityBaseHeatMachine.outputSlot, 0, 125, 59));
        }

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        if (this.base.hasFluid) {
            ret.add("fluidSlot");
            ret.add("outputSlot");
            ret.add("fluidTank");
        }
        ret.add("auto");
        ret.add("energy");
        ret.add("heat");
        ret.add("maxtemperature");
        ret.add("work");
        return ret;
    }

}
