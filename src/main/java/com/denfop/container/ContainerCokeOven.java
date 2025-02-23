package com.denfop.container;

import com.denfop.tiles.cokeoven.TileCokeOvenMain;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

public class ContainerCokeOven extends ContainerFullInv<TileCokeOvenMain> {

    public ContainerCokeOven(EntityPlayer entityPlayer, TileCokeOvenMain tileEntityBlastFurnaceMain) {
        super(entityPlayer, tileEntityBlastFurnaceMain, 182);


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.invSlotBlastFurnace, 0,
                63, 44
        ));


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.output1,
                0, 32, 78
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.fluidSlot,
                0, 8, 78
        ));

        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.output2,
                0, 152, 78
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.fluidSlot1,
                0, 131, 78
        ));
    }


    @Override
    public void onContainerClosed(@Nonnull final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.base.entityPlayerList.remove(playerIn);
    }


}
