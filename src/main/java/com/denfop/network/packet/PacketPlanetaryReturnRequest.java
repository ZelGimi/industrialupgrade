package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.space.teleport.SpaceTeleportController;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketPlanetaryReturnRequest implements IPacket {

    public PacketPlanetaryReturnRequest() {
    }

    public PacketPlanetaryReturnRequest(final boolean ignored) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(getId());
        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 92;
    }

    @Override
    public void readPacket(final CustomPacketBuffer buffer, final Player entityPlayer) {
        if (entityPlayer instanceof ServerPlayer serverPlayer) {
            SpaceTeleportController.requestReturn(serverPlayer);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }
}