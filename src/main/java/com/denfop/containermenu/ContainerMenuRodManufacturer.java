package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityRodManufacturer;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuRodManufacturer extends ContainerMenuFullInv<BlockEntityRodManufacturer> {

    public ContainerMenuRodManufacturer(BlockEntityRodManufacturer tile, Player entityPlayer) {
        super(entityPlayer, tile, 166);

        this.addSlotToContainer(new SlotInvSlot(tile.inputSlotA, 0, 38, 16));
        this.addSlotToContainer(new SlotInvSlot(tile.inputSlotA, 1, 38, 34));
        this.addSlotToContainer(new SlotInvSlot(tile.inputSlotA, 2, 38, 52));
        this.addSlotToContainer(new SlotInvSlot(tile.inputSlotA, 3, 56, 16));
        this.addSlotToContainer(new SlotInvSlot(tile.inputSlotA, 4, 56, 34));
        this.addSlotToContainer(new SlotInvSlot(tile.inputSlotA, 5, 56, 52));
        addSlotToContainer(new SlotInvSlot(tile.outputSlot,
                0, 116, 35
        ));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tile.upgradeSlot,
                    i, 152, 8 + i * 18
            ));
        }
        addSlotToContainer(new SlotInvSlot(tile.input_slot,
                0, -20, 84
        ));

    }


}
