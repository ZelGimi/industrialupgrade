package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.blastfurnace.block.BlockEntityBlastFurnaceMain;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuBlastFurnace extends ContainerMenuFullInv<BlockEntityBlastFurnaceMain> {

    public ContainerMenuBlastFurnace(Player entityPlayer, BlockEntityBlastFurnaceMain tileEntityBlastFurnaceMain) {
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


}
