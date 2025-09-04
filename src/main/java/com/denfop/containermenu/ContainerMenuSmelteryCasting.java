package com.denfop.containermenu;

import com.denfop.blockentity.smeltery.BlockEntitySmelteryCasting;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSmelteryCasting extends ContainerMenuFullInv<BlockEntitySmelteryCasting> {

    public ContainerMenuSmelteryCasting(
            BlockEntitySmelteryCasting tileEntityGeothermalExchanger,
            Player var1
    ) {
        super(var1, tileEntityGeothermalExchanger, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getOutputSlot(), 0, 107, 35));
        this.addSlotToContainer(new SlotInvSlot(tileEntityGeothermalExchanger.getInputSlotB(), 0, 28, 35));
    }

}
