package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityResearchTableSpace;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class ContainerMenuResearchTableSpace extends ContainerMenuFullInv<BlockEntityResearchTableSpace> {

    public final Player player;

    public ContainerMenuResearchTableSpace(BlockEntityResearchTableSpace tileEntityResearchTableSpace, Player var1) {
        super(var1, tileEntityResearchTableSpace, 255);
        this.addSlotToContainer(new SlotInvSlot(tileEntityResearchTableSpace.slotLens, 0, 197 + 9, 233));
        this.player = var1;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        this.base.timer = 5;
    }

    protected void addPlayerInventorySlots(Inventory inventory, int width, int height) {
        int n4 = (width - 162) / 2;

        int n3;


        for (n3 = 0; n3 < 9; ++n3) {
            this.addSlot(new Slot(inventory, n3, n4 + n3 * 18 + 18, height + -22));
        }

    }


}
