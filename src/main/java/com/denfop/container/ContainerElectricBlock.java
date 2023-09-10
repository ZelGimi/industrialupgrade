package com.denfop.container;

import com.denfop.invslot.SlotArmor;
import com.denfop.tiles.base.TileElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ContainerElectricBlock extends ContainerFullInv<TileElectricBlock> {

    public ContainerElectricBlock(EntityPlayer entityPlayer, TileElectricBlock tileEntity) {
        super(entityPlayer, tileEntity, 196);
        final List<EntityEquipmentSlot> list = Arrays
                .stream(EntityEquipmentSlot.values())
                .filter(type -> type.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
                .collect(
                        Collectors.toList());
        Collections.reverse(list);
        for (int col = 0; col < list.size(); ++col) {
            this.addSlotToContainer(new SlotArmor(entityPlayer.inventory, list.get(col), 8 + col * 18, 84));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity.inputslotA, 0, 56, 17));
        addSlotToContainer(new SlotInvSlot(tileEntity.inputslotB, 0, 56, 53));
        addSlotToContainer(new SlotInvSlot(tileEntity.inputslotC, 0, 56 - 36, 17));
        addSlotToContainer(new SlotInvSlot(tileEntity.inputslotC, 1, 56 - 36, 17 + 18));

    }


}
