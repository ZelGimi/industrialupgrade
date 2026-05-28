package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.space.teleport.SpaceTeleportController;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketPlanetaryTeleportRequest implements IPacket {

    public PacketPlanetaryTeleportRequest() {
    }

    public PacketPlanetaryTeleportRequest(final String bodyName) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(getId());
        buffer.writeString(bodyName == null ? "" : bodyName);
        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 91;
    }

    @Override
    public void readPacket(final CustomPacketBuffer buffer, final Player entityPlayer) {
        if (!(entityPlayer instanceof ServerPlayer serverPlayer)) {
            return;
        }

        String bodyName = buffer.readString();
        SpaceTeleportController.requestTeleport(serverPlayer, bodyName);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }
}