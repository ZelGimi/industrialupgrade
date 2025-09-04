package com.denfop.containermenu;


import com.denfop.blockentity.base.BlockEntityElectricBlock;
import com.denfop.containermenu.slot.SlotInvSlot;
import com.denfop.inventory.SlotArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ContainerMenuElectricBlock extends ContainerMenuFullInv<BlockEntityElectricBlock> {

    public ContainerMenuElectricBlock(Player entityPlayer, BlockEntityElectricBlock tileEntity) {
        super(null, tileEntity, 167);
        this.player = entityPlayer;
        this.inventory = entityPlayer.getInventory();
        final List<EquipmentSlot> list = Arrays
                .stream(EquipmentSlot.values())
                .filter(type -> type.getType() == EquipmentSlot.Type.ARMOR)
                .collect(
                        Collectors.toList());
        Collections.reverse(list);

        for (int col = 0; col < list.size(); ++col) {
            this.addSlotToContainer(new SlotArmor(entityPlayer.getInventory(), list.get(col), 8 + col * 18, 63));
        }
        final int width = 178;
        final int height = 167;
        int xStart = (width - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(
                        entityPlayer.getInventory(),
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(entityPlayer.getInventory(), col, xStart + col * 18, -1 + height + -24));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity.inputslotA, 0, 35, 19));
        addSlotToContainer(new SlotInvSlot(tileEntity.inputslotB, 0, 35, 41));
        addSlotToContainer(new SlotInvSlot(tileEntity.inputslotC, 0, 134, 63));
        addSlotToContainer(new SlotInvSlot(tileEntity.inputslotC, 1, 152, 63));

    }


}
