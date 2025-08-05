package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class PacketSuccessUpdateColony implements IPacket {

    private CustomPacketBuffer buffer;

    ;

    public PacketSuccessUpdateColony() {
    }

    public PacketSuccessUpdateColony(Player entityPlayer) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(entityPlayer.registryAccess());
        customPacketBuffer.writeByte(getId());
        customPacketBuffer.writeUUID(entityPlayer.getUUID());
        this.buffer = customPacketBuffer;
        IUCore.network.getServer().sendPacket(this, customPacketBuffer, (ServerPlayer) entityPlayer);
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
    }

    @Override
    public byte getId() {
        return 48;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        UUID uuid;
        uuid = (UUID) customPacketBuffer.readUUID();
        if (entityPlayer.getUUID().equals(uuid)) {
            entityPlayer.getInventory().getSelected().shrink(1);
            if (entityPlayer.getInventory().getSelected().getCount() == 0) {
                entityPlayer.getInventory().setPickedItem(ItemStack.EMPTY);
            }
            entityPlayer.containerMenu.broadcastChanges();
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
