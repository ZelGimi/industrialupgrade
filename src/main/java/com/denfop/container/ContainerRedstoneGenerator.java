package com.denfop.container;

import com.denfop.tiles.mechanism.generator.energy.redstone.TileBaseRedstoneGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerRedstoneGenerator extends ContainerFullInv<TileBaseRedstoneGenerator> {

    public ContainerRedstoneGenerator(Player entityPlayer, TileBaseRedstoneGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.slot, 0, 65, 53));
    }


}
