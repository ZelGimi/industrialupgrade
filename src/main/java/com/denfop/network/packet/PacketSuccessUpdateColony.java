package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class PacketSuccessUpdateColony implements IPacket {

    public PacketSuccessUpdateColony() {
    }

    ;

    public PacketSuccessUpdateColony(EntityPlayer entityPlayer) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());
        customPacketBuffer.writeUniqueId(entityPlayer.getUniqueID());
        IUCore.network.getServer().sendPacket(customPacketBuffer, (EntityPlayerMP) entityPlayer);
    }

    @Override
    public byte getId() {
        return 48;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        UUID uuid;
        uuid = (UUID) customPacketBuffer.readUniqueId();
        if (entityPlayer.getUniqueID().equals(uuid)) {
            entityPlayer.inventory.getItemStack().shrink(1);
            if (entityPlayer.inventory.getItemStack().getCount() == 0) {
                entityPlayer.inventory.setItemStack(ItemStack.EMPTY);
            }
            entityPlayer.inventoryContainer.detectAndSendChanges();
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
