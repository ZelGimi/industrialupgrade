package com.denfop.network.packet;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class PacketRelocatorTeleportPlayer implements IPacket {

    public PacketRelocatorTeleportPlayer() {

    }

    public PacketRelocatorTeleportPlayer(Player player, Point point) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        buffer.writeByte(getId());
        buffer.writeUUID(player.getUUID());
        point.writeToBuffer(buffer);
        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 52;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        UUID uuid = customPacketBuffer.readUUID();
        if (entityPlayer.getUUID().equals(uuid)) {
            Point point = new Point(customPacketBuffer);
            ItemStack stack = entityPlayer.getMainHandItem();
            if (ElectricItem.manager.canUse(stack, 1000000)) {
                ElectricItem.manager.discharge(stack, 1000000, 14, true, false, false);
                entityPlayer.closeContainer();
                RelocatorNetwork.instance.teleportPlayer(entityPlayer, point);
            }

        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
