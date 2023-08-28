package com.denfop.container;

import com.denfop.tiles.mechanism.blastfurnace.block.TileBlastFurnaceMain;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

public class ContainerBlastFurnace extends ContainerFullInv<TileBlastFurnaceMain> {

    public ContainerBlastFurnace(EntityPlayer entityPlayer, TileBlastFurnaceMain tileEntityBlastFurnaceMain) {
        super(entityPlayer, tileEntityBlastFurnaceMain, 166);


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.output, 0,
                116, 35
        ));


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.invSlotBlastFurnace, 0,
                56, 34
        ));


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.output1,
                0, 29, 62
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.fluidSlot,
                0, 8, 62
        ));
    }


    @Override
    public void onContainerClosed(@Nonnull final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.base.entityPlayerList.remove(playerIn);
    }


}
