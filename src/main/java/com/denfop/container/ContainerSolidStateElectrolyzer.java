package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntitySolidStateElectrolyzer;
import net.minecraft.world.entity.player.Player;

public class ContainerSolidStateElectrolyzer extends ContainerFullInv<TileEntitySolidStateElectrolyzer> {

    public ContainerSolidStateElectrolyzer(Player var1, TileEntitySolidStateElectrolyzer tileEntity1) {
        super(var1, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 0, 120, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 120, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 40, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.cathodeslot, 0, 65, 20));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.anodeslot, 0, 65, 68));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 95, 44));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 148, 21 + i * 18
            ));
        }
    }

}
