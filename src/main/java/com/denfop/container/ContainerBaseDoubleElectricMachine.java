package com.denfop.container;

import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public abstract class ContainerBaseDoubleElectricMachine extends ContainerFullInv<TileEntityDoubleElectricMachine> {

    public ContainerBaseDoubleElectricMachine(
            EntityPlayer entityPlayer,
            TileEntityDoubleElectricMachine base1,
            int height,
            int dischargeX,
            int dischargeY,
            boolean register
    ) {
        super(entityPlayer, base1, height);
        if (register) {
            this.addSlotToContainer(new SlotInvSlot(base1.dischargeSlot, 0, dischargeX, dischargeY));
        }
    }

    public List<String> getNetworkedFields() {

        return super.getNetworkedFields();

    }

}
