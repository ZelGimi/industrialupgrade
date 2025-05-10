package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityMatterFactory;
import net.minecraft.world.entity.player.Player;

public class ContainerMatterFactory extends ContainerFullInv<TileEntityMatterFactory> {

    public ContainerMatterFactory(TileEntityMatterFactory tileEntityMatterFactory, Player var1) {
        super(tileEntityMatterFactory, var1);
        this.addSlotToContainer(new SlotInvSlot(tileEntityMatterFactory.inputSlotA, 0, 70, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntityMatterFactory.outputSlot, 0, 70, 60));
    }

}
