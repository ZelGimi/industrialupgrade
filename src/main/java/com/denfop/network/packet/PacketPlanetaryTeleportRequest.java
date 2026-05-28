package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.space.teleport.SpaceTeleportController;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PacketPlanetaryTeleportRequest implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketPlanetaryTeleportRequest() {
    }

    public PacketPlanetaryTeleportRequest(final String bodyName, Level level) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(level.registryAccess());
        buffer.writeByte(getId());
        buffer.writeString(bodyName == null ? "" : bodyName);
        this.buffer = buffer;
        IUCore.network.getClient().sendPacket(this, buffer);
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