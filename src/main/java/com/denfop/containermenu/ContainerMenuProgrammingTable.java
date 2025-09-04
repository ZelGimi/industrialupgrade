package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityProgrammingTable;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuProgrammingTable extends ContainerMenuFullInv<BlockEntityProgrammingTable> {

    public ContainerMenuProgrammingTable(BlockEntityProgrammingTable tleEntityMatterFactory, Player var1) {
        super(tleEntityMatterFactory, var1);
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.inputSlotA, 0, 30, 45));
        this.addSlotToContainer(new SlotInvSlot(tleEntityMatterFactory.outputSlot, 0, 90, 45));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tleEntityMatterFactory).upgradeSlot, i, 152, 8 + i * 18));
        }
    }

}
