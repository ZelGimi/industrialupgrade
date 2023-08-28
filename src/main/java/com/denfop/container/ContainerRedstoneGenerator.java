package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.redstone.TileBaseRedstoneGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerRedstoneGenerator extends ContainerFullInv<TileBaseRedstoneGenerator> {

    public ContainerRedstoneGenerator(EntityPlayer entityPlayer, TileBaseRedstoneGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slot, 0, 65, 53));
    }


}
