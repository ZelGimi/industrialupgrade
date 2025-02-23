package com.denfop.container;

import com.denfop.tiles.mechanism.blastfurnace.block.TileBlastFurnaceMain;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

public class ContainerBlastFurnace extends ContainerFullInv<TileBlastFurnaceMain> {

    public ContainerBlastFurnace(EntityPlayer entityPlayer, TileBlastFurnaceMain tileEntityBlastFurnaceMain) {
        super(entityPlayer, tileEntityBlastFurnaceMain, 182);


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.output, 0,
                139, 45
        ));


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.invSlotBlastFurnace, 0,
                63, 44
        ));


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.output1,
                0, 32, 78
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.fluidSlot,
                0, 8, 78
        ));
    }


    @Override
    public void onContainerClosed(@Nonnull final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.base.entityPlayerList.remove(playerIn);
    }


}
