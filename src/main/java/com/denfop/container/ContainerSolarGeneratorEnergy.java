package com.denfop.container;

import com.denfop.tiles.base.TileEntitySolarGeneratorEnergy;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerSolarGeneratorEnergy extends ContainerFullInv<TileEntitySolarGeneratorEnergy> {

    public ContainerSolarGeneratorEnergy(EntityPlayer entityPlayer, TileEntitySolarGeneratorEnergy tileEntity) {
        super(entityPlayer, tileEntity, 196);

        addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 67 + 2, 34 + 1));
        for (int i = 0; i < tileEntity.input.size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity.input, i, 80 + i * 18, 85));
        }

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("sunenergy");
        ret.add("work");
        ret.add("generation");

        return ret;

    }

}
