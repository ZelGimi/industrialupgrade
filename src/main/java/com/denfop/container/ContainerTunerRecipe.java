package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityRecipeTuner;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerTunerRecipe extends ContainerFullInv<TileEntityRecipeTuner> {

    public ContainerTunerRecipe(TileEntityRecipeTuner tileEntityRecipeTuner, EntityPlayer var1) {
        super(tileEntityRecipeTuner, var1);
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new SlotInvSlot(tileEntityRecipeTuner.slot, i, 8 + i * 18, 62));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntityRecipeTuner.input_slot, 0, 8 + 2 * 15, 38));

    }

}
