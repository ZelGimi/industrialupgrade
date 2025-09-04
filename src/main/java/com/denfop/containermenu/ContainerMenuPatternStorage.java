package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPatternStorage;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPatternStorage extends ContainerMenuFullInv<BlockEntityPatternStorage> {

    public ContainerMenuPatternStorage(Player player, BlockEntityPatternStorage tileEntity1) {
        super(player, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.diskSlot, 0, 18, 20));
    }


}
