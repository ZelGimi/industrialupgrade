package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityRecipeTuner;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuTunerRecipe extends ContainerMenuFullInv<BlockEntityRecipeTuner> {

    public ContainerMenuTunerRecipe(BlockEntityRecipeTuner tileEntityRecipeTuner, Player var1) {
        super(tileEntityRecipeTuner, var1);
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityRecipeTuner.slot, i, 8 + i * 18, 62));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntityRecipeTuner.input_slot, 0, 8 + 2 * 15, 38));

    }

}
