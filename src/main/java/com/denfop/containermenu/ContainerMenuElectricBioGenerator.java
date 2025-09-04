package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityBioGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuElectricBioGenerator extends ContainerMenuFullInv<BlockEntityBioGenerator> {

    public ContainerMenuElectricBioGenerator(Player entityPlayer, BlockEntityBioGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 43, 40));


    }


}
