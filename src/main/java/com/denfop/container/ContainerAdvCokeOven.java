package com.denfop.container;

import com.denfop.tiles.adv_cokeoven.TileCokeOvenMain;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;

public class ContainerAdvCokeOven extends ContainerFullInv<TileCokeOvenMain> {

    public ContainerAdvCokeOven(Player entityPlayer, TileCokeOvenMain tileEntityBlastFurnaceMain) {
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
    public void removed(@Nonnull final Player playerIn) {
        super.removed(playerIn);
        this.base.entityPlayerList.remove(playerIn);
    }


}
