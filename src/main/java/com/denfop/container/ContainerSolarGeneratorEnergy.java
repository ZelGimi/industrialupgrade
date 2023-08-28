package com.denfop.container;

import com.denfop.tiles.base.TileSolarGeneratorEnergy;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSolarGeneratorEnergy extends ContainerFullInv<TileSolarGeneratorEnergy> {

    public ContainerSolarGeneratorEnergy(EntityPlayer entityPlayer, TileSolarGeneratorEnergy tileEntity) {
        super(entityPlayer, tileEntity, 196);

        addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 67 + 2, 34 + 1));
        for (int i = 0; i < tileEntity.input.size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity.input, i, 80 + i * 18, 85));
        }

    }


}
