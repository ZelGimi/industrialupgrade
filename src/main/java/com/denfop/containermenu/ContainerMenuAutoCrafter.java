package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityAutoCrafter;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuAutoCrafter extends ContainerMenuFullInv<BlockEntityAutoCrafter> {

    public ContainerMenuAutoCrafter(BlockEntityAutoCrafter tileEntityAutoCrafter, Player var1) {
        super(var1, tileEntityAutoCrafter, 202);
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new SlotVirtual(tileEntityAutoCrafter, i, 10 + (i % 3) * 18, 18 + (i / 3) * 18,
                    tileEntityAutoCrafter.getAutoCrafter()
            ));
        }
        this.addSlotToContainer(new SlotInvSlot(tileEntityAutoCrafter.outputSlot, 0, 100, 35));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityAutoCrafter.upgradeSlot, i, 152, 8 + i * 18));
        }
        for (int i = 0; i < 18; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntityAutoCrafter.getSlot(), i, 8 + (i % 9) * 18, 82 + (i / 9) * 18
            ));
        }
    }

}
