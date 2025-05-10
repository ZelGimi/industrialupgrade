package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityBioGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerElectricBioGenerator extends ContainerFullInv<TileEntityBioGenerator> {

    public ContainerElectricBioGenerator(Player entityPlayer, TileEntityBioGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 43, 40));


    }


}
