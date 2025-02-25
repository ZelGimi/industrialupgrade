package com.denfop.container;

import com.denfop.tiles.smeltery.TileEntitySmelteryCasting;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSmelteryCasting extends ContainerFullInv<TileEntitySmelteryCasting> {

    public ContainerSmelteryCasting(
            TileEntitySmelteryCasting tileEntityGeothermalExchanger,
            EntityPlayer var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getOutputSlot(), 0, 107, 35));
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getInputSlotB(), 0, 28, 35));
    }

}
